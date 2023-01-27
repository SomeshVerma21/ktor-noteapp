package com.example.routing

import com.example.db.NotesDao
import com.example.db.NotesDaoImpl
import com.example.db.UserDao
import com.example.db.UserDaoImpl
import com.example.models.Note
import com.example.models.User
import com.example.utils.CheckApiKey
import com.example.utils.Constant
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

fun Application.configureRouting() {
    val dao:UserDao = UserDaoImpl()
    val noteDao:NotesDao = NotesDaoImpl()
    routing {
        get("/") {
            if (CheckApiKey(call.request.headers[Constant.API_KEY]))
                call.respondText("Hello World!")
            else
                call.respond(HttpStatusCode.Unauthorized)
        }
        route("user"){
            get {
                if (CheckApiKey(call.request.headers[Constant.API_KEY]))
                    call.respond("Provide user id")
                else
                    call.respond(HttpStatusCode.Unauthorized)
            }
            get("{id}") {
                if (CheckApiKey(call.request.headers[Constant.API_KEY])){
                    val id = call.parameters["id"]?.toInt()
                    if (id != null){
                        val res = dao.getUser(id)
                        if (res != null)
                            call.respond(HttpStatusCode.OK, res)
                        else
                            call.respond(HttpStatusCode.OK, "User not found")
                    }
                    else
                        call.respond(HttpStatusCode.OK, "Provide user id")
                }else
                    call.respond(HttpStatusCode.Unauthorized)
            }
            route("save-user") {
                post {
                    if (CheckApiKey(call.request.headers[Constant.API_KEY])){
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
                    }else
                        call.respond(HttpStatusCode.Unauthorized)

                }
            }
        }
        route("note"){
            get("{nid}"){
                if (CheckApiKey(call.request.headers[Constant.API_KEY])){
                    val id = call.parameters["nid"]?.toInt()
                    if (id != null){
                        val res = noteDao.getNote(nid = id)
                        if (res != null)
                            call.respond(HttpStatusCode.OK, res)
                        else
                            call.respond(HttpStatusCode.OK, "Nothing found")
                    }else
                        call.respond(HttpStatusCode.OK, "Provide note id")
                }else
                    call.respond(HttpStatusCode.Unauthorized)

            }

            get("all") {
                if(CheckApiKey(call.request.headers[Constant.API_KEY])){
                    val res = noteDao.getNotes()
                    call.respond(HttpStatusCode.OK, res)
                }else
                    call.respond(HttpStatusCode.Unauthorized)
            }

            post("upload"){
                if (CheckApiKey(call.request.headers[Constant.API_KEY])){
                    try {
                        val data = Json.decodeFromString<Note>(call.receiveText())
                        val res = noteDao.createNote(
                            Note(title = data.title, uid = data.uid, story = data.story, created = data.created,
                                updated = data.updated, backgroundColor = data.backgroundColor)
                        )
                        if (res != null)
                            call.respond(HttpStatusCode.OK, res)
                        else
                            call.respond(HttpStatusCode.OK, "something went wrong")
                    }catch (e:Exception){
                        e.printStackTrace()
                        call.respond(HttpStatusCode.OK, "Parameter missing")
                    }
                }else
                    call.respond(HttpStatusCode.Unauthorized)
            }
        }
    }
}
