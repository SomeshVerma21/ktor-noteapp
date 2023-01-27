package com.example.plugins

import io.ktor.server.auth.*
import io.ktor.util.*
import io.ktor.server.auth.jwt.*
import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.request.*

//fun Application.configureSecurity() {
//
//    authentication {
//            jwt {
//                val jwtAudience = this@configureSecurity.environment.config.property("jwt.audience").getString()
//                realm = this@configureSecurity.environment.config.property("jwt.realm").getString()
//                verifier(
//                    JWT
//                        .require(Algorithm.HMAC256("secret"))
//                        .withAudience(jwtAudience)
//                        .withIssuer(this@configureSecurity.environment.config.property("jwt.domain").getString())
//                        .build()
//                )
//                validate { credential ->
//                    if (credential.payload.audience.contains(jwtAudience)) JWTPrincipal(credential.payload) else null
//                }
//            }
//        }
//
//}
fun Application.configureSecurity() {

    authentication {
            jwt {
                val jwtAudience = "http://0.0.0.0:8080/"
                realm = "Access to 'hello'"
                verifier(
                    JWT
                        .require(Algorithm.HMAC256("secret"))
                        .withAudience(jwtAudience)
                        .withIssuer("http://0.0.0.0:8080/")
                        .build()
                )
                validate { credential ->
                    if (credential.payload.audience.contains("secret")) JWTPrincipal(credential.payload) else null
                }
            }
        }

}

class JwtService{
    val issuer = "note_server"
    val jwtSecret = "vermaji"
    val algorithim = Algorithm.HMAC512(jwtSecret)

    val verifier: JWTVerifier = JWT
        .require(algorithim)
        .withIssuer(issuer)
        .build()

    fun generateToken(uid:Int) : String{
        return JWT.create()
            .withSubject("Note Service aud")
            .withIssuer(issuer)
            .withClaim("uid", uid)
            .sign(algorithim)
    }
}
