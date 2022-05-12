package io.pleo.antaeus.core.exceptions

class PaymentProviderFailureException(id: Int) :  Exception("payment failed for invoice: '$id' try again")
