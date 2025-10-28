package org.olf.kb.adapters

import static groovy.transform.TypeCheckingMode.SKIP

import java.text.*

import org.olf.TitleEnricherService
import org.olf.dataimport.internal.InternalPackageImplWithPackageContents
import org.olf.dataimport.internal.PackageContentImpl
import org.olf.dataimport.internal.PackageSchema
import org.olf.dataimport.internal.PackageSchema.ContentItemSchema
import org.olf.kb.KBCache
import org.olf.kb.KBCacheUpdater
import org.springframework.validation.BindingResult

import grails.web.databinding.DataBinder
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
// Can't cast between groovy.util.slurpersupport.GPathResult and groovy.xml.slurpersupport.GPathResult for some reason
import groovy.xml.XmlSlurper
import groovy.xml.slurpersupport.GPathResult
import groovyx.net.http.*

import org.slf4j.MDC

/**
 * A debug adapter to treat some XML file as a GoKB package harvest
 */

@Slf4j
@CompileStatic
public class DebugGoKbAdapter extends GOKbOAIAdapter {
  private static final String XML_FILE_LOCATION = "src/integration-test/resources/DebugGoKbAdapter"

  private String getXMLFile() {
    // Full override path to file eg "src/integration-test/resources/DebugGoKbAdapter/exampleXMLPage.xml"
    String filePath = System.getenv("XML_FILE_PATH");

    if (filePath) {
      return filePath;
    }

    // Split directory/fileName envs
    String fileDirectory = System.getenv("XML_FILE_DIRECTORY");
    String fileName = System.getenv("XML_FILE");

    return "${fileDirectory ?: XML_FILE_LOCATION}/${fileName ?: 'exampleXMLPage.xml'}";
  }

  public void freshenPackageData(final String source_name,
                                 final String base_url,
                                 final String current_cursor,
                                 final KBCache cache,
                                 final boolean trustedSourceTI = false) {

    GPathResult xml

    def pageXml = new XmlSlurper().parse(new File(base_url ?: getXMLFile()))

    if (( pageXml instanceof GPathResult ) ) {
      xml = (GPathResult) pageXml;
      Map page_result = processPackagePage('', xml, source_name, cache, trustedSourceTI)
    } else {
      log.warn("Not a GPathResult... skipping");
    }
  }

  public void freshenTitleData(String source_name,
                                 String base_url,
                                 String current_cursor,
                                 KBCache cache,
                                 boolean trustedSourceTI = false) {
    throw new RuntimeException("Title data not suported by DebugGoKbAdapter")
  }

  public void freshenHoldingsData(String cursor,
                                  String source_name,
                                  KBCache cache) {
    throw new RuntimeException("Holdings data not suported by DebugGoKbAdapter")
  }

  @CompileStatic(SKIP)
  protected Map processPackagePage(String cursor, GPathResult oai_page, String source_name, KBCache cache, boolean trustedSourceTI) {

    final SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")

    // Force the formatter to use UCT because we want "Z" as the timezone
    sdf.setTimeZone(TimeZone.getTimeZone("UTC"))

    def result = [:]


    // If there is no cursor, initialise it to an empty string.
    result.new_cursor = (cursor && cursor.trim()) != '' ? cursor : ''
    result.count = 0

    log.debug("DebugGoKbAdapter::processPackagePage(${cursor},...")

    // Remove the ThreadLocal<Set> containing ids of TIs enriched by this process.
    TitleEnricherService.enrichedIds.remove()

    oai_page.ListRecords.record.each { record ->
      result.count++
      def record_identifier = record?.header?.identifier?.text()
      def package_name = record?.metadata?.gokb?.package?.name?.text()
      def primary_slug = record?.metadata?.gokb?.package?.find{
        it.@uuid?.text() != null && it.@uuid?.text()?.trim() != ''
      }?.@uuid?.text()
      def datestamp = record?.header?.datestamp?.text()
      def editStatus = record?.metadata?.gokb?.package?.editStatus?.text()
      def listStatus = record?.metadata?.gokb?.package?.listStatus?.text()
      def packageStatus = record?.metadata?.gokb?.package?.status?.text()

      log.debug("Processing OAI record :: ${result.count} ${record_identifier} ${package_name}")

      if (!package_name) {
        log.info("Ignoring Package '${record_identifier}' because package_name is missing")
      } else if (!primary_slug) {
        log.info("Ignoring Package '${record_identifier}' because primary_slug is missing")
      } else if (editStatus.toLowerCase() == 'rejected') {
        log.info("Ignoring Package '${package_name}' because editStatus=='${editStatus}'")
      } else if (listStatus.toLowerCase() != 'checked') {
        log.info("Ignoring Package '${package_name}' because listStatus=='${listStatus}' (required: 'checked')")
      } else {
        PackageSchema json_package_description = gokbToERM(record, trustedSourceTI)
        cache.onPackageChange(source_name, json_package_description)
      }

      if ( datestamp > result.new_cursor ) {
        log.debug("Datestamp from record \"${datestamp}\" larger than current cursor (\"${result.new_cursor}\") - update it")
        // Because OAI uses >= we want to nudge up the cursor timestamp by 1s (2019-02-06T11:19:20Z)
        Date parsed_datestamp = parseDate(datestamp) // sdf.parse(datestamp)
        long incremented_datestamp = parsed_datestamp.getTime()+1000
        String new_string_datestamp = sdf.format(new Date(incremented_datestamp))

        log.debug("New cursor value - \"${datestamp}\" > \"${result.new_cursor}\" - updating as \"${new_string_datestamp}\"")
        result.new_cursor = new_string_datestamp
        log.debug("result.new_cursor=${result.new_cursor}")
      }
    }

    result.resumptionToken = oai_page.ListRecords?.resumptionToken?.text()
    return result
  }

  @CompileStatic(SKIP)
  protected InternalPackageImplWithPackageContents gokbToERM(GPathResult xml_gokb_record, boolean trustedSourceTI) {

    def package_record = xml_gokb_record?.metadata?.gokb?.package
    def header = xml_gokb_record?.header

    def result = null

    if ( ( package_record != null ) && ( package_record.name != null ) ) {

      def package_name = package_record.name?.text()
      def package_shortcode = package_record.shortcode?.text()
      def nominal_provider = package_record.nominalProvider?.name?.text()
      def package_status = package_record.status?.text()
      def package_description = package_record?.description?.text()
      def source_data_created = package_record.dateCreated?.text()
      def source_data_updated = package_record.lastUpdated?.text()
      def availability_scope = package_record.global?.text()
      def content_types = package_record.contentType?.findAll {
        it.text() != null && it.text()?.trim() != ''
      }?.collect {
        [contentType: it.text()]
      }
      def alternate_resource_names = package_record.variantNames?.variantName?.findAll {
        it.text() != null && it.text()?.trim() != ''
      }?.collect {
        [name: it.text()]
      }

      def alternate_slugs = [];
      if (package_shortcode != null && package_shortcode?.trim() != '') {
        alternate_slugs.add(
          [
            slug: package_shortcode
          ]
        )
      }

      def identifiers = package_record.identifiers?.identifier?.findAll {
        (it.@type?.text() == null || it.@type?.text()?.trim() == '') && obtainNamespace(it) != null
      }?.collect {
        [
          namespace: obtainNamespace(it),
          value: it.@value?.text()
        ]
      }

      def ttl_prv_identifiers = package_record.identifiers?.identifier?.findAll {
        it.@type?.text()?.trim()?.toLowerCase() == 'ttl_prv' && obtainNamespace(it) != null
      }?.collect {
        [
          namespace: "gokb_ttl_prv_${obtainNamespace(it)}",
          value: it.@value?.text()
        ]
      }
      if (ttl_prv_identifiers.size()) {
        identifiers.addAll(ttl_prv_identifiers)
      }

      def gokbId_identifier = package_record.find{
        it.@id?.text() != null && it.@id?.text()?.trim() != ''
      }
      if (gokbId_identifier) {
        identifiers.add(
          [ namespace: 'gokb_id', value: gokbId_identifier.@id?.text() ]
        )
      }

      def primary_slug = package_record.find{
        it.@uuid?.text() != null && it.@uuid?.text()?.trim() != ''
      }?.@uuid?.text();
      // we don't have to check here if primary_slug (gokb uuid) is present, because if not we're ignoring the package, see above
      identifiers.add(
        [ namespace: 'gokb_uuid', value: primary_slug ]
      );

      def availability_constraints = []
      def global_note = package_record.globalNote.text()?.trim();

      // Build up availabilityConstraints
      // Ignore if availability scope is not local, regional or consortium
      if (
        (
          availability_scope?.trim()?.toLowerCase() == 'regional' ||
          availability_scope?.trim()?.toLowerCase() == 'consortium'
        ) && global_note
      ) {

        availability_constraints.add(
          [
            body: global_note
          ]
        )
      } else if (availability_scope?.trim()?.toLowerCase() == 'local') {
        def curatory_groups = package_record.curatoryGroups?.group?.name?.collect {

          return [
            body: it?.text()?.trim(),
          ]
        }

        if ((curatory_groups ?: []).size() > 0) {
          availability_constraints.addAll(curatory_groups)
        }
      }

      /* Build package description URLS */
      def package_description_urls = []
      def header_uri = header?.uri?.text()?.trim()
      def metadata_url = package_record?.descriptionURL?.text()?.trim()

      if (header_uri) {
        package_description_urls.add([ url: header_uri])
      }
      if (metadata_url) {
        package_description_urls.add([ url: metadata_url])
      }

      result = [
        header:[
          lifecycleStatus: package_status,
          availability:[
            type: 'general'
          ],
          packageProvider:[
            name:nominal_provider
          ],
          packageSource:'GOKb',
          packageName: package_name,
          trustedSourceTI: trustedSourceTI,
          packageSlug: primary_slug,
          sourceDataCreated: source_data_created,
          sourceDataUpdated: source_data_updated,
          sourceTitleCount: 0,
          availabilityConstraints: availability_constraints,
          availabilityScope: availability_scope,
          contentTypes: content_types,
          alternateResourceNames: alternate_resource_names,
          alternateSlugs: alternate_slugs,
          packageDescriptionUrls: package_description_urls,
          description: package_description
        ],
        identifiers: identifiers,
        packageContents: []
      ]

      package_record.TIPPs?.TIPP.each { tipp_entry ->
        def tipp_status = tipp_entry?.status?.text()
        // Ensure logging below has correct title for log export
        if(tipp_entry?.title?.name?.text()) {
          MDC.put("title", tipp_entry?.title?.name?.text())
        }

        // log.info("Tipp.title is of size ${tipp_entry?.title?.name?.size()} and tipp_entry?.title?.name is ${tipp_entry?.title?.name}");

        if (tipp_status == 'Current') {
          result.header.sourceTitleCount++
        }

        // Skip delete tipps, and skip tipps where no title has been properly idenitified yet for the KBart line
        if ( ( tipp_status != 'Deleted' ) && ( tipp_entry?.title?.name?.size() > 0 ) ) {

          def tipp_id = tipp_entry?.@id?.toString()
          def tipp_medium = tipp_entry?.medium?.text()

          def tipp_coverage = [] // [ "startVolume": "8", "startIssue": "1", "startDate": "1982-01-01", "endVolume": null, "endIssue": null, "endDate": null ],

          // Coverage node exists
          if (tipp_entry.coverage.size()) {
            // Our domain model does not allow null startDate or endDate
            String start_date_string = tipp_entry.coverage?.@startDate?.toString()
            String end_date_string = tipp_entry.coverage?.@endDate?.toString()

            tipp_coverage.add(["startVolume": tipp_entry.coverage?.@startVolume?.toString(),
              "startIssue": tipp_entry.coverage?.@startIssue?.toString(),
              "startDate": start_date_string?.length() > 0 ? start_date_string : null,
              "endVolume":tipp_entry.coverage?.@endVolume?.toString(),
              "endIssue": tipp_entry.coverage?.@endIssue?.toString(),
              "endDate": end_date_string?.length() > 0 ? end_date_string : null]
            )
          }

          def tipp_coverage_depth = tipp_entry.coverage.@coverageDepth?.toString()
          def tipp_coverage_note = tipp_entry.coverage.@coverageNote?.toString()

          final String embargo = tipp_entry.coverage?.@embargo?.toString()

          def tipp_url = tipp_entry.url?.text()
          def tipp_platform_url = tipp_entry.platform?.primaryUrl?.text()
          def tipp_platform_name = tipp_entry.platform?.name?.text()

          String access_start = tipp_entry.access?.@start?.toString()
          String access_end = tipp_entry.access?.@end?.toString()

          //Retired TIPPs are no longer in the package and should have an access_end, if not then make a guess at it
          if(access_end.length()==0 && tipp_status == "Retired") {
            access_end = tipp_entry.lastUpdated?.text().toString()
            // This used to appear in the job log, relegating to module logs only
            log.debug( "accessEnd date guessed for retired title: ${tipp_entry?.title?.name?.text()} in package: ${package_name}. TIPP ID: ${tipp_id}" )
          }

          Map packageContent = parseTitleInformation(tipp_entry?.title, tipp_coverage)

          packageContent << [
            "instanceMedium": tipp_medium,
            "coverage": tipp_coverage,
            "embargo": embargo,
            "coverageDepth": tipp_coverage_depth,
            "coverageNote": tipp_coverage_note,
            "platformUrl": tipp_platform_url,
            "platformName": tipp_platform_name,
            "url": tipp_url,
            "accessStart": access_start,
            "accessEnd": access_end
          ]

          // log.debug("consider tipp ${tipp_title}")

          result.packageContents.add(packageContent)
        }
        else {
          log.warn("Skipping tipp without verified title");
        }
      }
    }
    else {
      throw new RuntimeException("Problem decoding package record: ${package_record}")
    }

    InternalPackageImplWithPackageContents pkg = new InternalPackageImplWithPackageContents()
    BindingResult binding = bindData (pkg, result)
    if (binding?.hasErrors()) {
      binding.allErrors.each { log.debug "\t${it}" }
    }
    pkg
  }

  @CompileStatic(SKIP)
  // Include tipp_coverage information for media logic
  private Map parseTitleInformation(GPathResult title, def coverage = null) {
    def titleText = title?.name?.text()
    def media = null

    List instance_identifiers = [] // [ "namespace": "issn", "value": "0278-7393" ]
    List sibling_identifiers = []

    // Assuming electronic record -- store GoKB uuid as identifier so we can match on that later
    instance_identifiers.add(["namespace": "gokb_uuid", "value": title?.@uuid?.toString() ])

    // If we're processing an electronic record then issn is a sibling identifier
    // Ensure issn, pissn, pisbn end up in siblingInstanceIdentifiers
    title.identifiers.identifier.each { ti_id ->
      switch(ti_id.@namespace) {
        case 'issn':
        case 'pissn':
          sibling_identifiers.add(["namespace": "issn", "value": ti_id.@value?.toString() ])
          break;
        case 'pisbn':
          sibling_identifiers.add(["namespace": "isbn", "value": ti_id.@value?.toString() ])
          break;
        default:
          instance_identifiers.add(["namespace": ti_id.@namespace?.toString(), "value": ti_id.@value?.toString() ])
          break;
      }
    }

    // It appears that tipp_entry?.title?.type?.value() can be a list
    String title_pub_type = title?.type?.text()
    // Turns JournalInstance into journal, BookInstance into book, DatabaseInstance into database
    String pub_media = title_pub_type.replace('Instance', '')

    switch ( title_pub_type ) {
      case 'JournalInstance':
        media = 'serial'
        break
      case 'BookInstance':
        media = 'monograph'
        break
      default:
        if ( coverage ) {
          media = 'serial'
        } else {
          media = 'monograph'
        }
        break
    }

    def source_identifier = title?.@uuid?.toString()

    return ([
      "title": titleText,
      "instanceMedia": media,
      "instancePublicationMedia": pub_media,
      "sourceIdentifierNamespace": "GoKB",
      "sourceIdentifier": source_identifier,
      "instanceIdentifiers": instance_identifiers,
      "siblingInstanceIdentifiers": sibling_identifiers,
      "monographEdition": title?.editionStatement?.text(),
      "monographVolume": title?.volumeNumber?.text(),
      "firstAuthor": title?.firstAuthor?.text(),
      "firstEditor": title?.firstEditor?.text(),
      "dateMonographPublishedPrint": title?.dateFirstInPrint?.text(),
      "dateMonographPublished": title?.dateFirstOnline?.text()
    ])
  }

  @CompileStatic(SKIP)
  private String obtainNamespace(GPathResult record) {
    if (record.@namespaceName?.text() != null && record.@namespaceName?.text()?.trim() != '') {
      return record.@namespaceName?.text()?.toLowerCase()?.replaceAll(/\s+/, "_")
    } else if (record.@namespace?.text() != null && record.@namespace?.text()?.trim() != '') {
      return record.@namespace?.text()?.toLowerCase()?.replaceAll(/\s+/, "_")
    }
  }
}
