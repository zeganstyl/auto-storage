package com.autostorage.model

import com.autostorage.routes.PATH
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class StoredProduct(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<StoredProduct>(StoredProducts)
    var type by ProductType referencedOn StoredProducts.type
    var cell by StoredProducts.cell
    var acceptanceOrder by Order referencedOn StoredProducts.acceptanceOrder
    var note by StoredProducts.note

    val url: String
        get() = "${PATH.storage}/${id.value}"
}
