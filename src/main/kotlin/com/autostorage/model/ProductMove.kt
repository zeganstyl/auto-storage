package com.autostorage.model

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class ProductMove(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<ProductMove>(ProductsMoving)
    var count by ProductsMoving.count
    var order by Order referencedOn ProductsMoving.order
    var productType by ProductType referencedOn ProductsMoving.productType
    var provider by Counterparty referencedOn ProductsMoving.provider
}
