package pl.weblang.persistence

import org.amshove.kluent.`should be`
import org.amshove.kluent.`should equal`
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith
import pl.weblang.background.source.SourceSearchResult


@RunWith(JUnitPlatform::class)
class SegmentVerificationRepositoryTest : Spek({
                                                   describe("Basic crud operations") {
                                                       val connection = DatabaseConnection(DatabaseMode.TestDatabaseMode())
                                                       val repository = SegmentVerificationRepository()
                                                       beforeEachTest {
                                                           connection.createTable(SegmentVerificationRepository.SourceSearchResults)
                                                       }
                                                       afterEachTest {
                                                           connection.dropTable(SegmentVerificationRepository.SourceSearchResults)
                                                       }
                                                       it("Should create a source search result") {
                                                           repository.create(SourceSearchResult(
                                                                   fragmentSize = 1,
                                                                   fragmentPosition = 15,
                                                                   file = "file",
                                                                   sourceIntegration = "sourceIntegration",
                                                                   segmentNumber = 12,
                                                                   timestamp = 123154123L))
                                                           repository.count() `should be` 1
                                                       }
                                                       it("Should retrieve all source search results") {
                                                           val sourceSearchResult = SourceSearchResult(
                                                                   fragmentSize = 1,
                                                                   fragmentPosition = 15,
                                                                   file = "file",
                                                                   sourceIntegration = "sourceIntegration",
                                                                   segmentNumber = 12,
                                                                   timestamp = 123154123L)

                                                           repository.create(sourceSearchResult)

                                                           val results = repository.retrieveAll()
                                                           results.size `should be` 1
                                                           results.first() `should equal` sourceSearchResult
                                                       }
                                                   }
                                               })


