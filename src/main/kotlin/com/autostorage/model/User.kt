package com.autostorage.model

import io.ktor.auth.*

class User(
    val name: String,
    val role: UserRole
) : Principal {
    val roleView
        get() = when (role) {
            UserRole.Root -> "Админ"
            UserRole.Seller -> "Продавец"
            UserRole.Manager -> "Менеджер"
            UserRole.StorageKeeper -> "Кладовщик"
        }
}
