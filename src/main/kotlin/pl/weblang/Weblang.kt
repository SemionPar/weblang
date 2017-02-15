package pl.weblang

import org.omegat.core.CoreEvents
import org.omegat.core.events.IApplicationEventListener
import org.omegat.core.events.IProjectEventListener
import pl.weblang.background.BackgroundService
import pl.weblang.background.source.ExactHitVO
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

    private val exactHitsRepository = ExactHitsRepository()
    private val suggestionsRepository = WildcardHitsRepository()
    private val missingGlossaryEntryRepository = MissingGlossaryEntryRepository()

    private val adapter: ViewModel<ExactHitVO> by lazy { ExactHitsViewModel(exactHitsRepository) }

    private val pane: Pane by lazy { Pane(CoreAdapter.mainWindow) }
    private val menu: Menu by lazy { Menu(CoreAdapter.mainWindow, adapter, missingGlossaryEntryRepository) }

    private val integrationSettings = IntegrationSettings()
    private val integrationManager = IntegrationManager(integrationSettings)
    private val backgroundService: BackgroundService by lazy {
        BackgroundService(integrationManager.verifierIntegrations(),
                missingGlossaryEntryRepository, exactHitsRepository, suggestionsRepository, CoreAdapter)
    }
    private val selectionHandler = SelectionAdapter(CoreAdapter)
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

    private fun createIApplicationEventListener(): IApplicationEventListener = object : IApplicationEventListener {
        override fun onApplicationStartup() {
            pane.addDockable()
            pane.initializeGui()
        }

        override fun onApplicationShutdown() {
        }
    }

    private fun startBackgroundVerifierService() {
        backgroundService.startBackgroundVerifierService()
    }

    private fun setupPane() {
        pane.assignKeyBinding(setupInstantSearchKeyAction())
    }

    private fun setupMenu() {
        menu.initializeMenu()
    }

    private fun setupInstantSearchKeyAction(): KeyAction {
        val CtrlG = KeyStroke.getKeyStroke(KeyEvent.VK_G, 1 shl 7, false)
        val shortcutKey = "instantSearchKeyPressed"
        val shortcut = Shortcut(CtrlG, shortcutKey)
        return KeyAction({
            selectionHandler.selection?.let {
                pane.displayInstantSearchResults(instantSearchController.search(it))
            }
        }, shortcut)
    }
}

