package ttp.com.routes

import io.ktor.http.*
import io.ktor.resources.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ttp.com.authentication.JwtService
import ttp.com.data.model.RegisterRequest
import ttp.com.data.model.SimpleResponse
import ttp.com.repository.repo

const val API_VERSION = "/v1"
const val USERS = "$API_VERSION/users"
const val REGISTER_REQUEST = "$USERS/register"
const val LOGIN_REQUEST = "$USERS/login"

@Resource(REGISTER_REQUEST)
class UserRegisterRoute

@Resource(LOGIN_REQUEST)
class UserLoginRoute

fun Route.UserRoutes(
    db: repo,
    jwtService: JwtService,
    hashFun: (String) -> String
) {
    post<UserRegisterRoute> {
        val registerRequest = try {
            call.receive<RegisterRequest>()
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, SimpleResponse(false, "Missing"))
            return@post
        }
    }

    post<UserLoginRoute> {

    }
}