package com.autostorage.model

enum class UserRole(val nameView: String) {
    Seller("Продавец"),
    Manager("Менеджер"),
    StorageKeeper("Кладовщик"),
    Root("Админ")
}
