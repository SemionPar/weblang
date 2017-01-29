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
import java.awt.event.KeyEvent
import javax.swing.KeyStroke


class Weblang {

    val pane: Pane by lazy { Pane(CoreAdapter.mainWindow) }
    val menu: Menu by lazy { Menu(CoreAdapter.mainWindow) }

    val integrationSettings = IntegrationSettings()
    val integrationManager = IntegrationManager(integrationSettings)
    val verifierService: VerifierService by lazy { VerifierService(integrationManager.verifierIntegrations()) }
    val selectionHandler = SelectionHandler()
    val instantSearchController: InstantSearchController by lazy {
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

