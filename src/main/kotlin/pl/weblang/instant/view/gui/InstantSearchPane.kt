package pl.weblang.instant.view.gui


import com.vlsolutions.swing.docking.Dockable
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.launch
import mu.KLogging
import org.omegat.gui.main.DockableScrollPane
import org.omegat.util.gui.IPaneMenu
import org.omegat.util.gui.StaticUIUtils
import pl.weblang.shortcut.KeyAction
import pl.weblang.shortcut.KeyBinding
import java.awt.Desktop
import java.awt.Dimension
import javax.swing.*
import javax.swing.event.HyperlinkEvent

private val initialPaneSize = Dimension(300, 200)

class InstantSearchPane(
        private var omegatPane: Dockable? = null,
        private val textPane: JTextPane = JTextPane(),
        private val title: String = "Weblang",
        private val key: String = "WEBLANG") : IPaneMenu {
    companion object : KLogging()

    private val progressLayerUI: ProgressLayerUI
    private val jLayer: JLayer<JTextPane>
    val parentPane: JPanel
    val displayedHtml: String? get() = textPane.text
    val inProgress: Boolean get() = progressLayerUI.isVisible

    private val desktop: Desktop?
        get() {
            val desktop = Desktop.getDesktop()
            return desktop
        }

    init {
        setupTextPane()
        progressLayerUI = ProgressLayerUI().apply { stop() }
        jLayer = JLayer(textPane, progressLayerUI).apply {
            setSize(initialPaneSize)
        }
        parentPane = JPanel().apply {
            minimumSize = Dimension(initialPaneSize)
            isVisible = true
            add(jLayer)
            size = initialPaneSize
        }
    }

    override fun populatePaneMenu(menu: JPopupMenu?) {
        /*todo: handle regular sensible menu interactions*/
    }

    fun addKeyBinding(keyAction: KeyAction) {
        val inputMap = parentPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
        val actionMap = parentPane.actionMap
        KeyBinding(inputMap, actionMap, keyAction)
    }

    fun displayHtml(text: String) {
        progressLayerUI.stop()
        textPane.isVisible = true
        textPane.text = text
    }

    fun showProgressAnimation() {
        textPane.isVisible = false
        progressLayerUI.start()
    }

    fun getOmegaTPane(): Dockable {
        return omegatPane ?: run {
            val dockableScrollPane = DockableScrollPane(key, title, parentPane, true)
            omegatPane = dockableScrollPane
            logger.info { "Dockable pane instantiated" }
            dockableScrollPane
        }
    }

    private fun setupTextPane() {
        textPane.apply {
            isEditable = false
            isVisible = true
            StaticUIUtils.makeCaretAlwaysVisible(this)
            minimumSize = Dimension(initialPaneSize)
            size = initialPaneSize
            contentType = "text/html"
            text = "Use Ctrl+G to run a search on selected text"
            addHyperlinkListener { event ->
                launch(CommonPool) {
                    when {
                        HyperlinkEvent.EventType.ACTIVATED == event.eventType -> {
                            try {
                                desktop?.browse(event.url.toURI())
                            } catch(exception: Exception) {
                                logger.error { "Error while opening link: ${exception.message}" }
                            }
                        }
                        else -> {
                        }
                    }
                }
            }
        }
    }
}

