package com.autostorage.model

import org.jetbrains.exposed.dao.id.IntIdTable

object ProductTypes: IntIdTable() {
    val name = text("name")

    val model = text("model").default("")

    val carModel = text("carModel").default("")

    val provider = reference("provider", Counterparties).nullable()

    val cost = float("cost").default(0f)

    val minRequired = integer("minRequired").default(0)

    val note = text("note").default("")
}
