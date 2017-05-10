package pl.weblang.shortcut

data class KeyAction(val action: () -> Unit, val shortcut: Shortcut)
