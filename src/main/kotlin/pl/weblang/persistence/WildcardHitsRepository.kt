package pl.weblang.persistence

import mu.KLogging
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import pl.weblang.domain.background.source.WildcardHitVO

/**
 * Repository for wildcard hits
 */
class WildcardHitsRepository {
    companion object : KLogging()

    object WildcardHitsTable : Table() {
        val id = integer("id").autoIncrement().primaryKey()
        val fragmentSize = integer("fragment_size")
        val fragmentPosition = integer("fragment_position")
        val wildcardPosition = integer("wildcard_position")
        val suggestion = text("suggestion")
        val file = text("file")
        val sourceIntegration = text("source_integration")
        val sourceIntegrationFileName = text("source_integration_file_name")
        val segmentNumber = integer("segment_number")
        val timestamp = long("timestamp")
    }

    fun create(wildcardHitVO: WildcardHitVO) {
        transaction {
            WildcardHitsTable.insert {
                it[fragmentSize] = wildcardHitVO.fragmentSize
                it[fragmentPosition] = wildcardHitVO.fragmentPosition
                it[wildcardPosition] = wildcardHitVO.wildcardPosition
                it[suggestion] = wildcardHitVO.suggestion
                it[file] = wildcardHitVO.file
                it[sourceIntegration] = wildcardHitVO.sourceIntegration
                it[sourceIntegrationFileName] = wildcardHitVO.sourceIntegrationFileName
                it[segmentNumber] = wildcardHitVO.segmentNumber
                it[timestamp] = wildcardHitVO.timestamp
            }
        }
        logger.info { "Inserted $wildcardHitVO" }

    }

    fun retrieveAll(): Iterator<WildcardHitVO> {
        return transaction { WildcardHitsTable.selectAll().map { fromRow(it) } }.iterator()
    }

    private fun fromRow(row: ResultRow) =
            WildcardHitVO(row[WildcardHitsTable.fragmentSize],
                          row[WildcardHitsTable.fragmentPosition],
                          row[WildcardHitsTable.wildcardPosition],
                          row[WildcardHitsTable.suggestion],
                          row[WildcardHitsTable.file],
                          row[WildcardHitsTable.sourceIntegration],
                          row[WildcardHitsTable.sourceIntegrationFileName],
                          row[WildcardHitsTable.segmentNumber],
                          row[WildcardHitsTable.timestamp])

}
