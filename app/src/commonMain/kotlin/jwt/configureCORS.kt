package site.geniyz.ots.jwt

import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.cors.routing.CORS
import kotlin.io.encoding.ExperimentalEncodingApi
import kotlin.uuid.ExperimentalUuidApi


@OptIn(ExperimentalEncodingApi::class, ExperimentalUuidApi::class)
fun Application.configureCORS() {
    install(CORS) {
        allowSameOrigin = true
        allowCredentials = true
        allowNonSimpleContentTypes = true

        allowMethod(HttpMethod.Options)
        allowMethod(HttpMethod.Get)
        allowMethod(HttpMethod.Post)
        allowMethod(HttpMethod.Put)
        allowMethod(HttpMethod.Delete)

        allowHeader(HttpHeaders.ContentType)
        allowHeader(HttpHeaders.Authorization)
        allowHeader(HttpHeaders.AccessControlAllowOrigin)

        allowHeader("*")
        anyHost()
    }
}
