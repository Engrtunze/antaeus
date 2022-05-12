package io.pleo.antaeus.core.services

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.pleo.antaeus.core.exceptions.PaymentProviderFailureException
import io.pleo.antaeus.core.external.PaymentProvider
import io.pleo.antaeus.models.Currency
import io.pleo.antaeus.models.Invoice
import io.pleo.antaeus.models.InvoiceStatus
import io.pleo.antaeus.models.Money
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.math.BigDecimal

val INVOICE = Invoice(1, 1, Money(BigDecimal(1), Currency.DKK), status = InvoiceStatus.PENDING )

class BillingServiceTest {
    private val paymentProvider = mockk<PaymentProvider>()
    private val pendingInvoice = creatInvoice(InvoiceStatus.PENDING)
    private val invoiceService = mockk<InvoiceService> {
        every { fetchAllPendingInvoice() } returns listOf(pendingInvoice)
        every { updateInvoiceStatus(any(), any()) } returns 1
    }
    private val billingService = BillingService(paymentProvider = paymentProvider, invoiceService = invoiceService)


    @Test
    fun testSuccessFullyBilledInvoice() {
        every { paymentProvider.charge(any()) } returns true

        billingService.billPendingInvoices()
        verify(exactly = 1) { invoiceService.updateInvoiceStatus(any(), InvoiceStatus.PAID) }
    }

    @Test
    fun testFailedBilledInvoice() {
        every { paymentProvider.charge(any()) } returns false

        billingService.billPendingInvoices()
        verify(inverse = true) { invoiceService.updateInvoiceStatus(any(), InvoiceStatus.PAID) }

    }

    @Test
    fun testUpdateSingleInvoiceToPaid() {

        every { invoiceService.fetch(INVOICE.id) } returns INVOICE
        every { paymentProvider.charge(INVOICE) } returns true

        billingService.billSingleInvoice(INVOICE.id)

        verify { invoiceService.updateInvoiceStatus(INVOICE, InvoiceStatus.PAID) }
    }

    @Test
    fun testDoesNotUpdateSingleInvoice() {
        every { invoiceService.fetch(INVOICE.id) } returns INVOICE
        every { paymentProvider.charge(INVOICE) } returns false


        assertThrows<PaymentProviderFailureException> {
            billingService.billSingleInvoice(INVOICE.id)
        }

        verify(inverse = true) { invoiceService.updateInvoiceStatus(any(), any()) }
    }
}