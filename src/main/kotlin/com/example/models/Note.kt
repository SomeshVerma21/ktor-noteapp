package com.example.models

import kotlinx.serialization.Serializable

@Serializable
data class Note(
    val nid:Int = 0,
    val title:String,
    val story:String
)
