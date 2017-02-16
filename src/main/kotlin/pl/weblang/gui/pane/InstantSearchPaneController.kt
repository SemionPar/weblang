package pl.weblang.gui.pane

import mu.KLogging
import org.omegat.gui.main.IMainWindow
import pl.weblang.gui.KeyAction
import pl.weblang.gui.Templater
import pl.weblang.instant.InstantSearchResults

val templater: Templater by lazy { Templater() }

class InstantSearchPaneController(mainWindow: IMainWindow) {
    companion object : KLogging()

    private val instantSearchPane = InstantSearchPane(mainWindow)

    fun assignKeyBinding(keyActions: Iterable<KeyAction>) {
        keyActions.forEach { instantSearchPane.addKeyBinding(it) }
    }

    fun displayInstantSearchResults(results: InstantSearchResults) {
        val html = templater.generateHtml(results)
        logger.debug { "Templater produced: $html" }
        instantSearchPane.displayText(html)
    }

    fun displayLoadingSpinner() {
        logger.debug { "Loading spinner..." }
        instantSearchPane.displayLoadingSpinner()
    }

    fun initialize() {
        instantSearchPane.addDockable()
    }
}

