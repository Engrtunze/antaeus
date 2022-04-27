package io.pleo.antaeus.core.services

import io.pleo.antaeus.models.Currency
import io.pleo.antaeus.models.Invoice
import io.pleo.antaeus.models.InvoiceStatus
import io.pleo.antaeus.models.Money
import java.math.BigDecimal
import kotlin.random.Random


internal fun creatInvoice(invoiceStatus: InvoiceStatus): Invoice {
    return Invoice(id = Random.nextInt(), customerId = Random.nextInt(), status = invoiceStatus, amount = createMoney())
}

internal fun createMoney(): Money {
    return Money(value = BigDecimal(Random.nextDouble(10.0, 200.0)), currency = Currency.values()[Random.nextInt(4)])
}