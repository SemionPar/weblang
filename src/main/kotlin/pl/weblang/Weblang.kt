package pl.weblang

import org.omegat.core.CoreEvents
import org.omegat.core.events.IApplicationEventListener
import org.omegat.core.events.IProjectEventListener
import pl.weblang.background.BackgroundService
import pl.weblang.gui.*
import pl.weblang.instant.InstantSearchController
import pl.weblang.integration.IntegrationManager
import pl.weblang.integration.IntegrationSettings
import pl.weblang.integration.web.WebIntegrationController
import pl.weblang.persistence.ExactHitsRepository
import pl.weblang.persistence.MissingGlossaryEntryRepository
import pl.weblang.persistence.WildcardHitsRepository
import java.awt.event.KeyEvent
import javax.swing.KeyStroke

/**
 * Main plugin class
 */
class Weblang {

    private val directHitsRepository = ExactHitsRepository()
    private val suggestionsRepository = WildcardHitsRepository()
    private val missingGlossaryEntryRepository = MissingGlossaryEntryRepository()

    private val adapter: ExactHitsViewModel by lazy { ExactHitsViewModel(directHitsRepository) }

    private val pane: Pane by lazy { Pane(CoreAdapter.mainWindow) }
    private val menu: Menu by lazy { Menu(CoreAdapter.mainWindow, adapter) }

    private val integrationSettings = IntegrationSettings()
    private val integrationManager = IntegrationManager(integrationSettings)
    private val backgroundService: BackgroundService by lazy {
        BackgroundService(integrationManager.verifierIntegrations(),
                missingGlossaryEntryRepository, directHitsRepository, suggestionsRepository)
    }
    private val selectionHandler = SelectionHandler()
    private val instantSearchController: InstantSearchController by lazy {
        InstantSearchController(WebIntegrationController(integrationManager.instantSearchIntegrations()))
    }

    /**
     * Register listeners in OmegaT
     */
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
        backgroundService.startBackgroundVerifierService()
    }

    private fun setupPane() {
        pane.assignKeyBinding(KeyAction({
            selectionHandler.selection?.let {
                pane.displayInstantSearchResults(instantSearchController.search(it))
            }
        },
                Pair(KeyStroke.getKeyStroke(KeyEvent.VK_G, 1 shl 7, false),
                        "instantSearchKeyPressed")))
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

