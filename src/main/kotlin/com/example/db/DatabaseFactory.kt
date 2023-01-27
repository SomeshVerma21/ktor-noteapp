package com.example.db

import com.example.models.Notes
import com.example.models.Users
import com.example.utils.Constant
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseFactory {
    fun init() {
        val driverClassName = System.getenv()[Constant.JDBC_CLASS_NAME].orEmpty()
        val jdbcURL = System.getenv()[Constant.JDBC_URL].orEmpty()
        val database = Database.connect(jdbcURL, driverClassName)

        transaction(database){
            SchemaUtils.create(Notes)
            SchemaUtils.create(Users)
        }

    }

    suspend fun <T> dbQuery(block:suspend () -> T) : T =
        newSuspendedTransaction(Dispatchers.IO){block()}
}