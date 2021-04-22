package com.autostorage.routes

import kotlinx.serialization.Serializable

@Serializable
data class ProductMoveDTO(
    val id: Int,
    var count: Int,
    val provider: Int,
    var note: String = "",
    val name: String? = null,
    val cost: Float? = null,
    val providerId: Int? = null,
    val providerName: String? = null,
    val providerUrl: String? = null,
    val moveId: Int = 0
)
