package com.autostorage.model

import com.autostorage.routes.PATH
import io.ktor.application.*
import io.ktor.request.*
import io.ktor.response.*
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.SortOrder

class Counterparty(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Counterparty>(Counterparties)

    var name by Counterparties.name
    var email by Counterparties.email
    var phone by Counterparties.phone
    var type by Counterparties.type
    var note by Counterparties.note

    val url: String
        get() = "${PATH.counterparties}/${id.value}"
}
