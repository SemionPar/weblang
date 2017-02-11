package pl.weblang.background

import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.future.future
import kotlinx.coroutines.experimental.launch
import mu.KLogging
import org.omegat.tokenizer.LuceneEnglishTokenizer
import pl.weblang.CoreAdapter
import pl.weblang.background.forgetful.GlossaryTermSearchDispatcher
import pl.weblang.background.forgetful.GlossaryTermSearchJobBuilder
import pl.weblang.background.source.SourceSearchDispatcher
import pl.weblang.background.source.SourceSearchJobBuilder
import pl.weblang.databaseName
import pl.weblang.hasDatabaseFile
import pl.weblang.integration.VerifierServiceProvider
import pl.weblang.persistence.*
import kotlin.reflect.KProperty


class VerifierService(verifierProviders: List<VerifierServiceProvider>,
                      missingGlossaryEntryRepository: MissingGlossaryEntryRepository,
                      directHitsRepository: DirectHitsRepository,
                      suggestionsRepository: SuggestionsRepository) {
    companion object : KLogging()

    var databaseConnection: DatabaseConnection? = null

    val editorState: EditorState by lazy {
        EditorState(runVerificationJobOnSegmentChange())
    }

    val sourceSearchJobBuilder: SourceSearchJobBuilder = SourceSearchJobBuilder(
            LuceneEnglishTokenizer(),
            verifierProviders)
    val glossaryTermSearchJobBuilder: GlossaryTermSearchJobBuilder = GlossaryTermSearchJobBuilder()
    val sourceSearchDispatcher = SourceSearchDispatcher(sourceSearchJobBuilder, directHitsRepository,
            suggestionsRepository)
    val glossaryTermSearchDispatcher = GlossaryTermSearchDispatcher(glossaryTermSearchJobBuilder,
            missingGlossaryEntryRepository)

    val processedEntryChangedListener = ProcessedEntryChangedListener(editorState)

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
                    future { glossaryTermSearchDispatcher.start(new) }
                }
            }
        }
    }

    private fun startDatabase() {
        if (CoreAdapter.project.hasDatabaseFile) {
            establishConnectionWithProjectDatabase()
        } else {
            establishConnectionWithProjectDatabase()
            databaseConnection?.createTable(DirectHitsRepository.SourceSearchResults)
        }
    }

    private fun establishConnectionWithProjectDatabase() {
        databaseConnection = DatabaseConnection(DatabaseMode.ProductionDatabaseMode(
                "jdbc:h2:${CoreAdapter.project.databaseName};DB_CLOSE_DELAY=-1;DATABASE_TO_UPPER=false"))
    }
}



