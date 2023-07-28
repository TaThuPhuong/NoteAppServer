package ttp.com.plugins

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ttp.com.authentication.JwtService
import ttp.com.authentication.hashFun
import ttp.com.data.model.User

/**
 * [jwtService] :
 *
 * */
fun Application.configureRouting() {
    val jwtService = JwtService()
    routing {
        get("/") {
            call.respondText("hhhh/")
        }

        get("/token") {
            val email = call.request.queryParameters["email"]
            val pass = call.request.queryParameters["pass"]
            val name = call.request.queryParameters["name"]

            val user = User(email!!, hashFun(pass!!), name!!)
            call.respond(jwtService.generateToken(user))
        }

        route("/notes") {
            route("/create") {
                post {
                    val body = call.receive<String>()
                    call.respond(body)
                }
            }

            delete {
                val body = call.receive<String>()
                call.respond(body)
            }
        }
    }
}
