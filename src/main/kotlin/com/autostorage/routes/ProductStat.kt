package com.autostorage.routes

import com.autostorage.model.ProductType

class ProductStat(
    val type: ProductType,
    var sold: Int,
    var purchased: Int,
    var percents: Int
)
