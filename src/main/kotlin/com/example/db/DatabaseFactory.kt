package com.example.db

import com.example.models.Notes
import com.example.models.Users
import com.example.utils.Constant
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseFactory {
    fun init() {
        val database = Database.connect(hikari())

        transaction(database){
            SchemaUtils.create(Notes)
            SchemaUtils.create(Users)
        }

    }

    private fun hikari(): HikariDataSource {
        val config = HikariConfig()
        config.driverClassName = System.getenv()[Constant.JDBC_CLASS_NAME]
        config.jdbcUrl = System.getenv()[Constant.JDBC_URL]
        config.username = System.getenv()[Constant.DB_USER_NAME]
        config.password = System.getenv()[Constant.DB_PASSWORD]
        config.maximumPoolSize = 3
        config.isAutoCommit = false
        config.transactionIsolation = "TRANSACTION_REPEATABLE_READ"
        config.validate()
        return HikariDataSource(config)
    }

    suspend fun <T> dbQuery(block:suspend () -> T) : T =
        newSuspendedTransaction(Dispatchers.IO){block()}
}