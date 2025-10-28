package org.olf.licenses

import org.olf.DependentModuleProxyService
import org.olf.general.Org

import com.k_int.okapi.OkapiTenantAwareController

import grails.gorm.multitenancy.CurrentTenant
import groovy.util.logging.Slf4j

@Slf4j
@CurrentTenant
class OrgController extends OkapiTenantAwareController<Org>  {

  DependentModuleProxyService dependentModuleProxyService
  
  OrgController() {
    super(Org)
  }
  
  public find(String id) {
    respond dependentModuleProxyService.coordinateOrg(id)
  }
}

