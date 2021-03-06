package pl.weblang.persistence

import mu.KLogging
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import pl.weblang.domain.background.source.ExactHitVO

/**
 * Repository for exact hits
 */
class ExactHitsRepository : Repository<ExactHitVO> {
    companion object : KLogging()

    object ExactHitsTable : Table() {
        val id = integer("id").autoIncrement().primaryKey()
        val fragmentSize = integer("fragment_size")
        val fragmentPosition = integer("fragment_position")
        val file = text("file")
        val sourceIntegration = text("source_integration")
        val sourceIntegrationFileName = text("source_integration_file_name")
        val segmentNumber = integer("segment_number")
        val timestamp = long("timestamp")
    }

    override fun create(entity: ExactHitVO) {
        transaction {
            ExactHitsTable.insert {
                it[fragmentSize] = entity.fragmentSize
                it[fragmentPosition] = entity.fragmentPosition
                it[file] = entity.file
                it[sourceIntegration] = entity.sourceIntegration
                it[sourceIntegrationFileName] = entity.sourceIntegrationFileName
                it[segmentNumber] = entity.segmentNumber
                it[timestamp] = entity.timestamp
            }
        }
        logger.info { "Inserted $entity" }

    }

    override fun retrieveAll(): Iterator<ExactHitVO> {
        return transaction { ExactHitsTable.selectAll().map { fromRow(it) } }.iterator()
    }

    override fun getFieldNames(): List<String> {
        return ExactHitsRepository.ExactHitsTable.columns.map { it.name }
    }

    private fun fromRow(row: ResultRow): ExactHitVO =
            ExactHitVO(row[ExactHitsTable.fragmentSize],
                       row[ExactHitsTable.fragmentPosition],
                       row[ExactHitsTable.file],
                       row[ExactHitsTable.sourceIntegration],
                       row[ExactHitsTable.sourceIntegrationFileName],
                       row[ExactHitsTable.segmentNumber],
                       row[ExactHitsTable.timestamp])

}
