package com.example.db

import com.example.models.User

interface UserDao {
    suspend fun createUser(name:String, email:String, password:String) : User?

    suspend fun getUser(uid:Int) : User?
}