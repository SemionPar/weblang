package pl.weblang.persistence

import org.amshove.kluent.`should be`
import org.amshove.kluent.`should equal`
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith
import pl.weblang.domain.background.source.ExactHitVO

@RunWith(JUnitPlatform::class)
class ExactHitsRepositoryTest : Spek({
                                         describe("Basic crud operations") {
                                             val connection = DatabaseConnection(DatabaseMode.TestDatabaseMode())
                                             val repository = ExactHitsRepository()
                                             beforeEachTest {
                                                 connection.createTable(ExactHitsRepository.ExactHitsTable)
                                             }
                                             afterEachTest {
                                                 connection.dropTable(ExactHitsRepository.ExactHitsTable)
                                             }
                                             it("Should create a source search result") {
                                                 repository.create(ExactHitVO(
                                                         fragmentSize = 1,
                                                         fragmentPosition = 15,
                                                         file = "file",
                                                         sourceIntegration = "sourceIntegration",
                                                         sourceIntegrationFileName = "sourceIntegrationFileName",
                                                         segmentNumber = 12,
                                                         timestamp = 123154123L))
                                                 repository.retrieveAll().asSequence().count() `should be` 1
                                             }
                                             it("Should retrieve all source search results") {
                                                 val sourceSearchResult = ExactHitVO(
                                                         fragmentSize = 1,
                                                         fragmentPosition = 15,
                                                         file = "file",
                                                         sourceIntegration = "sourceIntegration",
                                                         sourceIntegrationFileName = "sourceIntegrationFileName",
                                                         segmentNumber = 12,
                                                         timestamp = 123154123L)

                                                 repository.create(sourceSearchResult)

                                                 val results = repository.retrieveAll().asSequence()
                                                 results.first() `should equal` sourceSearchResult
                                             }
                                         }
                                     })

