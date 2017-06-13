package pl.weblang.omegat

import org.omegat.core.CoreEvents
import org.omegat.core.data.IProject
import org.omegat.core.data.ProjectProperties
import org.omegat.core.events.IEntryEventListener
import org.omegat.gui.editor.IEditor
import org.omegat.gui.glossary.GlossaryManager
import org.omegat.gui.glossary.IGlossaries
import org.omegat.gui.main.IMainWindow
import java.io.File

/**
 * Adapter for Core OmegaT API
 */
object CoreAdapter {
    val editor: IEditor get() = org.omegat.core.Core.getEditor()
    val mainWindow: IMainWindow get() = org.omegat.core.Core.getMainWindow()
    val project: IProject get() = org.omegat.core.Core.getProject()
    val glossary: IGlossaries get() = org.omegat.core.Core.getGlossary()
    val glossaryManager: GlossaryManager get() = org.omegat.core.Core.getGlossaryManager()
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

val IProject.hasDatabaseFile: Boolean
    get() {
        return this.databaseFile.isFile
    }

val IProject.databaseFile: File
    get() {
        return File(this.projectProperties.weblangDir, "db.mv.db")
    }

val IProject.databaseName: String
    get() {
        return File(this.projectProperties.weblangDir, "db").absolutePath
    }

