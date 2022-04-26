package io.pleo.antaeus.core.services

import io.pleo.antaeus.core.exceptions.CurrencyMismatchException
import io.pleo.antaeus.core.exceptions.CustomerNotFoundException
import io.pleo.antaeus.core.exceptions.InvoiceNotFoundException
import io.pleo.antaeus.core.exceptions.NetworkException
import io.pleo.antaeus.core.external.PaymentProvider
import io.pleo.antaeus.models.Invoice
import io.pleo.antaeus.models.InvoiceStatus
import mu.KotlinLogging
import java.lang.Thread.sleep

class BillingService(private val paymentProvider: PaymentProvider, private val invoiceService: InvoiceService) {
    private val logger = KotlinLogging.logger {}
    private val numberOfTrial  = 3

    fun billPendingInvoice(){
        invoiceService.fetchAllPendingInvoice().forEach{ invoice -> billInvoices(invoice) }
    }

    private fun billInvoices (invoice : Invoice, failedTrials : Int = numberOfTrial)
    {

        try {

            if (paymentProvider.charge(invoice))
            {
                invoiceService.updateInvoiceStatus(invoice, InvoiceStatus.PAID )
            }

        }catch (e : Exception){
            when(e){
                is CurrencyMismatchException, is CustomerNotFoundException ->{
                    logger.error ( "billing invoice failed due to: ", e)
                }
                is NetworkException -> {
                    if(numberOfTrial > 0){
                        sleep(10000)
                        return billInvoices(invoice, failedTrials - 1)
                    }
                    logger.error ( "Billing invoice failed due to: ", e )
                }
                else -> logger.error("Something went wrong please try again later: ", e)

            }


        }

    }



}
