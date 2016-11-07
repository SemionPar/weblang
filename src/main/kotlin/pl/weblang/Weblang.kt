package pl.weblang

import org.omegat.core.Core
import org.omegat.core.CoreEvents
import org.omegat.core.events.IApplicationEventListener
import org.omegat.core.events.IProjectEventListener
import pl.weblang.gui.WeblangPane

class Weblang {

    init {
        CoreEvents.registerApplicationEventListener(createIApplicationEventListener())
        CoreEvents.registerProjectChangeListener(createIProjectEventListener())
    }


    private fun createIProjectEventListener(): IProjectEventListener = object : IProjectEventListener {
        override fun onProjectChanged(eventType: IProjectEventListener.PROJECT_CHANGE_TYPE) {
            when (eventType) {
                IProjectEventListener.PROJECT_CHANGE_TYPE.CLOSE -> TODO()
                IProjectEventListener.PROJECT_CHANGE_TYPE.COMPILE -> {
                }
                IProjectEventListener.PROJECT_CHANGE_TYPE.CREATE -> {
                }
                IProjectEventListener.PROJECT_CHANGE_TYPE.LOAD -> TODO()
                IProjectEventListener.PROJECT_CHANGE_TYPE.SAVE -> {
                }
                IProjectEventListener.PROJECT_CHANGE_TYPE.MODIFIED -> {
                }
            }
        }
    }

    private fun createIApplicationEventListener(): IApplicationEventListener = object : IApplicationEventListener {
        override fun onApplicationStartup() {
            val mainWindow = Core.getMainWindow().apply { lockUI() }
            WeblangPane(mainWindow)
            mainWindow.unlockUI()
        }

        override fun onApplicationShutdown() {

        }
    }

}
