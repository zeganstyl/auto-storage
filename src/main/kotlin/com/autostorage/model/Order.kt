package com.autostorage.model

import com.autostorage.routes.PATH
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class Order(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Order>(Orders)
    var type by Orders.type
    var createTime by Orders.createTime
    var completionTime by Orders.completionTime
    var note by Orders.note
    var status by Orders.status

    var counterparty by Counterparty referencedOn Orders.counterparty

    val url: String
        get() = "${PATH.orders}/${id.value}"

    val createTimeView
        get() = createTime.toString("YYYY-MM-dd")
}
