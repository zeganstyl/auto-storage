package com.autostorage.routes

import com.autostorage.*
import com.autostorage.model.*
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.freemarker.*
import io.ktor.http.content.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.sessions.*
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.neq

enum class NavLink(val viewName: String, val url: String) {
    Orders("Заявки", PATH.orders),
    Storage("Склад", PATH.storage),
    ProductTypes("Типы товаров", PATH.products),
    Counterparties("Контрагенты", PATH.counterparties),
    Stats("Статистика", PATH.stats)
}

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

        get("list/{itemType}") {
            authorize {
                when (ChooseItemType.valueOf(call.parameters["itemType"]!!)) {
                    ChooseItemType.Clients -> respondFreeMaker("counterpartiesList.ftl",
                        mapOf("items" to Counterparty.findOrderedList { Counterparties.type eq CounterpartyType.Client })
                    )
                    ChooseItemType.Providers -> respondFreeMaker("counterpartiesList.ftl",
                        mapOf("items" to Counterparty.findOrderedList { Counterparties.type neq CounterpartyType.Client })
                    )
                    ChooseItemType.Products -> respondFreeMaker("productsList.ftl",
                        mapOf("items" to ProductType.allList())
                    )
                }
            }
        }

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
