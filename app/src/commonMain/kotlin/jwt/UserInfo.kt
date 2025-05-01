package site.geniyz.ots.jwt

import com.auth0.jwt.JWT
import kotlinx.datetime.Clock
import kotlinx.datetime.toJavaInstant
import kotlinx.serialization.Serializable
import site.geniyz.ots.rim.json
import kotlin.time.Duration.Companion.minutes

@Serializable
data class UserInfo(
    val sub: String = "",
    val nickname: String = "",
    val password:  String = "",
    val groups: List<String> = emptyList(),
){

    fun makeJWT(): String {

        var draft = JWT.create()
            .withSubject( sub )
            .withIssuedAt(  Clock.System.now().toJavaInstant() )
            .withNotBefore( Clock.System.now().toJavaInstant() )
            .withExpiresAt( (Clock.System.now() + 15.minutes ).toJavaInstant() )
            .withIssuer( "test2" )

        draft = draft
            .withClaim("nickname", nickname)

        // ГРУППЫ | полномочия:
        draft = draft.withClaim("groups", groups )

        return draft.sign( getAlgo() )
    }


    companion object {
        val NONE = UserInfo()
        fun fromString(str: String): UserInfo = json.decodeFromString(str)

        fun fromJWT(
            accessToken: String,
        ): UserInfo {
            val verif = JWT.require( getAlgo() )

            val payload = verif
                .build()
                .verify(accessToken)
                .payload

            return try {
                fromString( payload.fromBase64() )
            }catch (e: Throwable){
                NONE
            }
        }

    }
}