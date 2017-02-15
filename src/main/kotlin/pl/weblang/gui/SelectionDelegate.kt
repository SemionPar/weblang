package pl.weblang.gui

import pl.weblang.CoreAdapter
import kotlin.reflect.KProperty

/**
 * Delegate for direct selection access
 */
class SelectionDelegate(private val coreAdapter: CoreAdapter) {
    operator fun getValue(thisRef: SelectionAdapter, property: KProperty<*>): String? = coreAdapter.editor.selectedText
}
