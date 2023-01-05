package com.example.models

import org.jetbrains.exposed.sql.Table


object Users : Table("users"){
    val uid = integer("uid").autoIncrement()
    val fullName = varchar("full_name", 200)
    val email = varchar("email", 50)
    val password = text("password")

    override val primaryKey = PrimaryKey(uid)
}