package com.autostorage.model

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.jodatime.date
import org.joda.time.DateTime

object StoredProducts: IntIdTable() {
    val type = reference("type", ProductTypes)

    val cell = text("cell").default("")

    val note = text("note").default("")

    val acceptanceOrder = reference("acceptanceOrder", Orders)
}
