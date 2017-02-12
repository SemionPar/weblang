package pl.weblang.background

import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.future.future
import kotlinx.coroutines.experimental.launch
import mu.KLogging
import org.omegat.tokenizer.LuceneEnglishTokenizer
import pl.weblang.CoreAdapter
import pl.weblang.background.forgetful.GlossarySearchDispatcher
import pl.weblang.background.forgetful.GlossarySearchJobBuilder
import pl.weblang.background.source.SourceSearchDispatcher
import pl.weblang.background.source.SourceSearchJobBuilder
import pl.weblang.databaseName
import pl.weblang.hasDatabaseFile
import pl.weblang.integration.VerifierServiceProvider
import pl.weblang.persistence.*
import kotlin.reflect.KProperty

/**
 * Handles background search jobs
 */
class BackgroundService(verifierProviders: List<VerifierServiceProvider>,
                        missingGlossaryEntryRepository: MissingGlossaryEntryRepository,
                        exactHitsRepository: ExactHitsRepository,
                        wildcardHitsRepository: WildcardHitsRepository) {

    companion object : KLogging()

    var databaseConnection: DatabaseConnection? = null

    val editorState: EditorState by lazy {
        EditorState(runVerificationJobOnSegmentChange())
    }

    val sourceSearchJobBuilder: SourceSearchJobBuilder = SourceSearchJobBuilder(
            LuceneEnglishTokenizer(),
            verifierProviders)

    val glossarySearchJobBuilder: GlossarySearchJobBuilder = GlossarySearchJobBuilder()

    val sourceSearchDispatcher = SourceSearchDispatcher(sourceSearchJobBuilder, exactHitsRepository,
            wildcardHitsRepository)

    val glossarySearchDispatcher = GlossarySearchDispatcher(glossarySearchJobBuilder,
            missingGlossaryEntryRepository)

    val processedEntryChangedListener = ProcessedEntryChangedListener(editorState)

    /**
     * Start listening for events
     */
    fun startBackgroundVerifierService() {
        startDatabase()
        CoreAdapter.registerEntryEventListener(processedEntryChangedListener)
    }

    private fun runVerificationJobOnSegmentChange(): (KProperty<*>, Segment, Segment) -> Unit {
        return {
            property, old, new ->
            if (!new.hasInvalidState) {
                launch(CommonPool) {
                    future { sourceSearchDispatcher.start(new) }
                    future { glossarySearchDispatcher.start(new) }
                }
            }
        }
    }

    private fun startDatabase() {
        if (CoreAdapter.project.hasDatabaseFile) {
            establishConnectionWithProjectDatabase()
        } else {
            establishConnectionWithProjectDatabase()
            databaseConnection?.createTable(ExactHitsRepository.ExactHitsTable)
            databaseConnection?.createTable(WildcardHitsRepository.WildcardHitsTable)
            databaseConnection?.createTable(MissingGlossaryEntryRepository.MissingGlossaryEntriesTable)
        }
    }

    private fun establishConnectionWithProjectDatabase() {
        databaseConnection = DatabaseConnection(DatabaseMode.ProductionDatabaseMode(
                "jdbc:h2:${CoreAdapter.project.databaseName};DB_CLOSE_DELAY=-1;DATABASE_TO_UPPER=false"))
    }
}

