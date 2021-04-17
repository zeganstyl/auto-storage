package com.autostorage.model

import org.jetbrains.exposed.dao.id.IntIdTable

object ProductsMoving: IntIdTable() {
    val order = reference("order", Orders)

    val productType = reference("productType", ProductTypes)

    val provider = reference("provider", Counterparties).nullable()

    val count = integer("count")
}
