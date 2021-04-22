package com.autostorage.model

enum class OrderStatus(val nameView: String) {
    New("Новая"),
    OnTheWay("В пути на склад"),
    Accepting("Приемка"),
    Ready("Ожидает на складе"),
    Completed("Выполнено"),
    Canceled("Отмена")
}
