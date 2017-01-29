package pl.weblang.persistence

import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import pl.weblang.background.source.SourceSearchResult

class SegmentVerificationRepository {

    object SourceSearchResults : Table() {
        val id = integer("id").autoIncrement().primaryKey()
        val fragmentSize = integer("fragment_size")
        val fragmentPosition = integer("fragment_position")
        val file = text("file")
        val sourceIntegration = text("source_integration")
        val segmentNumber = integer("segment_number")
        val timestamp = long("timestamp")

    }

    fun create(sourceSearchResult: SourceSearchResult) {
        transaction {
            SourceSearchResults.insert {
                it[fragmentSize] = sourceSearchResult.fragmentSize
                it[fragmentPosition] = sourceSearchResult.fragmentPosition
                it[file] = sourceSearchResult.file
                it[sourceIntegration] = sourceSearchResult.sourceIntegration
                it[segmentNumber] = sourceSearchResult.segmentNumber
                it[timestamp] = sourceSearchResult.timestamp
            }
        }
    }

    fun retrieveAll(): List<SourceSearchResult> {
        return transaction { SourceSearchResults.selectAll().map { fromRow(it) } }
    }

    fun count(): Int {
        return retrieveAll().count()
    }

    private fun fromRow(row: ResultRow): SourceSearchResult =
            SourceSearchResult(row[SourceSearchResults.fragmentSize],
                               row[SourceSearchResults.fragmentPosition],
                               row[SourceSearchResults.file],
                               row[SourceSearchResults.sourceIntegration],
                               row[SourceSearchResults.segmentNumber],
                               row[SourceSearchResults.timestamp])

}
