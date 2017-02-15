package pl.weblang.gui

import mu.KLogging
import org.omegat.gui.main.DockableScrollPane
import org.omegat.gui.main.IMainWindow
import org.omegat.util.gui.IPaneMenu
import org.omegat.util.gui.StaticUIUtils
import pl.weblang.instant.InstantSearchResults
import java.awt.Desktop
import java.awt.Dimension
import javax.swing.JComponent
import javax.swing.JPopupMenu
import javax.swing.JTextPane
import javax.swing.KeyStroke
import javax.swing.event.HyperlinkEvent

val templater: Templater by lazy { Templater() }

class Pane(val mainWindow: IMainWindow) : JTextPane(), IPaneMenu {
    companion object : KLogging()

    val title = "Weblang"
    val key = "WEBLANG"
    private var dockableScrollPane: DockableScrollPane? = null

    override fun populatePaneMenu(menu: JPopupMenu?) {
    }

    private val desktop: Desktop?
        get() {
            val desktop = Desktop.getDesktop()
            return desktop
        }

    fun initializeGui() {
        isEditable = false
        isVisible = true
        dockableScrollPane?.isVisible = true
        StaticUIUtils.makeCaretAlwaysVisible(this)
        minimumSize = Dimension(100, 200)
        contentType = "text/html"
        addHyperlinkListener { event ->
            run {
                when {
                    HyperlinkEvent.EventType.ACTIVATED == event.eventType -> {
                        try {
                            desktop?.browse(event.url.toURI())
                        } catch(exception: Exception) {
                            logger.debug { "Error while opening link: ${exception.message}" }
                        }
                    }
                    else -> {
                    }
                }
            }
        }
        logger.info { "Weblang pane initiated" }
    }

    fun addDockable() {
        mainWindow.addDockable(DockableScrollPane(key, title, this, true).apply { dockableScrollPane = this })
        logger.info { "Weblang pane added as dockable" }
    }

    fun assignKeyBinding(keyAction: KeyAction) {
        pl.weblang.gui.KeyBinding(getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW), actionMap, keyAction)
    }

    fun displayInstantSearchResults(results: InstantSearchResults) {
        val html = templater.generateHtml(results)
        logger().debug { html }
        text = html
    }

}

data class KeyAction(val action: () -> Unit, val shortcut: Shortcut)

data class Shortcut(val keyStroke: KeyStroke, val key: String)

