package com.example.models

import kotlinx.serialization.Serializable

@Serializable
data class User(
    var uid:Int = 0,
    val name:String,
    val email:String,
    val password:String
)