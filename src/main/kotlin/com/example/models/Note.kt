package com.example.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Note(
    val nid:Int = 0,
    val uid:Int,
    val title:String,
    val story:String,
    val created:String,
    val updated:String,
    @SerialName("background_color")
    val backgroundColor:String
)
