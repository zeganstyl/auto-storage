package com.autostorage.routes

import com.autostorage.ExpirableLoginSession
import com.autostorage.model.UserRole
import com.autostorage.model.getUser
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.freemarker.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.sessions.*

fun Route.loginRoute() {
    route(PATH.login) {
        get {
            call.respond(
                FreeMarkerContent(
                    "login.ftl",
                    mapOf(
                        "path" to "/",
                        "roles" to UserRole.values()
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
}
