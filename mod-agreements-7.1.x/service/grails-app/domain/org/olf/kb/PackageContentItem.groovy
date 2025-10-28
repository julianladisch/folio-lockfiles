package org.olf.kb

import java.time.LocalDate

import grails.gorm.MultiTenant
import org.olf.general.StringUtils


/**
 * mod-erm representation of a package
 */
public class PackageContentItem extends ErmResource implements MultiTenant<PackageContentItem> {

  Pkg pkg
  PlatformTitleInstance pti

  String getName() {
    "'${ StringUtils.truncate( pti?.titleInstance?.name, 70 ) }' on Platform '${ StringUtils.truncate( pti?.platform?.name, 70 ) }' in Package ${ StringUtils.truncate( pkg.name, 70 ) }" as String
  }

  String getLongName() {
    "'${pti.titleInstance.name}' on Platform '${pti.platform.name}' in Package ${pkg.name}" as String
  }

  static transients = ['longName']

  // Track this package content item - when did we first detect it (added) when did we last
  // see it, and when did we determine it has been removed?
  Long addedTimestamp
  Long removedTimestamp
  Long lastSeenTimestamp

  // The date range on which this content item is live within the package
  LocalDate accessStart
  LocalDate accessEnd

  // A field primarily to deposit KBART::CoverageNote type data
  String note

  // A field primarily to deposit KBART::CoverageDepth type data
  String depth

  // KBART::Embargo data
  Embargo embargo

  static mapping = {
                  pkg column:'pci_pkg_fk'
                  pti column:'pci_pti_fk'
          accessStart column:'pci_access_start'
            accessEnd column:'pci_access_end'
              embargo column:'pci_embargo_fk'
                 note column:'pci_note'
                depth column:'pci_depth'
       addedTimestamp column:'pci_added_ts'
     removedTimestamp column:'pci_removed_ts'
    lastSeenTimestamp column:'pci_last_seen_ts'
  }

  static constraints = {
                  pkg(nullable:false)
                  pti(nullable:false)
          accessStart(nullable:true)
            accessEnd(nullable:true)
                 note(nullable:true, blank:false)
                depth(nullable:true, blank:false)
       addedTimestamp(nullable:true)
     removedTimestamp(nullable:true)
    lastSeenTimestamp(nullable:true)
              embargo(nullable:true)
  }

  /**
   * Gather together all coverage information into a single summary statement that can be used in search results.
   * see: https://www.editeur.org/files/ONIX%20for%20Serials%20-%20Coverage/20120326_ONIX_Coverage_overview_v1_0.pdf
   */
  public String generateCoverageSummary() {
    return coverageStatements.join('; ');
  }
}
