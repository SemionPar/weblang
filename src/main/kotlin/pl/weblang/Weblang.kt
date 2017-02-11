package pl.weblang

import org.omegat.core.CoreEvents
import org.omegat.core.events.IApplicationEventListener
import org.omegat.core.events.IProjectEventListener
import pl.weblang.background.VerifierService
import pl.weblang.gui.KeyAction
import pl.weblang.gui.Menu
import pl.weblang.gui.Pane
import pl.weblang.gui.SelectionHandler
import pl.weblang.instant.InstantSearchController
import pl.weblang.integration.IntegrationManager
import pl.weblang.integration.IntegrationSettings
import pl.weblang.integration.web.WebIntegrationController
import pl.weblang.persistence.DirectHitsRepository
import pl.weblang.persistence.MissingGlossaryEntryRepository
import pl.weblang.persistence.SuggestionsRepository
import java.awt.event.KeyEvent
import javax.swing.KeyStroke


class Weblang {

    private val directHitsRepository = DirectHitsRepository()
    private val suggestionsRepository = SuggestionsRepository()
    private val missingGlossaryEntryRepository = MissingGlossaryEntryRepository()
    private val pane: Pane by lazy { Pane(CoreAdapter.mainWindow) }
    private val menu: Menu by lazy { Menu(CoreAdapter.mainWindow, directHitsRepository) }

    private val integrationSettings = IntegrationSettings()
    private val integrationManager = IntegrationManager(integrationSettings)
    private val verifierService: VerifierService by lazy {
        VerifierService(integrationManager.verifierIntegrations(),
                missingGlossaryEntryRepository, directHitsRepository, suggestionsRepository)
    }
    private val selectionHandler = SelectionHandler()
    private val instantSearchController: InstantSearchController by lazy {
        InstantSearchController(WebIntegrationController(integrationManager.instantSearchIntegrations()))
    }

    init {
        CoreEvents.registerApplicationEventListener(createIApplicationEventListener())
        CoreEvents.registerProjectChangeListener(createIProjectEventListener())
    }

    private fun createIProjectEventListener(): IProjectEventListener = IProjectEventListener { event ->
        when (event) {
            IProjectEventListener.PROJECT_CHANGE_TYPE.CLOSE -> {
            }
            IProjectEventListener.PROJECT_CHANGE_TYPE.COMPILE -> {
            }
            IProjectEventListener.PROJECT_CHANGE_TYPE.CREATE -> {
                setupPane()
                setupMenu()
                startBackgroundVerifierService()
            }
            IProjectEventListener.PROJECT_CHANGE_TYPE.LOAD -> {
                setupPane()
                setupMenu()
                startBackgroundVerifierService()
            }
            IProjectEventListener.PROJECT_CHANGE_TYPE.SAVE -> {
            }
            IProjectEventListener.PROJECT_CHANGE_TYPE.MODIFIED -> {
            }
            null -> {
            }
        }
    }

    private fun startBackgroundVerifierService() {
        verifierService.startBackgroundVerifierService()
    }

    private fun setupPane() {
        pane.assignKeyBinding(KeyAction({
            selectionHandler.selection?.let {
                pane.displayInstantSearchResults(instantSearchController.search(it))
            }
        },
                Pair(KeyStroke.getKeyStroke(KeyEvent.VK_G, 1 shl 7, false),
                        "instantSearchKeyPressed")))
        pane.addDockable()
    }

    private fun setupMenu() {
        menu.initializeMenu()
    }

    private fun createIApplicationEventListener(): IApplicationEventListener = object : IApplicationEventListener {
        override fun onApplicationStartup() {
            pane.addDockable()
            pane.initializeGui()
        }

        override fun onApplicationShutdown() {
        }
    }
}

