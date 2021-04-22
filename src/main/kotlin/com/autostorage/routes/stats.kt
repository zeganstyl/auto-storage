package com.autostorage.routes

import com.autostorage.authorize
import com.autostorage.model.OrderStatus
import com.autostorage.model.OrderType
import com.autostorage.model.Orders
import com.autostorage.model.ProductType
import com.autostorage.model.ProductTypes
import com.autostorage.model.ProductsMoving
import com.autostorage.respondFreeMaker
import io.ktor.application.*
import io.ktor.routing.*
import org.jetbrains.exposed.sql.JoinType
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.sum
import org.joda.time.DateTime
import org.joda.time.DateTimeZone

fun Route.statsRoute() {
    route(PATH.stats) {
        get {
            authorize { user ->
                val params = call.parameters

                val toTime = DateTime(params["to"]?.toLongOrNull() ?: DateTime(DateTimeZone.UTC).millis)
                val fromTime = DateTime(params["from"]?.toLongOrNull() ?: toTime.minus(2592000000).millis) // minus 30 days

                val stats = LinkedHashMap<Int, ProductStat>()

                ProductTypes.join(
                    ProductsMoving,
                    JoinType.LEFT,
                    onColumn = ProductTypes.id,
                    otherColumn = ProductsMoving.productType
                ).join(
                    Orders,
                    JoinType.INNER,
                    onColumn = ProductsMoving.order,
                    otherColumn = Orders.id,
                    additionalConstraint = {
                        (Orders.status eq OrderStatus.Completed)
                            .and(Orders.type eq OrderType.Buy)
                            .and(Orders.completionTime lessEq toTime)
                            .and(Orders.completionTime greaterEq fromTime)
                    }
                ).slice(
                    ProductTypes.id, ProductTypes.name, ProductsMoving.count.sum()
                ).selectAll().groupBy(ProductTypes.id).orderBy(ProductTypes.id).forEach {
                    val type = ProductType.wrapRow(it)
                    stats[type.id.value] = ProductStat(type, it[ProductsMoving.count.sum()] ?: 0, 0, 0)
                }

                ProductTypes.join(
                    ProductsMoving,
                    JoinType.LEFT,
                    onColumn = ProductTypes.id,
                    otherColumn = ProductsMoving.productType
                ).join(
                    Orders,
                    JoinType.INNER,
                    onColumn = ProductsMoving.order,
                    otherColumn = Orders.id,
                    additionalConstraint = {
                        (Orders.status eq OrderStatus.Completed)
                            .and(Orders.type eq OrderType.Supply)
                            .and(Orders.completionTime lessEq toTime)
                            .and(Orders.completionTime greaterEq fromTime)
                    }
                ).slice(
                    ProductTypes.id, ProductTypes.name, ProductsMoving.count.sum()
                ).selectAll().groupBy(ProductTypes.id).orderBy(ProductTypes.id).forEach { resultRow ->
                    val type = ProductType.wrapRow(resultRow)
                    val prod = stats[type.id.value] ?: ProductStat(type, 0, 0, 0).also { stats[type.id.value] = it }
                    prod.purchased = resultRow[ProductsMoving.count.sum()]!!
                    prod.percents = ((prod.sold.toFloat() / prod.purchased) * 100f).toInt()
                }

                respondFreeMaker(
                    "stats.ftl",
                    mapOf(
                        "user" to user,
                        "navLinks" to NavLink.values(),
                        "activeLink" to NavLink.Stats,
                        "stats" to stats.map { it.value },
                        "toTime" to toTime.toString("yyyy-MM-dd'T'HH:mm"),
                        "fromTime" to fromTime.toString("yyyy-MM-dd'T'HH:mm")
                    )
                )
            }
        }
    }
}
