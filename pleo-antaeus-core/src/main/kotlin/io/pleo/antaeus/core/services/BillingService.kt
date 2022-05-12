package io.pleo.antaeus.core.services

import io.pleo.antaeus.core.exceptions.*
import io.pleo.antaeus.core.external.PaymentProvider
import io.pleo.antaeus.models.Invoice
import io.pleo.antaeus.models.InvoiceStatus
import mu.KotlinLogging
import java.lang.Thread.sleep

class BillingService(private val paymentProvider: PaymentProvider, private val invoiceService: InvoiceService) {
    private val logger = KotlinLogging.logger {}
    private val numberOfTrial = 2

    fun billPendingInvoices() {
        invoiceService.fetchAllPendingInvoice().forEach { invoice ->
            try {
                billInvoices(invoice)
            } catch (e: Exception) {
                logger.info { "failed to process invoice {}" }
            }

        }
    }

    fun billSingleInvoice(id: Int): Invoice {
        val invoice = invoiceService.fetch(id)
        if (invoice.status == InvoiceStatus.PENDING) {
            billInvoices(invoice)
        } else {
            throw InvoiceAlreadyBilledException(id)
        }

        return invoiceService.fetch(id)

    }

    private fun billInvoices(invoice: Invoice, failedTrials: Int = numberOfTrial) {

        try {

            if (paymentProvider.charge(invoice)) {
                invoiceService.updateInvoiceStatus(invoice, InvoiceStatus.PAID)
            } else {
                throw PaymentProviderFailureException(invoice.id)
            }

        } catch (e: Exception) {
            when (e) {
                is CurrencyMismatchException, is CustomerNotFoundException -> {
                    logger.error("billing invoice failed due to: ", e)
                }
                is NetworkException -> {
                    if (numberOfTrial > 0) {
                        sleep(10000)
                        return billInvoices(invoice, failedTrials - 1)
                    }
                    logger.error("Billing invoice failed due to: ", e)
                }
                else -> logger.error("Something went wrong please try again later: ", e)

            }
            throw  e
        }

    }


}
