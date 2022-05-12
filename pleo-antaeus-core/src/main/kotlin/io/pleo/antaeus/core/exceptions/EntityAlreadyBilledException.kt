package io.pleo.antaeus.core.exceptions

abstract class EntityAlreadyBilledException(entity: String, id: Int) : Exception("$entity '$id' was already billed")
