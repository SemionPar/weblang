package pl.weblang.persistence

import mu.KLogging
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import pl.weblang.background.source.WildcardSuggestionHit

class SuggestionsRepository {
    companion object : KLogging()

    object Suggestions : Table() {
        val id = integer("id").autoIncrement().primaryKey()
        val fragmentSize = integer("fragment_size")
        val fragmentPosition = integer("fragment_position")
        val wildcardPosition = integer("wildcard_position")
        val suggestion = text("suggestion")
        val file = text("file")
        val sourceIntegration = text("source_integration")
        val segmentNumber = integer("segment_number")
        val timestamp = long("timestamp")

    }

    fun create(wildcardSuggestionHit: WildcardSuggestionHit) {
        transaction {
            Suggestions.insert {
                it[fragmentSize] = wildcardSuggestionHit.fragmentSize
                it[fragmentPosition] = wildcardSuggestionHit.fragmentPosition
                it[wildcardPosition] = wildcardSuggestionHit.wildcardPosition
                it[suggestion] = wildcardSuggestionHit.suggestion
                it[file] = wildcardSuggestionHit.file
                it[sourceIntegration] = wildcardSuggestionHit.sourceIntegration
                it[segmentNumber] = wildcardSuggestionHit.segmentNumber
                it[timestamp] = wildcardSuggestionHit.timestamp
            }
        }
        logger.info { "Inserted $wildcardSuggestionHit" }

    }

    fun retrieveAll(): List<WildcardSuggestionHit> {
        return transaction { Suggestions.selectAll().map { fromRow(it) } }
    }

    fun count(): Int {
        return retrieveAll().count()
    }

    private fun fromRow(row: ResultRow) =
            WildcardSuggestionHit(row[Suggestions.fragmentSize],
                    row[Suggestions.fragmentPosition],
                    row[Suggestions.wildcardPosition],
                    row[Suggestions.suggestion],
                    row[Suggestions.file],
                    row[Suggestions.sourceIntegration],
                    row[Suggestions.segmentNumber],
                    row[Suggestions.timestamp])

}
