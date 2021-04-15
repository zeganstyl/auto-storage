package com.autostorage.model

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.jodatime.datetime
import org.joda.time.DateTime

object Orders: IntIdTable() {
    val type = enumeration("type", OrderType::class)

    val status = enumeration("status", OrderStatus::class)

    val createTime = datetime("createTime").default(DateTime.now())

    val completionTime = datetime("completionTime").nullable()

    val note = text("note").default("")

    val counterparty = reference("counterparty", Counterparties)
}
