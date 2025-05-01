package site.geniyz.ots.jwt

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.request.*
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.*
import site.geniyz.ots.rim.json

fun Application.authModule() {
    configureCORS()
    routing {
        auth()
    }
}

fun Route.auth(){

    val userlist = listOf(
        UserInfo(sub = "0", nickname = "root", password = "toor", groups = listOf("admin", "gamer")),
        UserInfo(sub = "1", nickname = "BOBA", password = "abcd", groups = listOf("gamer")),
        UserInfo(sub = "2", nickname = "aton", password = "efgh", groups = listOf("gamer")),
        UserInfo(sub = "3", nickname = "bany", password = "empt", groups = listOf("banned", "gamer")),
    )

    post("/auth"){
        val req: UserInfo = json.decodeFromString( call.receiveText() )

        val user = userlist.firstOrNull() {
            it.nickname == req.nickname && it.password == req.password
        }

        if(user == null){
            call.respond(HttpStatusCode.BadRequest)
        }else {
            call.respondText(user.makeJWT())
        }
    }
}
