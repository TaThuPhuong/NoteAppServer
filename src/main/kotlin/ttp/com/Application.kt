package ttp.com

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.resources.*
import io.ktor.server.tomcat.*
import ttp.com.authentication.hashFun
import ttp.com.plugins.configureRouting
import ttp.com.plugins.configureSecurity
import ttp.com.plugins.configureSerialization
import ttp.com.repository.DatabaseFactory
import ttp.com.repository.repo

fun main() {
    embeddedServer(
        Tomcat,
        port = 8081,
        host = "0.0.0.0",
        watchPaths = listOf("classes"),
        module = Application::module
    ).start(wait = true)
}

fun Application.module() {
    install(Resources)
    DatabaseFactory.init()
    val db = repo()
    val hashFun = { s: String -> hashFun(s) }

    configureSecurity()
    configureSerialization()
    configureRouting()
}
