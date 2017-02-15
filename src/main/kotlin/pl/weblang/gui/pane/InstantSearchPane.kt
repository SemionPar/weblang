package pl.weblang.gui.pane

import kotlinx.coroutines.experimental.future.future
import mu.KLogging
import org.omegat.gui.main.DockableScrollPane
import org.omegat.gui.main.IMainWindow
import org.omegat.util.gui.IPaneMenu
import org.omegat.util.gui.StaticUIUtils
import pl.weblang.gui.KeyBinding
import pl.weblang.gui.Templater
import pl.weblang.instant.InstantSearchResults
import java.awt.Desktop
import java.awt.Dimension
import javax.swing.*
import javax.swing.event.HyperlinkEvent


val templater: Templater by lazy { Templater() }

class InstantSearchPane(val mainWindow: IMainWindow) : IPaneMenu {
    companion object : KLogging()

    private val textPane = JTextPane()
    private val graphicPane = JFrame()

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
        textPane.isEditable = false
        textPanegisVisible = true
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

    fun assignKeyBinding(keyAction: KeyAction) {
        KeyBinding(getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW), actionMap, keyAction)
    }

    fun clear() {
        text = ""
    }

    fun displayInstantSearchResults(results: InstantSearchResults) {

        val futureHtml = future {
            templater.generateHtml(results)
        }

        val loading = ImageIcon("img/ajax-loader.gif")
        add(JLabel("tupot", loading, JLabel.CENTER))

        if (!futureHtml.isCompletedExceptionally) {
            val html = futureHtml.join()
            logger.debug { html }
            text = html
        }
    }
}
