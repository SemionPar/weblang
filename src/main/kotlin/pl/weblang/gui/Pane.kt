package pl.weblang.gui

import mu.KLogging
import org.omegat.core.data.SourceTextEntry
import org.omegat.gui.common.EntryInfoThreadPane
import org.omegat.gui.main.DockableScrollPane
import org.omegat.gui.main.IMainWindow
import org.omegat.util.gui.IPaneMenu
import org.omegat.util.gui.StaticUIUtils
import org.thymeleaf.TemplateEngine
import org.thymeleaf.context.Context
import org.thymeleaf.templatemode.TemplateMode
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver
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
        mainWindow.addDockable(DockableScrollPane(title, title, this, true))
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

    fun assignKeyBinding(keyAction: KeyAction) {
        pl.weblang.gui.KeyBinding(getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW), actionMap, keyAction)
    }

    fun displayInstantSearchResults(results: InstantSearchResults) {
        return Context().apply {
            setVariables(mapOf(Pair("totalCount", results.count),
                               Pair("entries", results.responses)))
        }.run { templater.process(this) }
    }
}

data class KeyAction(val action: () -> Unit, val keyStrokeAndIdPair: Pair<KeyStroke, String>)

class Templater {

    val templateEngine: TemplateEngine = TemplateEngine().apply {
        addTemplateResolver(ClassLoaderTemplateResolver().apply {
            order = 1
            resolvablePatterns = setOf("templates/*")
            suffix = ".html"
            templateMode = TemplateMode.HTML
            isCacheable = false
        })
    }
    val webInstantSearchResultTemplateName = "templates/InstantSearchResults"

    fun process(context: Context): String {
        return templateEngine.process(webInstantSearchResultTemplateName, context)
    }
}

