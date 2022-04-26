package io.pleo.antaeus.app

import io.pleo.antaeus.core.scheduler.BillingSchedulerService
import io.pleo.antaeus.core.services.BillingService
import org.quartz.CronScheduleBuilder
import org.quartz.JobBuilder
import org.quartz.JobDetail
import org.quartz.Scheduler
import org.quartz.Trigger
import org.quartz.TriggerBuilder
import org.quartz.impl.StdSchedulerFactory

class MainSchedulerConfig {

     fun initiate(billingService: BillingService){
         val invoiceBillingJob :JobDetail = JobBuilder.newJob(BillingSchedulerService::class.java)
             .build()
         invoiceBillingJob.jobDataMap["billingService"] = billingService
         val triggerInvoiceBilling: Trigger = TriggerBuilder.newTrigger()
             .withSchedule(CronScheduleBuilder.cronSchedule("0 0 10 1 * ?"))
             .build()
         val invoiceScheduler: Scheduler = StdSchedulerFactory().scheduler
         invoiceScheduler.start()
         invoiceScheduler.scheduleJob(invoiceBillingJob, triggerInvoiceBilling)
     }
}