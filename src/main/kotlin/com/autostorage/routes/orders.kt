package com.autostorage.routes

import com.autostorage.allList
import com.autostorage.authorize
import com.autostorage.badRequest
import com.autostorage.idParam
import com.autostorage.model.Counterparty
import com.autostorage.model.Order
import com.autostorage.model.OrderType
import com.autostorage.model.ProductMove
import com.autostorage.model.ProductsMoving
import com.autostorage.respondFreeMaker
import io.ktor.application.*
import io.ktor.request.*
import io.ktor.routing.*

fun Route.ordersRoute() {
    route(PATH.orders) {
        get {
            authorize { user ->
                respondFreeMaker(
                    "orders.ftl",
                    mapOf(
                        "user" to user,
                        "orders" to Order.all().map { it },
                        "navLinks" to NavLink.values(),
                        "activeLink" to NavLink.Orders
                    )
                )
            }
        }

        route("${PATH.submit}/{type}") {
            get {
                authorize { user ->
                    val orderType = OrderType.valueOf(call.parameters["type"] ?: badRequest("type is missing"))

                    respondFreeMaker(
                        "editBuyOrder.ftl",
                        mapOf(
                            "user" to user,
                            "order" to null,
                            "navLinks" to NavLink.values(),
                            "activeLink" to NavLink.Orders,
                            "productMoves" to emptyList<ProductMove>()
                        )
                    )
                }
            }

            post {
                authorize { user ->
                    val orderType = OrderType.valueOf(call.parameters["type"] ?: badRequest("type is missing"))

                    val params = call.receiveParameters()

                    params.forEach { s, list ->
                        println("$s: ${list[0]}")
                    }

                    val block: Order.() -> Unit = {
                        when (orderType) {
                            OrderType.Buy -> {
                                params["clientId"]?.toIntOrNull()?.also { counterparty = Counterparty[it] }
                            }
                        }
                    }

//                    val id = params["id"]!!.toInt()
//                    if (id > 0) {
//                        Order.findById(id)?.apply(block)
//                    } else {
//                        Order.new(block)
//                    }
                }
            }
        }

        route(PATH.idParam) {
            get {
                authorize { user ->
                    val order = Order[idParam()]

                    respondFreeMaker(
                        "buyOrder.ftl",
                        mapOf(
                            "user" to user,
                            "order" to order,
                            "navLinks" to NavLink.values(),
                            "activeLink" to null
                        )
                    )
                }
            }
        }
    }
}
