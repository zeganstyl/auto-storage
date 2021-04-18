package com.autostorage.routes

import kotlinx.serialization.Serializable

@Serializable
data class ProductMoveDTO(
    val id: Int,
    val count: Int,
    val provider: Int,
    val note: String = ""
)
