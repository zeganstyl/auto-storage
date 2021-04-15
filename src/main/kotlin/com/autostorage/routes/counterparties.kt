package com.autostorage.routes

import com.autostorage.allList
import com.autostorage.authorize
import com.autostorage.getOrThrow
import com.autostorage.idParam
import com.autostorage.model.Counterparties
import com.autostorage.model.Counterparty
import com.autostorage.model.CounterpartyType
import com.autostorage.respondFreeMaker
import io.ktor.application.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.SqlExpressionBuilder.neq

fun Route.counterpartiesRoute() {
    route(PATH.counterparties) {
        get {
            authorize { user ->
                respondFreeMaker(
                    "counterparties.ftl",
                    mapOf(
                        "user" to user,
                        "counterparties" to Counterparty.allList(),
                        "navLinks" to NavLink.values(),
                        "activeLink" to NavLink.Counterparties
                    )
                )
            }
        }

        route(PATH.idParam) {
            get {
                authorize { user ->
                    val counterparty = Counterparty.getOrThrow(idParam())

                    respondFreeMaker(
                        "counterparty.ftl",
                        mapOf(
                            "user" to user,
                            "counterparty" to counterparty,
                            "navLinks" to NavLink.values(),
                            "activeLink" to NavLink.Counterparties,
                            "counterpartyTypes" to CounterpartyType.values()
                        )
                    )
                }
            }
        }

        route(PATH.submit) {
            get {
                authorize { user ->
                    respondFreeMaker(
                        "counterparty.ftl",
                        mapOf(
                            "user" to user,
                            "counterparty" to null,
                            "navLinks" to NavLink.values(),
                            "activeLink" to NavLink.Counterparties,
                            "counterpartyTypes" to CounterpartyType.values()
                        )
                    )
                }
            }

            post {
                authorize {
                    val params = call.receiveParameters()

                    val block: Counterparty.() -> Unit = {
                        name = params["name"]!!
                        email = params["email"]!!
                        phone = params["phone"]!!
                        note = params["note"]!!
                        type = CounterpartyType.valueOf(params["type"]!!)
                    }

                    val id = params["id"]!!.toInt()
                    if (id > 0) {
                        Counterparty[id].block()
                    } else {
                        Counterparty.new(block)
                    }

                    call.respondRedirect(PATH.counterparties)
                }
            }
        }

        get("type/{itemType}") {
            authorize {
                val itemType = ChooseItemType.valueOf(call.parameters["itemType"]!!)

                val items = Counterparty.find {
                    when (itemType) {
                        ChooseItemType.Clients -> Counterparties.type eq CounterpartyType.Client
                        ChooseItemType.Providers -> Counterparties.type neq CounterpartyType.Client
                    }
                }.orderBy(Pair(Counterparties.id, SortOrder.ASC)).toList()

                respondFreeMaker(
                    "counterpartiesList.ftl",
                    mapOf(
                        "counterparties" to items
                    )
                )
            }
        }
    }
}
