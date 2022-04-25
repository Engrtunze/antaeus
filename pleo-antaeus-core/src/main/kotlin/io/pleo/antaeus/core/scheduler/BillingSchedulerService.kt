package io.pleo.antaeus.core.scheduler

import mu.KotlinLogging
import org.quartz.Job
import org.quartz.JobExecutionContext
import org.quartz.JobExecutionException

class BillingSchedulerService : Job {

    private val logger = KotlinLogging.logger {}

    override fun execute(context: JobExecutionContext) {

        try {
            println("testing task")
        } catch (e: JobExecutionException) {
            logger.error { e }
        }


    }
}