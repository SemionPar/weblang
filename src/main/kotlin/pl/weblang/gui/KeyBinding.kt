package pl.weblang.gui

import mu.KLogging
import pl.weblang.gui.pane.KeyAction
import java.awt.event.ActionEvent
import javax.swing.AbstractAction
import javax.swing.ActionMap
import javax.swing.InputMap

/**
 * Associates action to a shortcut
 */
class KeyBinding(inputMap: InputMap, actionMap: ActionMap, val keyAction: KeyAction) {
    companion object : KLogging()

    init {
        val shortcut = keyAction.shortcut
        val key = shortcut.key

        inputMap.put(shortcut.keyStroke, key)
        actionMap.put(key, object : AbstractAction() {
            override fun actionPerformed(e: ActionEvent) {
                keyAction.action()
                logger.info { "$keyAction performed" }
            }
        })
    }
}
