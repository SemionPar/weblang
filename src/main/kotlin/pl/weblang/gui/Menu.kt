package pl.weblang.gui

import org.omegat.gui.main.IMainWindow
import pl.weblang.persistence.SegmentVerificationRepository
import java.awt.event.ActionEvent
import java.awt.event.KeyEvent
import javax.swing.JMenu
import javax.swing.JMenuItem
import javax.swing.KeyStroke


class Menu(val mainWindow: IMainWindow, private val segmentVerificationRepository: SegmentVerificationRepository) {
    fun initializeMenu() {
        val menu = JMenu("Weblang").apply {
            accessibleContext.accessibleDescription = "Weblang plugin main menu"
            mnemonic = KeyEvent.VK_G
            mainWindow.applicationFrame.jMenuBar.add(this)
        }
        menu.add(JMenuItem("Background search", KeyEvent.VK_B).apply {
            accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_1, ActionEvent.ALT_MASK)
            accessibleContext.accessibleDescription = "Show background search results"
            addActionListener(BackgroundSearchResultsPane(segmentVerificationRepository))
        })

    }

}

