package pl.weblang.gui.pane

import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.launch
import mu.KLogging
import org.omegat.gui.main.DockableScrollPane
import org.omegat.gui.main.IMainWindow
import org.omegat.util.gui.IPaneMenu
import org.omegat.util.gui.StaticUIUtils
import pl.weblang.gui.KeyAction
import pl.weblang.gui.KeyBinding
import java.awt.BorderLayout
import java.awt.Desktop
import java.awt.Dimension
import javax.swing.*
import javax.swing.event.HyperlinkEvent


class InstantSearchPane(val mainWindow: IMainWindow) : IPaneMenu {
    companion object : KLogging()

    private val textPane = JTextPane()
    private val graphicPane = JPanel()
    private val parentPane = JPanel()
    private var dockableScrollPane: DockableScrollPane? = null

    val title = "Weblang"
    val key = "WEBLANG"

    init {
        setupGraphicPane()
        setupTextPane()
        parentPane.apply {
            minimumSize = Dimension(300, 300)
            add(textPane, BorderLayout.CENTER)
            add(graphicPane, BorderLayout.CENTER)
            isVisible = true
        }
    }

    override fun populatePaneMenu(menu: JPopupMenu?) {
        /*todo: handle regular sensible menu interactions*/
    }

    private val desktop: Desktop?
        get() {
            val desktop = Desktop.getDesktop()
            return desktop
        }

    fun addKeyBinding(keyAction: KeyAction) {
        val inputMap = parentPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
        val actionMap = parentPane.actionMap
        KeyBinding(inputMap, actionMap, keyAction)
    }

    fun clear() {
        textPane.text = ""
        graphicPane.isVisible = false
    }

    fun displayText(text: String) {
        textPane.text = text
        showTextPane()
    }

    fun displayLoadingSpinner() {
        showGraphicPane()
    }

    fun addDockable() {
        if (dockableScrollPane == null) {
            mainWindow.addDockable(DockableScrollPane(key, title, parentPane, true).apply {
                dockableScrollPane = this
            })
        }
        dockableScrollPane?.isVisible = true
        logger.info { "Weblang pane added as dockable" }
    }

    private fun setupGraphicPane() {
        graphicPane.apply {
            val loadingSpinner = ImageIcon("img/ajax-loader.gif")
            isVisible = false
            minimumSize = Dimension(300, 300)
            val label = JLabel("luluz", loadingSpinner, JLabel.CENTER)
            add(label, BorderLayout.CENTER)
            label.isVisible = true
            label.minimumSize = Dimension(300, 300)
        }
    }

    private fun setupTextPane() {
        textPane.apply {
            isEditable = false
            isVisible = false
            StaticUIUtils.makeCaretAlwaysVisible(this)
            minimumSize = Dimension(300, 300)
            contentType = "text/html"
            addHyperlinkListener { event ->
                launch(CommonPool) {
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
        }
    }

    private fun showTextPane() {
        graphicPane.apply {
            isVisible = false
        }
        textPane.apply {
            isVisible = true
        }
    }

    private fun showGraphicPane() {
        textPane.apply {
            isVisible = false
        }
        graphicPane.apply {
            isVisible = true
        }
    }
}
