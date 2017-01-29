package pl.weblang.gui

import org.omegat.gui.main.IMainWindow
import java.awt.event.KeyEvent
import javax.swing.JMenu

class Menu(val mainWindow: IMainWindow) {
    fun initializeMenu() {
        JMenu("Weblang").apply {
            accessibleContext.accessibleDescription = "Weblang plugin main menu"
            mnemonic = KeyEvent.VK_G
            mainWindow.applicationFrame.jMenuBar.add(this)
        }
    }
}
