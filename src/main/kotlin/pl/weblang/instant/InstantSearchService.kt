package pl.weblang.instant

import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.async
import mu.KLogging
import pl.weblang.gui.KeyAction
import pl.weblang.gui.SelectionAdapter
import pl.weblang.gui.Shortcut
import pl.weblang.gui.pane.InstantSearchPaneController
import pl.weblang.integration.web.WebIntegrationController
import java.awt.event.KeyEvent
import javax.swing.KeyStroke

/**
 * Main Instant Search feature controller
 */
class InstantSearchService(val webIntegrationController: WebIntegrationController,
                           val selectionHandler: SelectionAdapter,
                           val instantSearchPaneController: InstantSearchPaneController) {
    companion object : KLogging()

    private val inputProcessor: InputProcessor = InputProcessor()

    fun search(selection: String) {
        val searchedPhrase = inputProcessor.toWords(selection)

        async(CommonPool) {
            val futureResults = async(CommonPool) {
                val response = webIntegrationController.processInstantSearch(searchedPhrase)
                InstantSearchResults(response)
            }
            instantSearchPaneController.displayLoadingSpinner()
            instantSearchPaneController.displayInstantSearchResults(futureResults.await())
        }
    }

    fun start() {
        instantSearchPaneController.initialize()
        instantSearchPaneController.assignKeyBinding(listOf(setupInstantSearchKeyAction()))
        logger.info { "Started InstantSearchService" }
    }

    private fun setupInstantSearchKeyAction(): KeyAction {
        val CtrlG = KeyStroke.getKeyStroke(KeyEvent.VK_G, 1 shl 7, false)
        val shortcutKey = "instantSearchKeyPressed"
        val shortcut = Shortcut(CtrlG, shortcutKey)
        return KeyAction({
            selectionHandler.selection?.let {
                this@InstantSearchService.search(it)
            }
        }, shortcut)
    }
}

class InputProcessor {
    fun toWords(input: String): List<String> {
        return input.split(" ")
    }
}

inline fun <T> Iterable<T>.sumByLong(selector: (T) -> Long): Long {
    val sum: Long = this
            .map { selector(it) }
            .sum()
    return sum
}
