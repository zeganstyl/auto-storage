package com.autostorage.model

import org.jetbrains.exposed.dao.id.IntIdTable

object Counterparties: IntIdTable() {
    val name = text("name")

    val type = enumeration("type", CounterpartyType::class)

    val email = text("email").default("")

    val phone = text("phone").default("")

    val note = text("note").default("")
}
