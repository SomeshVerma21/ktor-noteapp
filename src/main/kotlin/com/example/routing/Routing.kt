package com.example.routing

import com.example.db.NotesDao
import com.example.db.NotesDaoImpl
import com.example.db.UserDao
import com.example.db.UserDaoImpl
import com.example.models.Note
import com.example.models.Notes
import com.example.models.User
import com.example.models.Users
import com.example.utils.Response
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.internal.decodeStringToJsonTree
import org.jetbrains.exposed.sql.not

fun Application.configureRouting() {
    val dao:UserDao = UserDaoImpl()
    val noteDao:NotesDao = NotesDaoImpl()
    routing {
        get("/") {
            call.respondText("Hello World!")
        }

        route("user"){
            get {
                call.respond("Provide user id")
            }
            get("{id}") {
                val id = call.parameters["id"]?.toInt()
                if (id != null)
                    call.respond(HttpStatusCode.OK, dao.getUser(id))
                else
                    call.respond(HttpStatusCode.OK, "Provide user id")
            }
            route("save-user") {
                post {
                    val text = call.receiveText()
                    val obj = Json.decodeFromString<User>(text)
                    println("Text----------------> $obj")
                    val name = obj.name
                    val email = obj.email
                    val password = obj.password

                    val res = dao.createUser(name = name, email = email, password = password)
                    if (res != null)
                        call.respond(HttpStatusCode.OK, res)
                    else
                        call.respond(HttpStatusCode.OK, "something went wrong")
                }
            }
        }
        route("note"){
            get("{nid}"){
                val id = call.parameters["nid"]?.toInt()
                if (id != null){
                    val res = noteDao.getNote(nid = id)
                    if (res != null)
                        call.respond(HttpStatusCode.OK, res)
                    else
                        call.respond(HttpStatusCode.OK, "Nothing found")
                }else
                    call.respond(HttpStatusCode.OK, "Provide note id")
            }

            get("all") {
                val res = noteDao.getNotes()
                call.respond(HttpStatusCode.OK, res)
            }

            post("upload"){
                val data = Json.decodeFromString<Note>(call.receiveText())
                val res = noteDao.createNote(Note(title = data.title, story = data.story))
                if (res != null)
                    call.respond(HttpStatusCode.OK, res)
                else
                    call.respond(HttpStatusCode.OK, "something went wrong")
            }
        }
    }
}
