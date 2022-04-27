package io.pleo.antaeus.core.services

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.pleo.antaeus.core.external.PaymentProvider
import io.pleo.antaeus.models.InvoiceStatus
import org.junit.jupiter.api.Test

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

        billingService.billPendingInvoice()
        verify(exactly = 1) { invoiceService.updateInvoiceStatus(any(), InvoiceStatus.PAID) }
    }

    @Test
    fun testFailedBilledInvoice() {
        every { paymentProvider.charge(any()) } returns false

        billingService.billPendingInvoice()
        verify(inverse = true) { invoiceService.updateInvoiceStatus(any(), InvoiceStatus.PAID) }

    }

}