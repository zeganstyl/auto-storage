package com.autostorage.model

enum class CounterpartyType(val nameView: String) {
    Client("Клиент"),
    ProviderCompany("Компания-поставщик"),
    ProviderManufacturer("Производитель-поставщик"),
    ProviderDealer("Дилер-поставщик")
}
