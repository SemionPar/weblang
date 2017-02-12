package pl.weblang.persistence

import mu.KLogging
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import pl.weblang.background.forgetful.MissingGlossaryEntry

/**
 * Repository for missing glossary terms
 */
class MissingGlossaryEntryRepository {
    companion object : KLogging()

    object MissingGlossaryEntriesTable : Table() {
        val id = integer("id").autoIncrement().primaryKey()
        val file = text("file")
        val segmentNumber = integer("segment_number")
        val timestamp = long("timestamp")

    }

    fun create(missingGlossaryEntry: MissingGlossaryEntry) {
        transaction {
            MissingGlossaryEntriesTable.insert {
                it[file] = missingGlossaryEntry.file
                it[segmentNumber] = missingGlossaryEntry.segmentNumber
                it[timestamp] = missingGlossaryEntry.timestamp
            }
        }
        logger.info { "Inserted $missingGlossaryEntry" }

    }

    fun retrieveAll(): List<MissingGlossaryEntry> {
        return transaction { MissingGlossaryEntriesTable.selectAll().map { fromRow(it) } }
    }

    fun count(): Int {
        return retrieveAll().count()
    }

    private fun fromRow(row: ResultRow): MissingGlossaryEntry =
            MissingGlossaryEntry(row[MissingGlossaryEntriesTable.file],
                    row[MissingGlossaryEntriesTable.segmentNumber],
                    row[MissingGlossaryEntriesTable.timestamp])

}
