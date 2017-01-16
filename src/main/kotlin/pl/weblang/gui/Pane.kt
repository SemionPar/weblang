package pl.weblang.gui

import mu.KLogging
import org.omegat.core.data.SourceTextEntry
import org.omegat.gui.common.EntryInfoThreadPane
import org.omegat.gui.main.DockableScrollPane
import org.omegat.gui.main.IMainWindow
import org.omegat.util.gui.IPaneMenu
import org.omegat.util.gui.StaticUIUtils
import pl.weblang.instant.InstantSearchResults
import java.awt.Desktop
import java.awt.Dimension
import javax.swing.JComponent
import javax.swing.JPopupMenu
import javax.swing.KeyStroke
import javax.swing.event.HyperlinkEvent


val templater: Templater by lazy { Templater() }

class Pane(val mainWindow: IMainWindow) : EntryInfoThreadPane<Pane>(true), IPaneMenu {
    companion object : KLogging()

    val title = "Weblang"
    val key = "WEBLANG"
    val dockableScrollPane: DockableScrollPane = DockableScrollPane(key, title, this, true)

    override fun populatePaneMenu(menu: JPopupMenu?) {
    }

    override fun startSearchThread(p0: SourceTextEntry?) {
    }

    override fun setFoundResult(p0: SourceTextEntry?, p1: Pane?) {

    }

    fun initializeGui() {
        isEditable = false
        StaticUIUtils.makeCaretAlwaysVisible(this)
        minimumSize = Dimension(100, 200)
        contentType = "text/html"
        addHyperlinkListener { event ->
            run {
                when {
                    HyperlinkEvent.EventType.ACTIVATED == event.eventType -> {
                        Desktop.getDesktop().browse(event.url.toURI())
                    }
                    else -> {
                    }
                }
            }
        }
        logger.info { "Weblang pane initiated" }
    }

    fun addDockable() {
        mainWindow.addDockable(dockableScrollPane)
        isVisible = true
    }

    fun assignKeyBinding(keyAction: KeyAction) {
        pl.weblang.gui.KeyBinding(getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW), actionMap, keyAction)
    }

    fun displayInstantSearchResults(results: InstantSearchResults) {
        val html = templater.process(results)
        logger().debug { html }
        text = html
    }
}

data class KeyAction(val action: () -> Unit, val keyStrokeAndIdPair: Pair<KeyStroke, String>)

