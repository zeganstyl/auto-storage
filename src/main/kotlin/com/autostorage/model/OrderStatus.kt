package com.autostorage.model

enum class OrderStatus(val nameView: String) {
    New("Новая"),
    OnTheWay("В пути"),
    Ready("Ожидает"),
    Completed("Выполнено"),
    Canceled("Отмена")
}
