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
import org.jetbrains.exposed.sql.JoinType
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.count
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.sum
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.joda.time.DateTime

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

        get("requested/{providerId}") {
            newSuspendedTransaction {
                val providerId = call.parameters["providerId"]!!.toInt()
                val provider = Counterparty[providerId]
                if (!provider.type.isProvider) badRequest("this is not a provider")

                val products = HashMap<Int, ProductMoveDTO>()
                val orders = HashSet<Int>()

                fun sumProduct(type: ProductType, count: Int): ProductMoveDTO {
                    val id = type.id.value
                    var prod = products[id]
                    if (prod == null) {
                        prod = ProductMoveDTO(
                            id,
                            0,
                            providerId,
                            "",
                            type.name,
                            type.cost,
                            provider.id.value,
                            provider.name,
                            provider.url
                        )
                        products[id] = prod
                    }
                    prod.count += count
                    return prod
                }

                ProductsMoving.innerJoin(Orders).select {
                    (ProductsMoving.provider eq provider.id)
                        .and(Orders.status eq OrderStatus.New)
                        .and(Orders.type eq OrderType.Buy)
                }.forEach {
                    val move = ProductMove.wrapRow(it)

                    val type = move.productType

                    val moveDto = sumProduct(type, move.count)

                    val orderId = move.order.id.value
                    if (!orders.contains(orderId)) {
                        orders.add(orderId)
                        if (moveDto.note.isEmpty()) {
                            moveDto.note = "По заявкам: $orderId"
                        } else {
                            moveDto.note += ", $orderId"
                        }
                    }
                }

                ProductTypes.join(StoredProducts, JoinType.LEFT)
                    .slice(StoredProducts.id.count(), ProductTypes.id, ProductTypes.name, ProductTypes.minRequired, ProductTypes.cost)
                    .select { ProductTypes.provider eq providerId }
                    .groupBy(ProductTypes.id).orderBy(ProductTypes.id).forEach {
                        val type = ProductType.wrapRow(it)

                        val countToBuy = type.minRequired - it[StoredProducts.id.count()].toInt()
                        val moveDto = sumProduct(type, countToBuy)

                        if (moveDto.note.isNotEmpty()) moveDto.note += "; "
                        moveDto.note += "+$countToBuy пополнить запасы"
                    }

                call.respond(products.map { it.value })
            }
        }

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

        route(PATH.storage) {
            get {
                authorize { user ->
                    respondFreeMaker(
                        "storage.ftl",
                        mapOf(
                            "user" to user,
                            "products" to StoredProduct.allList(),
                            "navLinks" to NavLink.values(),
                            "activeLink" to NavLink.Storage
                        )
                    )
                }
            }

            route(PATH.idParam) {
                get {
                    authorize { user ->
                        val prod = StoredProduct.findById(call.parameters["id"]?.toIntOrNull() ?: throw NotFoundException("")) ?: throw NotFoundException("")

                        respondFreeMaker(
                            "editStoredProduct.ftl",
                            mapOf(
                                "user" to user,
                                "product" to prod,
                                "navLinks" to NavLink.values(),
                                "activeLink" to NavLink.Storage
                            )
                        )
                    }
                }

                post {
                    authorize { user ->
                        val params = call.parameters

                        val prod = StoredProduct.findById(params["id"]?.toIntOrNull() ?: throw NotFoundException("")) ?: throw NotFoundException("")
                        params["cell"]?.also { prod.cell = it }
                        params["note"]?.also { prod.note = it }

                        call.respondRedirect(PATH.storage)
                    }
                }
            }
        }

        productsRoute()
        ordersRoute()
        counterpartiesRoute()
        statsRoute()

        loginRoute()

        static(PATH.static) {
            resources("static")
        }
    }
}
