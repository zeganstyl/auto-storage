package com.autostorage.model

import org.jetbrains.exposed.dao.id.IntIdTable

object ProductGrouping: IntIdTable() {
    val productType = reference("productType", ProductTypes)

    val group = reference("group", ProductGroups)
}
