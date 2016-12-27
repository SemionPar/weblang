package pl.weblang.gui

import mu.KLogging
import java.awt.event.ActionEvent
import javax.swing.AbstractAction
import javax.swing.ActionMap
import javax.swing.InputMap

class KeyBinding(inputMap: InputMap, actionMap: ActionMap, val keyAction: KeyAction) {
    companion object : KLogging()

    init {
        inputMap.put(keyAction.keyStrokeAndIdPair.first, keyAction.keyStrokeAndIdPair.second)
        actionMap.put(keyAction.keyStrokeAndIdPair.second, object : AbstractAction() {
            override fun actionPerformed(e: ActionEvent) {
                keyAction.action()
                logger.info { "${keyAction.action} performed" }
            }
        })
    }
}
