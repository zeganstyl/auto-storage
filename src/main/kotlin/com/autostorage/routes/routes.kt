package com.autostorage.routes

import com.autostorage.ExpirableLoginSession
import com.autostorage.allList
import com.autostorage.authorize
import com.autostorage.badRequest
import com.autostorage.getOrThrow
import com.autostorage.idParam
import com.autostorage.model.Counterparties
import com.autostorage.model.Counterparty
import com.autostorage.model.CounterpartyType
import com.autostorage.model.Order
import com.autostorage.model.OrderType
import com.autostorage.model.ProductType
import com.autostorage.model.StoredProduct
import com.autostorage.model.getUser
import com.autostorage.respondFreeMaker
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.freemarker.*
import io.ktor.http.content.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.sessions.*
import org.jetbrains.exposed.sql.not
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

enum class NavLink(val viewName: String, val url: String) {
    Orders("Заявки", PATH.orders),
    Storage("Склад", PATH.storage),
    ProductTypes("Типы товаров", PATH.products),
    Counterparties("Контрагенты", PATH.counterparties),
    Stats("Статистика", PATH.stats)
}

val providerTypes = listOf(
    CounterpartyType.ProviderCompany,
    CounterpartyType.ProviderDealer,
    CounterpartyType.ProviderManufacturer
)

fun Application.routes() {
    routing {
        get("/") {
            call.respondRedirect(PATH.orders)
        }

        route(PATH.storage) {
            get {
                authorize { user ->
                    respondFreeMaker(
                        "storage.ftl",
                        mapOf(
                            "user" to user,
                            "storedProducts" to StoredProduct.allList(),
                            "navLinks" to NavLink.values(),
                            "activeLink" to NavLink.Storage
                        )
                    )
                }
            }
        }

        productsRoute()
        ordersRoute()
        counterpartiesRoute()

        route(PATH.stats) {
            get {
                authorize { user ->
                    respondFreeMaker(
                        "stats.ftl",
                        mapOf(
                            "user" to user,
                            "navLinks" to NavLink.values(),
                            "activeLink" to NavLink.Stats
                        )
                    )
                }
            }
        }

        route(PATH.login) {
            get {
                call.respond(
                    FreeMarkerContent(
                        "login.ftl",
                        mapOf(
                            "path" to "/"
                        )
                    )
                )
            }

            post {
                val params = call.receiveParameters()

                val login = params["login"] ?: ""
                if (login.isEmpty()) throw BadRequestException("incorrect login or password")

                val path = params["path"] ?: "/"

                call.sessions.set(ExpirableLoginSession(getUser(login)))

                call.respondRedirect(path)
            }
        }

        get(PATH.logout) {
            call.sessions.clear<ExpirableLoginSession>()
            call.respondRedirect("/login")
        }

        static(PATH.static) {
            resources("static")
        }
    }
}
