package com.example.models

import org.jetbrains.exposed.sql.Table


object Notes : Table("notes"){
    val nid = integer("nid").autoIncrement()
    val uid = integer("uid")
    val title = varchar("full_name", 1000)
    val story = text("story")
    val created = varchar("created", 50)
    val updated = varchar("updated", 50)
    val backgroundColor = varchar("bg_color", 50)

    override val primaryKey = PrimaryKey(nid)
}

