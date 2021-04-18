package com.autostorage.routes

import com.autostorage.authorize
import com.autostorage.badRequest
import com.autostorage.jsonConfig
import com.autostorage.model.Counterparty
import com.autostorage.model.Order
import com.autostorage.model.OrderStatus
import com.autostorage.model.OrderType
import com.autostorage.model.PaymentMethod
import com.autostorage.model.ProductMove
import com.autostorage.model.ProductType
import com.autostorage.respondFreeMaker
import io.ktor.application.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import kotlinx.serialization.decodeFromString
import org.joda.time.DateTime

fun Route.orderGet() {
    get {
        authorize { user ->
            val id = call.parameters["id"]?.toIntOrNull()
            val order = if (id != null) Order[id] else null

            val orderType = order?.type ?: OrderType.valueOf(call.parameters["type"] ?: badRequest("type is missing"))

            respondFreeMaker(
                "editOrder.ftl",
                mapOf(
                    "user" to user,
                    "order" to order,
                    "navLinks" to NavLink.values(),
                    "activeLink" to NavLink.Orders,
                    "productMoves" to (order?.productMoves?.toList() ?: emptyList()),
                    "orderType" to orderType,
                    "orderStatuses" to OrderStatus.values(),
                    "isSupply" to (orderType == OrderType.Supply)
                )
            )
        }
    }
}

fun Route.orderPost() {
    post {
        authorize {
            val params = call.receiveParameters()

            val block: Order.() -> Unit = {
                this.counterparty = Counterparty[params["counterparty"]!!.toInt()]
                this.note = params["note"]!!
                params["paymentMethod"]?.also { this.paymentMethod = PaymentMethod.valueOf(it) }
            }

            val id = call.parameters["id"]?.toIntOrNull() ?: 0
            var isNew = false
            val order = if (id > 0) {
                val order = Order[id]
                val oldStatus = order.status
                order.apply(block)
                val newStatus = order.status
                if (oldStatus != newStatus) {
                    if (newStatus == OrderStatus.Completed || newStatus == OrderStatus.Canceled) {
                        order.completionTime = DateTime.now()
                    }
                }
                order
            } else {
                isNew = true
                val orderType = OrderType.valueOf(call.parameters["type"] ?: badRequest("type is missing"))
                Order.new {
                    this.type = orderType
                    block()
                }
            }

            jsonConfig.decodeFromString<List<ProductMoveDTO>>(params["products"]!!).forEach { dto ->
                ProductMove.new {
                    this.order = order
                    this.count = dto.count
                    this.productType = ProductType[dto.id].also {
                        if (isNew) this.cost = it.cost
                    }
                    this.provider = Counterparty.findById(dto.provider)
                    this.note = dto.note
                }
            }

            call.respondRedirect(PATH.orders)
        }
    }
}

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
                        "activeLink" to NavLink.Orders,
                        "orderTypes" to OrderType.values()
                    )
                )
            }
        }

        route(PATH.idParam) {
            orderGet()
            orderPost()
        }

        route("${PATH.submit}/{type}") {
            orderGet()
            orderPost()
        }
    }
}
