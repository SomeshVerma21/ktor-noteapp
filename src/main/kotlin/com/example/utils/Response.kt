package com.example.utils

import kotlinx.serialization.Serializable

@Serializable
sealed class Response{
    data class Success(val message: String, val data: Any) : Response()
    @Serializable data class Failed(val message: String) : Response()
}
