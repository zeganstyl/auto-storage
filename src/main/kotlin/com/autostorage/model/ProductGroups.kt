package com.autostorage.model

import org.jetbrains.exposed.dao.id.IntIdTable

object ProductGroups: IntIdTable() {
    val name = text("name")

    val description = text("description")
}
