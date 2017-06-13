package pl.weblang.domain.instant

import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.async
import mu.KLogging
import pl.weblang.domain.instant.view.gui.InstantSearchPaneController
import pl.weblang.integration.web.WebIntegrationController
import pl.weblang.omegat.SelectionAdapter
import pl.weblang.shortcut.KeyAction
import pl.weblang.shortcut.Shortcut
import java.awt.event.KeyEvent
import javax.swing.KeyStroke

/**
 * Main Instant Search feature controller
 */
class InstantSearchService(val webIntegrationController: WebIntegrationController,
                           val selectionHandler: SelectionAdapter,
                           val instantSearchPaneController: InstantSearchPaneController) {
    companion object : KLogging()

    private val inputValidator: InputValidator = InputValidator()

    fun search(selection: String) {
        try {
            val searchedPhrase = selection.validate().trim()
            async(CommonPool) {
                val futureResults = async(CommonPool) {
                    val response = webIntegrationController.processInstantSearch(searchedPhrase)
                    InstantSearchResults(response)
                }
                instantSearchPaneController.displayLoadingSpinner()
                instantSearchPaneController.displayInstantSearchResults(futureResults.await())
            }
        } catch(e: IllegalArgumentException) {
            logger.debug { e }
            instantSearchPaneController.displayError()
        }
    }

    fun start() {
        instantSearchPaneController.initialize()
        instantSearchPaneController.assignKeyBinding(listOf(setupInstantSearchKeyAction()))
        logger.info { "Started InstantSearchService" }
    }

    private fun setupInstantSearchKeyAction(): KeyAction {
        val ctrlG = KeyStroke.getKeyStroke(KeyEvent.VK_G, 1 shl 7, false)
        val shortcutKey = "instantSearchKeyPressed"
        val shortcut = Shortcut(ctrlG, shortcutKey)
        return KeyAction({
                             selectionHandler.selection?.let {
                                 this@InstantSearchService.search(it)
                             }
                         }, shortcut)
    }

    private fun String.validate() = this.also { inputValidator.validate(this) }
}
