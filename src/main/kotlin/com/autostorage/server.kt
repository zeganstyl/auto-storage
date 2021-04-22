package com.autostorage

import com.autostorage.model.User
import com.autostorage.model.UserRole
import com.autostorage.model.initDB
import com.autostorage.routes.routes
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.features.*
import io.ktor.freemarker.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.serialization.*
import io.ktor.server.netty.*
import io.ktor.sessions.*
import io.ktor.util.*
import io.ktor.util.pipeline.*
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.SqlExpressionBuilder
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.util.*

fun main(args: Array<String>): Unit = EngineMain.main(args)

val jsonConfig = Json {
    encodeDefaults = true
    isLenient = true
    allowSpecialFloatingPointValues = true
    allowStructuredMapKeys = true
    prettyPrint = false
    useArrayPolymorphism = true
    ignoreUnknownKeys = true
}

fun Application.module(testing: Boolean = false) {
    initDB()

    install(FreeMarker) {
        setClassForTemplateLoading(this::class.java, "/templates/")
        this.defaultEncoding = "UTF-8"
        this.locale = Locale("ru_RU")
        numberFormat = "computer"
    }

    install(ContentNegotiation) {
        json(jsonConfig)
    }

    install(Sessions) {
        // REMEMBER! Change this string and store them safely
        val key = hex(System.getenv("SESSION_SECRET") ?: "03515606058610610561058")

        cookie<ExpirableLoginSession>("SESSION") {
            transform(SessionTransportTransformerMessageAuthentication(key))
        }
    }

    install(Authentication) {
        session<ExpirableLoginSession>("session") {
            validate { it.user }
        }
    }

    routes()
}

fun ApplicationCall.user(): User = sessions.get<ExpirableLoginSession>()!!.user

suspend fun PipelineContext<Unit, ApplicationCall>.authorize(block: suspend (user: User) -> Unit) =
    call.authorize(block)

suspend fun PipelineContext<Unit, ApplicationCall>.respondFreeMaker(
    template: String,
    model: Any?,
    etag: String? = null,
    contentType: ContentType = ContentType.Text.Html.withCharset(Charsets.UTF_8)
) {
    call.respond(FreeMarkerContent(template, model, etag, contentType))
}

fun <T: Entity<Int>> EntityClass<Int, T>.getOrThrow(id: Int, message: String? = "Not found") =
    findById(id) ?: throw NotFoundException(message)

fun PipelineContext<Unit, ApplicationCall>.idParam(): Int = call.parameters.getOrFail<Int>("id")

suspend fun respondLogin(call: ApplicationCall, path: String? = null) {
    call.respond(
        FreeMarkerContent(
            "login.ftl",
            mapOf(
                "path" to (path ?: call.request.path()),
                "roles" to UserRole.values()
            )
        )
    )
}

suspend fun ApplicationCall.authorize(block: suspend (user: User) -> Unit) {
    val user = sessions.get<ExpirableLoginSession>()?.user

    if (user == null) {
        respondLogin(this)
    } else {
        newSuspendedTransaction {
            block(user)
        }
    }
}

fun badRequest(message: String): Nothing = throw BadRequestException(message)

fun <T: IntEntity> IntEntityClass<T>.allList() = all().orderBy(Pair(table.id, SortOrder.ASC)).toList()

fun <T: IntEntity> IntEntityClass<T>.findOrderedList(op: SqlExpressionBuilder.() -> Op<Boolean>) =
    find(op).orderBy(Pair(table.id, SortOrder.ASC)).toList()
