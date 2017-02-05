package pl.weblang.background

import kotlinx.coroutines.async
import mu.KLogging
import org.omegat.tokenizer.LuceneEnglishTokenizer
import pl.weblang.CoreAdapter
import pl.weblang.background.forgetful.GlossaryTermSearchDispatcher
import pl.weblang.background.source.SourceSearchDispatcher
import pl.weblang.background.source.SourceSearchJobBuilder
import pl.weblang.databaseName
import pl.weblang.hasDatabaseFile
import pl.weblang.integration.VerifierIntegrationService
import pl.weblang.persistence.DatabaseConnection
import pl.weblang.persistence.DatabaseMode
import pl.weblang.persistence.SegmentVerificationRepository
import java.util.concurrent.CompletableFuture
import kotlin.concurrent.thread
import kotlin.reflect.KProperty


class VerifierService(verifierIntegrations: List<VerifierIntegrationService>,
                      private val segmentVerificationRepository: SegmentVerificationRepository) {
    companion object : KLogging()

    var databaseConnection: DatabaseConnection? = null

    val verifyPreviouslyProcessedEntry: (KProperty<*>, Segment, Segment) -> Unit = {
        property, old, new ->
        thread(name = "segmentChangeProcessor") {
            if (new.hasInvalidState) return@thread
            async {
                CompletableFuture.supplyAsync {
                    sourceSearchDispatcher.start(new,
                                                 segmentVerificationRepository)
                }
                CompletableFuture.supplyAsync { glossaryTermSearchDispatcher.start(new) }
            }
        }
    }

    val editorState: EditorState by lazy {
        EditorState(verifyPreviouslyProcessedEntry)
    }
    val sourceSearchJobBuilder: SourceSearchJobBuilder = SourceSearchJobBuilder(
            LuceneEnglishTokenizer(),
            verifierIntegrations)
    val sourceSearchDispatcher = SourceSearchDispatcher(sourceSearchJobBuilder)

    val glossaryTermSearchDispatcher = GlossaryTermSearchDispatcher(sourceSearchJobBuilder)

    val processedEntryChangedListener = ProcessedEntryChangedListener(editorState)

    fun startBackgroundVerifierService() {
        startDatabase()
        CoreAdapter.registerEntryEventListener(processedEntryChangedListener)
    }

    private fun startDatabase() {
        if (CoreAdapter.project.hasDatabaseFile) {
            establishConnectionWithProjectDatabase()
        } else {
            establishConnectionWithProjectDatabase()
            databaseConnection?.createTable(SegmentVerificationRepository.SourceSearchResults)
        }
    }

    private fun establishConnectionWithProjectDatabase() {
        databaseConnection = DatabaseConnection(DatabaseMode.ProductionDatabaseMode("jdbc:h2:${CoreAdapter.project.databaseName};DB_CLOSE_DELAY=-1;DATABASE_TO_UPPER=false"))
    }
}



