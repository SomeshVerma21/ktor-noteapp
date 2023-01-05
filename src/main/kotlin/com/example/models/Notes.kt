package com.example.models

import org.jetbrains.exposed.sql.Table


object Notes : Table("notes"){
    val nid = integer("nid").autoIncrement()
    val title = varchar("full_name", 1000)
    val story = text("story")

    override val primaryKey = PrimaryKey(nid)
}

