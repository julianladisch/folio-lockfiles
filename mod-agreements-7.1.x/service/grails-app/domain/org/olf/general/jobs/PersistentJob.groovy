package org.olf.general.jobs
import java.time.Instant

import com.k_int.web.toolkit.async.WithPromises
import com.k_int.web.toolkit.files.SingleFileAttachment
import com.k_int.web.toolkit.refdata.CategoryId
import com.k_int.web.toolkit.refdata.Defaults
import com.k_int.web.toolkit.refdata.RefdataValue

import grails.async.Promise
import grails.events.bus.EventBusAware
import grails.gorm.MultiTenant
import grails.gorm.dirty.checking.DirtyCheck
import grails.gorm.multitenancy.Tenants
import grails.util.Holders

@DirtyCheck
abstract class PersistentJob extends SingleFileAttachment implements EventBusAware, MultiTenant<PersistentJob> {
	
	public static enum Type {
		JOB,
		TASK
	}
  
  static transients = ['type', 'work', 'errorLog', 'errorLogCount', 'infoLog', 'infoLogCount', 'fullLog', 'fullLogCount']
    
  String name
  
  String runnerId
  
  @CategoryId(value='PersistentJob.Status', defaultInternal=true) // Workaround for a bug in toolkit creating a category for each extension even when not specified.
  @Defaults(['Queued', 'In progress', 'Ended'])
  RefdataValue status
  
  Instant dateCreated
  Instant started
  Instant ended
  
  @CategoryId(value='PersistentJob.Result', defaultInternal=true) // Workaround for a bug in toolkit creating a category for each extension even when not specified.
  @Defaults(['Success', 'Partial success', 'Failure', 'Interrupted'])
  RefdataValue result
  
  static mapping = {
//    tablePerHierarchy false
                      version false
                     name column:'job_name'
                   status column:'job_status_fk'
              dateCreated column:'job_date_created'
                  started column:'job_started'
                    ended column:'job_ended'
                   result column:'job_result_fk'
                 runnerId column:'job_runner_id', length:36
  }

  static constraints = {
                    name (nullable:false, blank:false)
                  status (nullable:false, bindable: false)
             dateCreated (nullable:true)
                 started (nullable:true)
                   ended (nullable:true)
                  result (nullable:true)
                runnerId (nullable:true, bindable: false, blank: false)
  }

//  def afterInsert () {
//    // Ugly work around events being raised on multi-tenant GORM entities not finding subscribers
//    // from the root context.
//    final String jobId = this.id
//    final String tenantId = Tenants.currentId()
////    Promise background = WithPromises.task {
//      JobRunnerService jrs = Holders.applicationContext.getBean('jobRunnerService')
//      jrs.handleNewJob(jobId, tenantId)
//    }
//    background.onError { Throwable e ->
//      log.error "Couldn't add job", e
//    }
//  }
  
  long getErrorLogCount() {
    Tenants.withCurrent {
      LogEntry.countByOriginAndType (this.id, LogEntry.TYPE_ERROR)
    }
  }
  
  List<LogEntry> getErrorLog() {
    Tenants.withCurrent {
      LogEntry.findAllByOriginAndType (this.id, LogEntry.TYPE_ERROR, [sort: 'dateCreated', order: "asc"])
    }
  }
  
  long getInfoLogCount() {
    Tenants.withCurrent {
      LogEntry.countByOriginAndType (this.id, LogEntry.TYPE_INFO)
    }
  }
  
  List<LogEntry> getInfoLog() {
    Tenants.withCurrent {
      LogEntry.findAllByOriginAndType (this.id, LogEntry.TYPE_INFO, [sort: 'dateCreated', order: "asc"])
    }
  }
  
  long getFullLogCount() {
    Tenants.withCurrent {
      LogEntry.countByOrigin (this.id)
    }
  }
  
  List<LogEntry> getFullLog() {
    Tenants.withCurrent {
      LogEntry.findAllByOrigin(this.id, [sort: 'dateCreated', order: "asc"])
    }
  }
  
  abstract Runnable getWork()
  
  String toString() {
    "${name}"
  }

	/**
	 * If the job was interrupted, this method is called by the framework to do any job specific handling of an interrupted
	 * scenario.

    * This needs overwriting in implementing classes that want special logic
	 */
  Runnable getOnInterrupted() {
    return { String tenantId, String jobId ->
      log.debug("No onInterrupted implemented, default implementation called as fallback")
    }
  }
  
  public Type getType() {
		return Type.JOB;
	}
}
