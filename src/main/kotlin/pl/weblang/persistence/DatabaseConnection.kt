package pl.weblang.persistence

import mu.KLogging
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.transactions.transaction

class DatabaseConnection(mode: DatabaseMode) {
    companion object : KLogging()

    val connection: Database = Database.connect(mode.dataSource, driver = "org.h2.Driver")

    init {
        logger.info { "Database connection established with mode: $mode" }
    }

    fun createTable(table: Table) {
        transaction {
            SchemaUtils.create(table)
        }
        logger.info { "Table $table created" }
    }

    fun dropTable(table: Table) {
        transaction {
            SchemaUtils.drop(table)
        }
        logger.info { "Table $table dropped" }
    }
}

sealed class DatabaseMode {
    abstract val dataSource: String

    data class TestDatabaseMode(override val dataSource: String = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1") : DatabaseMode()
    data class ProductionDatabaseMode(override val dataSource: String) : DatabaseMode()
}
