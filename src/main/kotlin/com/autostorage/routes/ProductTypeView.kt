package com.autostorage.routes

import com.autostorage.model.ProductType

data class ProductTypeView(
    var carModel: String = "",
    var maxRequired: Int = 0,
    var minRequired: Int = 0,
    var model: String = "",
    var name: String = "",
    var note: String = "",
    var providerName: String = "",
    var providerId: Int = 0
) {
    constructor(productType: ProductType): this(
        productType.carModel,
        productType.maxRequired,
        productType.minRequired,
        productType.model,
        productType.name,
        productType.note,
        productType.provider?.name ?: "",
        productType.provider?.id?.value ?: 0
    )
}
