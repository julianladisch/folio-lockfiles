package org.olf.licenses

import grails.gorm.multitenancy.CurrentTenant
import groovy.util.logging.Slf4j
import com.k_int.okapi.OkapiTenantAwareController
import org.olf.licenses.InternalContact
import grails.converters.JSON


/**
 * Access to InternalContact resources
 */
@Slf4j
@CurrentTenant
class InternalContactController extends OkapiTenantAwareController<InternalContactController>  {

  InternalContactController() {
    super(InternalContact)
  }

}
