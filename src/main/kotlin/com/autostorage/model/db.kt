package com.autostorage.model

import com.autostorage.WORD
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

val root = User(WORD.root, UserRole.Root)
val seller = User(WORD.seller, UserRole.Seller)
val manager = User(WORD.manager, UserRole.Manager)
val storageKeeper = User(WORD.storageKeeper, UserRole.StorageKeeper)

fun getUser(role: UserRole): User = when (role) {
    UserRole.Root -> root
    UserRole.Seller -> seller
    UserRole.Manager -> manager
    UserRole.StorageKeeper -> storageKeeper
}

fun getUser(role: String) = getUser(UserRole.valueOf(role))

fun initDB() {
    val config = HikariConfig().apply {
        val envDbUrl = System.getenv("DATABASE_URL")
        if (envDbUrl != null) {
            username = "postgres"
            password = "postgres"
            jdbcUrl = "jdbc:postgresql://localhost:5432/auto-storage"
            driverClassName = "org.postgresql.Driver"
        } else {
            driverClassName = "org.h2.Driver"
            jdbcUrl = "jdbc:h2:file:./ru.urfu.idea.db-test"
        }

//        username = "postgres"
//        password = "postgres"
//        jdbcUrl = "jdbc:postgresql://localhost:5432/auto-storage"
//        driverClassName = "org.postgresql.Driver"

        maximumPoolSize = 3
        isAutoCommit = false
        transactionIsolation = "TRANSACTION_REPEATABLE_READ"
        validate()
    }

    Database.connect(HikariDataSource(config))

    transaction {
        SchemaUtils.create(
            Orders,
            ProductsMoving,
            ProductTypes,
            Counterparties,
            StoredProducts
        )
    }
}
