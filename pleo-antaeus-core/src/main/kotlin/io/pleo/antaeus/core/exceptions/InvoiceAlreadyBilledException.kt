package io.pleo.antaeus.core.exceptions

class InvoiceAlreadyBilledException(id: Int) : EntityAlreadyBilledException("Invoice", id)
