package com.autostorage.model

import com.autostorage.routes.PATH
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class ProductType(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<ProductType>(ProductTypes)
    var carModel by ProductTypes.carModel
    var minRequired by ProductTypes.minRequired
    var model by ProductTypes.model
    var name by ProductTypes.name
    var note by ProductTypes.note
    var cost by ProductTypes.cost
    var provider by Counterparty optionalReferencedOn ProductTypes.provider

    val url: String
        get() = "${PATH.products}/${id.value}"
}
