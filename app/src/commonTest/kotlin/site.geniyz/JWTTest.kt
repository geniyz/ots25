package site.geniyz.ots

import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.testApplication
import kotlinx.serialization.encodeToString
import site.geniyz.ots.jwt.UserInfo
import site.geniyz.ots.jwt.authModule
import site.geniyz.ots.rim.json
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class `JWT` {

    @Test
    fun `тестирование логина админа`() = testApplication {
        application { authModule() }

        val response = client.post("/auth"){
            setBody(json.encodeToString(UserInfo(nickname = "root", password = "toor")))
        }
        assertEquals(HttpStatusCode.OK, response.status)

        val acc = response.bodyAsText()

        assertContains(acc, "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.")

        val user = UserInfo.fromJWT(acc)

        assertEquals("root", user.nickname)
        assertContains(user.groups, "gamer")
        assertContains(user.groups, "admin")
    }


    @Test
    fun `ошибка при неправильном пароле`() = testApplication {
        application { authModule() }

        val response = client.post("/auth"){
            setBody(json.encodeToString(UserInfo(nickname = "root", password = "йцук")))
        }
        assertEquals(HttpStatusCode.BadRequest, response.status)

        val acc = response.bodyAsText()

        assertEquals(acc, "")
    }


}
