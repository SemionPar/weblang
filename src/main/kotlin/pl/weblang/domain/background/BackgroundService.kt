package pl.weblang.domain.background

import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.launch
import mu.KLogging
import org.omegat.tokenizer.LuceneEnglishTokenizer
import pl.weblang.domain.background.forgetful.GlossarySearchDispatcher
import pl.weblang.domain.background.forgetful.GlossarySearchJobBuilder
import pl.weblang.domain.background.port.SearchServiceProvider
import pl.weblang.domain.background.source.SourceSearchDispatcher
import pl.weblang.domain.background.source.SourceSearchJobBuilder
import pl.weblang.omegat.CoreAdapter
import pl.weblang.omegat.databaseName
import pl.weblang.omegat.hasDatabaseFile
import pl.weblang.persistence.*
import kotlin.reflect.KProperty

/**
 * Handles background search jobs
 */
class BackgroundService(searchProviders: List<SearchServiceProvider>,
                        missingGlossaryEntryRepository: MissingGlossaryEntryRepository,
                        exactHitsRepository: ExactHitsRepository,
                        wildcardHitsRepository: WildcardHitsRepository,
                        private val coreAdapter: CoreAdapter) {

    companion object : KLogging()

    var databaseConnection: DatabaseConnection? = null

    val editorState: EditorState by lazy {
        EditorState(runVerificationJobOnSegmentChange(), coreAdapter)
    }

    val sourceSearchJobBuilder: SourceSearchJobBuilder = SourceSearchJobBuilder(
            LuceneEnglishTokenizer(),
            searchProviders)

    val glossarySearchJobBuilder: GlossarySearchJobBuilder = GlossarySearchJobBuilder(coreAdapter)

    val sourceSearchDispatcher = SourceSearchDispatcher(sourceSearchJobBuilder, exactHitsRepository,
                                                        wildcardHitsRepository)

    val glossarySearchDispatcher = GlossarySearchDispatcher(glossarySearchJobBuilder,
                                                            missingGlossaryEntryRepository)

    val processedEntryChangedListener = ProcessedEntryChangedListener(editorState)

    /**
     * Start listening for events
     */
    fun start() {
        startDatabase()
        coreAdapter.registerEntryEventListener(processedEntryChangedListener)
        logger.info { "Started BackgroundService" }
    }

    private fun runVerificationJobOnSegmentChange(): (KProperty<*>, Segment, Segment) -> Unit {
        return {
            property, old, new ->
            if (!new.hasInvalidState) {
                launch(CommonPool) {
                    glossarySearchDispatcher.start(new)
                }
                launch(CommonPool) {
                    sourceSearchDispatcher.start(new)
                }
            }
        }
    }

    private fun startDatabase() {
        if (coreAdapter.project.hasDatabaseFile) {
            establishConnectionWithProjectDatabase()
        } else {
            establishConnectionWithProjectDatabase()
            databaseConnection?.createTable(ExactHitsRepository.ExactHitsTable)
            databaseConnection?.createTable(WildcardHitsRepository.WildcardHitsTable)
            databaseConnection?.createTable(MissingGlossaryEntryRepository.MissingGlossaryEntriesTable)
        }
    }

    private fun establishConnectionWithProjectDatabase() {
        databaseConnection = DatabaseConnection(DatabaseMode.ProductionDatabaseMode(coreAdapter.project.databaseName))
    }
}

