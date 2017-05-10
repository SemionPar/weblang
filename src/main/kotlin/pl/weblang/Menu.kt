package pl.weblang

import org.omegat.gui.main.IMainWindow
import pl.weblang.background.forgetful.MissingGlossaryEntry
import pl.weblang.background.gui.BackgroundSearchResultsPane
import pl.weblang.background.gui.MissingGlossaryAlertsPane
import pl.weblang.background.gui.ViewModel
import pl.weblang.background.gui.WildcardHitsViewModel
import pl.weblang.background.source.ExactHitVO
import pl.weblang.persistence.MissingGlossaryEntryRepository
import java.awt.event.ActionEvent
import java.awt.event.KeyEvent
import javax.swing.JMenu
import javax.swing.JMenuItem
import javax.swing.KeyStroke

/**
 * Plugin menu
 */
class Menu(val mainWindow: IMainWindow,
           private val adapter: ViewModel<ExactHitVO>,
           private val missingGlossaryEntryRepository: MissingGlossaryEntryRepository) {

    /**
     * Setup menu items
     */
    fun initializeMenu() {
        val menu = JMenu("Weblang").apply {
            accessibleContext.accessibleDescription = "Weblang plugin main menu"
            mnemonic = KeyEvent.VK_G
            mainWindow.applicationFrame.jMenuBar.add(this)
        }
        menu.add(JMenuItem("Background search", KeyEvent.VK_B).apply {
            accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_1, ActionEvent.ALT_MASK)
            accessibleContext.accessibleDescription = "Show background search results"
            addActionListener(BackgroundSearchResultsPane(adapter))
        })
        menu.add(JMenuItem("Omitted glossary term alerts", KeyEvent.VK_G).apply {
            accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_1, ActionEvent.ALT_MASK)
            accessibleContext.accessibleDescription = "Show omitted glossary term alerts"
            val adapter: ViewModel<MissingGlossaryEntry> = WildcardHitsViewModel(missingGlossaryEntryRepository)
            addActionListener(MissingGlossaryAlertsPane(adapter, missingGlossaryEntryRepository))
        })

    }

}


