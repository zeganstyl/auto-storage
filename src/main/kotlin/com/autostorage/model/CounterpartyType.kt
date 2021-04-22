package com.autostorage.model

enum class CounterpartyType(val nameView: String, val isProvider: Boolean) {
    Client("Клиент", false),
    ProviderCompany("Компания-поставщик", true),
    ProviderManufacturer("Производитель-поставщик", true),
    ProviderDealer("Дилер-поставщик", true)
}
