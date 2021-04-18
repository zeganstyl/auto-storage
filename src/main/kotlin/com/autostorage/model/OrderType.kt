package com.autostorage.model

enum class OrderType(
    val nameView: String,
    val counterpartyName: String,
    val new: String,
    val showPayment: Boolean,
    val chooseClass: String
) {
    Buy("Покупка", "Клиент", "Новый клиент", true, "chooseClient"),
    Supply("Заказ у поставщика", "Поставщик", "Новый поставщик", false, "chooseProvider"),
    Return("Возврат", "Клиент", "", false, "chooseClient"),
    ReturnToProvider("Возврат поставщику", "Поставщик", "", false, "chooseProvider")
}
