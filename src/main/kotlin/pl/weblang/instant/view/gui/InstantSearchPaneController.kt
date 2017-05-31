package pl.weblang.instant.view.gui

import mu.KLogging
import org.omegat.gui.main.IMainWindow
import pl.weblang.instant.InstantSearchResults
import pl.weblang.instant.view.template.Templater
import pl.weblang.shortcut.KeyAction

val templater: Templater by lazy { Templater() }

class InstantSearchPaneController(private val mainWindow: IMainWindow) {

    companion object : KLogging()

    private val instantSearchPane = InstantSearchPane()

    fun assignKeyBinding(keyActions: Iterable<KeyAction>) {
        keyActions.forEach { instantSearchPane.addKeyBinding(it) }
    }

    fun displayInstantSearchResults(results: InstantSearchResults) {
        val html = templater.generateHtml(results)
        logger.debug { "Templater produced: $html" }
        instantSearchPane.displayHtml(html)
    }

    fun displayLoadingSpinner() {
        logger.debug { "Loading spinner..." }
        instantSearchPane.showProgressAnimation()
    }

    fun initialize() {
        mainWindow.addDockable(instantSearchPane.getOmegaTPane())
        InstantSearchPane.logger.info { "Weblang pane added as dockable" }
    }

    fun displayError() {
        instantSearchPane.displayHtml("Oops! An error occurred.")
    }
}

