package io.pleo.antaeus.core.scheduler

import io.pleo.antaeus.core.services.BillingService
import mu.KotlinLogging
import org.quartz.Job
import org.quartz.JobExecutionContext
import org.quartz.JobExecutionException

class BillingSchedulerService : Job {

    private val logger = KotlinLogging.logger {}

    override fun execute(context: JobExecutionContext) {

        try {
            val billingServiceSchedule = context.jobDetail.jobDataMap["billingService"] as BillingService
            billingServiceSchedule.billPendingInvoice()

        } catch (e: JobExecutionException) {
            logger.error("Billing scheduler failed due to: ", e)
        }


    }
}