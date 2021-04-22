package com.autostorage.routes

import com.autostorage.allList
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
import com.autostorage.model.StoredProduct
import com.autostorage.model.StoredProducts
import com.autostorage.respondFreeMaker
import io.ktor.application.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import kotlinx.serialization.decodeFromString
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.selectAll
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.joda.time.Instant

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
                    "isSupply" to (orderType == OrderType.Supply),
                    "productTypes" to ProductType.allList()
                )
            )
        }
    }
}

fun Route.orderPost() {
    post {
        authorize {
            val orderId = call.parameters["id"]?.toIntOrNull() ?: 0

            val params = call.receiveParameters()

            val block: Order.() -> Unit = {
                this.counterparty = Counterparty[params["counterparty"]!!.toInt()]
                this.note = params["note"]!!
                params["paymentMethod"]?.also { this.paymentMethod = PaymentMethod.valueOf(it) }
            }

            var isNew = false
            val order = if (orderId > 0) {
                val order = Order[orderId]
                val oldStatus = order.status

                if (oldStatus == OrderStatus.Completed || oldStatus == OrderStatus.Canceled) return@authorize

                order.apply(block)
                order.status = OrderStatus.valueOf(params["status"]!!)

                val newStatus = order.status

                if (oldStatus != newStatus) {
                    if (newStatus == OrderStatus.Completed || newStatus == OrderStatus.Canceled) {
                        order.completionTime = DateTime(DateTimeZone.UTC)
                    }

                    if (newStatus == OrderStatus.Completed) {
                        when (order.type) {
                            OrderType.Supply, OrderType.Return -> {
                                order.productMoves.forEach {
                                    val count = it.count
                                    for (i in 0 until count) {
                                        StoredProduct.new {
                                            this.acceptanceOrder = order
                                            this.type = it.productType
                                        }
                                    }
                                }
                            }
                            OrderType.Buy, OrderType.ReturnToProvider -> {
                                order.productMoves.forEach {
                                    StoredProducts.deleteWhere(limit = it.count) {
                                        StoredProducts.type eq it.productType.id
                                    }
                                }
                            }
                        }
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
                val moveBlock: ProductMove.() -> Unit = {
                    this.count = dto.count
                    this.productType = ProductType[dto.id].also {
                        if (isNew) this.cost = it.cost
                    }
                    this.provider = Counterparty.findById(dto.provider)
                    this.note = dto.note
                }
                if (dto.moveId > 0) {
                    moveBlock(ProductMove[dto.moveId])
                } else {
                    ProductMove.new {
                        this.order = order
                        moveBlock()
                    }
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
