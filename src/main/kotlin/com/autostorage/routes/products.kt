package com.autostorage.routes

import com.autostorage.allList
import com.autostorage.authorize
import com.autostorage.getOrThrow
import com.autostorage.idParam
import com.autostorage.model.Counterparty
import com.autostorage.model.ProductType
import com.autostorage.respondFreeMaker
import io.ktor.application.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.productsRoute() {
    route(PATH.products) {
        get {
            authorize { user ->
                respondFreeMaker(
                    "products.ftl",
                    mapOf(
                        "user" to user,
                        "products" to ProductType.allList(),
                        "navLinks" to NavLink.values(),
                        "activeLink" to NavLink.ProductTypes
                    )
                )
            }
        }

        route(PATH.idParam) {
            get {
                authorize { user ->
                    val product = ProductType.getOrThrow(idParam())

                    respondFreeMaker(
                        "editProduct.ftl",
                        mapOf(
                            "user" to user,
                            "product" to product,
                            "navLinks" to NavLink.values(),
                            "activeLink" to NavLink.ProductTypes
                        )
                    )
                }
            }
        }

        route(PATH.submit) {
            get {
                authorize { user ->
                    respondFreeMaker(
                        "editProduct.ftl",
                        mapOf(
                            "user" to user,
                            "product" to null,
                            "navLinks" to NavLink.values(),
                            "activeLink" to NavLink.ProductTypes
                        )
                    )
                }
            }

            post {
                authorize {
                    val params = call.receiveParameters()

                    val block: ProductType.() -> Unit = {
                        params["name"]?.also { name = it }
                        params["carModel"]?.also { carModel = it }
                        params["maxRequired"]?.toIntOrNull()?.also { maxRequired = it }
                        params["minRequired"]?.toIntOrNull()?.also { minRequired = it }
                        params["model"]?.also { model = it }
                        params["providerId"]?.toIntOrNull()?.also { Counterparty.findById(it).also { provider = it } }
                        params["note"]?.also { note = it }
                        params["cost"]?.toFloatOrNull()?.also { cost = it }
                    }

                    val id = params["id"]!!.toInt()
                    if (id > 0) {
                        ProductType[id].block()
                    } else {
                        ProductType.new(block)
                    }

                    call.respondRedirect(PATH.products)
                }
            }
        }
    }
}
