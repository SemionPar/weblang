package pl.weblang

import org.omegat.core.CoreEvents
import org.omegat.core.data.IProject
import org.omegat.core.data.ProjectProperties
import org.omegat.core.events.IEntryEventListener
import org.omegat.gui.editor.IEditor
import org.omegat.gui.main.IMainWindow
import java.io.File

object CoreAdapter {
    val editor: IEditor get() = org.omegat.core.Core.getEditor()
    val mainWindow: IMainWindow get() = org.omegat.core.Core.getMainWindow()
    val project: IProject get() = org.omegat.core.Core.getProject()
    fun registerEntryEventListener(listener: IEntryEventListener) {
        CoreEvents.registerEntryEventListener(listener)
    }
}

val ProjectProperties.weblangDir: File
    get() {
        val weblangDirectory = File(CoreAdapter.project.projectProperties.projectRootDir, "weblang")
        if (!weblangDirectory.exists()) weblangDirectory.mkdir()
        return weblangDirectory
    }

