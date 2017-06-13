package pl.weblang

import org.omegat.core.CoreEvents
import org.omegat.core.events.IApplicationEventListener
import org.omegat.core.events.IProjectEventListener
import pl.weblang.domain.background.BackgroundService
import pl.weblang.domain.background.gui.ExactHitsViewModel
import pl.weblang.domain.background.gui.ViewModel
import pl.weblang.domain.background.source.ExactHitVO
import pl.weblang.domain.instant.InstantSearchService
import pl.weblang.domain.instant.view.gui.InstantSearchPaneController
import pl.weblang.integration.IntegrationManager
import pl.weblang.integration.IntegrationSettings
import pl.weblang.integration.web.WebIntegrationController
import pl.weblang.omegat.CoreAdapter
import pl.weblang.omegat.Menu
import pl.weblang.omegat.SelectionAdapter
import pl.weblang.persistence.ExactHitsRepository
import pl.weblang.persistence.MissingGlossaryEntryRepository
import pl.weblang.persistence.WildcardHitsRepository

/**
 * Main plugin class
 */
class Weblang {

    private val exactHitsRepository = ExactHitsRepository()
    private val suggestionsRepository = WildcardHitsRepository()
    private val missingGlossaryEntryRepository = MissingGlossaryEntryRepository()
    private val adapter: ViewModel<ExactHitVO> by lazy { ExactHitsViewModel(exactHitsRepository) }
    private val instantSearchPaneController: InstantSearchPaneController by lazy {
        InstantSearchPaneController(
                CoreAdapter.mainWindow)
    }
    private val menu: Menu by lazy { Menu(CoreAdapter.mainWindow, adapter, missingGlossaryEntryRepository) }
    private val integrationSettings = IntegrationSettings()
    private val integrationManager = IntegrationManager(integrationSettings)
    private val backgroundService: BackgroundService by lazy {
        BackgroundService(integrationManager.verifierIntegrations(),
                          missingGlossaryEntryRepository, exactHitsRepository, suggestionsRepository, CoreAdapter)
    }
    private val selectionHandler = SelectionAdapter(CoreAdapter)
    private val instantSearchService: InstantSearchService by lazy {
        InstantSearchService(WebIntegrationController(integrationManager.instantSearchIntegrations()),
                             selectionHandler, instantSearchPaneController)
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
                setupMenu()
                startInstantSearchService()
                startBackgroundVerifierService()
            }
            IProjectEventListener.PROJECT_CHANGE_TYPE.LOAD -> {
                setupMenu()
                startInstantSearchService()
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
        }

        override fun onApplicationShutdown() {
        }
    }

    private fun startBackgroundVerifierService() {
        backgroundService.start()
    }

    private fun startInstantSearchService() {
        instantSearchService.start()
    }

    private fun setupMenu() {
        menu.initializeMenu()
    }

}

