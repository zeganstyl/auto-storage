package com.autostorage.model

import org.jetbrains.exposed.dao.id.IntIdTable

object ProductsMoving: IntIdTable() {
    val order = reference("order", Orders)

    val productType = reference("productType", ProductTypes)

    val provider = reference("provider", Counterparties).nullable()

    val count = integer("count")

    // Для каждой позиции нужно указать стоимость отдельно, так как цена на товар может в будущем измениться
    val cost = float("cost").default(0f)

    val note = text("note").default("")
}
