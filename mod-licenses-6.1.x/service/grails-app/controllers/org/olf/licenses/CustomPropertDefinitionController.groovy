package org.olf.licenses

import javax.servlet.ServletRequest

import com.k_int.okapi.OkapiTenantAwareController
import com.k_int.web.toolkit.custprops.CustomPropertyDefinition

import grails.databinding.SimpleMapDataBindingSource
import grails.gorm.multitenancy.CurrentTenant
import grails.web.Controller
import groovy.util.logging.Slf4j

@Slf4j
@CurrentTenant
@Controller
class CustomPropertyDefinitionController extends OkapiTenantAwareController<CustomPropertyDefinition> {
  
  CustomPropertyDefinitionController() {
    super(CustomPropertyDefinition)
  }
  
  protected def doTheLookup (Class res = this.resource, Closure baseQuery) {
    final int offset = params.int("offset") ?: 0
    final int perPage = Math.min(params.int('perPage') ?: params.int('max') ?: 10, 100)
    final int page = params.int("page") ?: (offset ? (offset / perPage) + 1 : 1)
    final List<String> filters = params.list("filters[]") ?: params.list("filters")
    final List<String> match_in = params.list("match[]") ?: params.list("match")
    
    // This is an ugly way to set defaults. We need to fix this.
    final List<String> sorts = params.list("sort[]") ?: params.list("sort") ?: [ 'primary;desc', 'weight;asc', 'label;asc', 'id;asc']
    
    if (params.boolean('stats')) {
      return simpleLookupService.lookupWithStats(res, params.term, perPage, page, filters, match_in, sorts, null, baseQuery)
    } else {
      return simpleLookupService.lookup(res, params.term, perPage, page, filters, match_in, sorts, baseQuery)
    }
  }
  
  protected CustomPropertyDefinition createResource(Map parameters) {
    def res
    if (!parameters.type) {
      res = super.createResource(parameters)
    } else {
      res = resource.forType("${parameters.type}", parameters)
    }
    
    res
  }
  
  protected CustomPropertyDefinition createResource() {
    def instance
    def json = getObjectToBind()
    if (json && json.type) {
      instance = resource.forType("${json.type}")
    }
    
    if (!instance) {
      instance = super.createResource()
    }
    
    bindData instance, (json ? new SimpleMapDataBindingSource(json) : getObjectToBind()), ['exclude': ['type']]
    instance
  }

  List<String> fetchContexts() {
    List<String> contexts = CustomPropertyDefinition.createCriteria().list {
      isNotNull('ctx')

      projections {
        distinct 'ctx'
      }
    }
    respond contexts
  }
}
