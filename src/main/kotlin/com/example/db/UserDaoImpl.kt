package com.example.db

import com.example.models.User
import com.example.models.Users
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll

class UserDaoImpl : UserDao {

    private fun resultRowToUser(row:ResultRow) = User(
        uid = row[Users.uid],
        name = row[Users.fullName],
        email = row[Users.email],
        password = row[Users.password]
    )
    override suspend fun createUser(name: String, email: String, password: String): User? = DatabaseFactory.dbQuery {
        val res = Users.insert {
            it[Users.fullName] = name
            it[Users.email] = email
            it[Users.password] = password
        }.resultedValues

        res?.singleOrNull()?.let (::resultRowToUser)
    }

    override suspend fun getUser(uid:Int): User?  =  DatabaseFactory.dbQuery {
        val user = Users.select(Users.uid eq uid).map ( ::resultRowToUser )
        if (user.isEmpty())
            return@dbQuery null
        else
            return@dbQuery user.last()
    }

}