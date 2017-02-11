package pl.weblang.persistence

import mu.KLogging
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import pl.weblang.background.source.SourceDirectHit

class DirectHitsRepository {
    companion object : KLogging()

    object SourceSearchResults : Table() {
        val id = integer("id").autoIncrement().primaryKey()
        val fragmentSize = integer("fragment_size")
        val fragmentPosition = integer("fragment_position")
        val file = text("file")
        val sourceIntegration = text("source_integration")
        val segmentNumber = integer("segment_number")
        val timestamp = long("timestamp")

    }

    fun create(sourceDirectHit: SourceDirectHit) {
        transaction {
            SourceSearchResults.insert {
                it[fragmentSize] = sourceDirectHit.fragmentSize
                it[fragmentPosition] = sourceDirectHit.fragmentPosition
                it[file] = sourceDirectHit.file
                it[sourceIntegration] = sourceDirectHit.sourceIntegration
                it[segmentNumber] = sourceDirectHit.segmentNumber
                it[timestamp] = sourceDirectHit.timestamp
            }
        }
        logger.info { "Inserted $sourceDirectHit" }

    }

    fun retrieveAll(): List<SourceDirectHit> {
        return transaction { SourceSearchResults.selectAll().map { fromRow(it) } }
    }

    fun count(): Int {
        return retrieveAll().count()
    }

    private fun fromRow(row: ResultRow): SourceDirectHit =
            SourceDirectHit(row[SourceSearchResults.fragmentSize],
                               row[SourceSearchResults.fragmentPosition],
                               row[SourceSearchResults.file],
                               row[SourceSearchResults.sourceIntegration],
                               row[SourceSearchResults.segmentNumber],
                               row[SourceSearchResults.timestamp])

}
