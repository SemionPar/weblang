package pl.weblang.shortcut

/**
 * Associates action to a shortcut
 */
class KeyBinding(inputMap: javax.swing.InputMap, actionMap: javax.swing.ActionMap, val keyAction: KeyAction) {
    companion object : mu.KLogging()

    init {
        val shortcut = keyAction.shortcut
        val key = shortcut.key

        inputMap.put(shortcut.keyStroke, key)
        actionMap.put(key, object : javax.swing.AbstractAction() {
            override fun actionPerformed(e: java.awt.event.ActionEvent) {
                keyAction.action()
                pl.weblang.shortcut.KeyBinding.Companion.logger.info { "$keyAction performed" }
            }
        })
    }
}
