package pl.weblang.gui

import pl.weblang.CoreAdapter

/**
 * Abstraction over selection
 */
class SelectionAdapter(coreAdapter: CoreAdapter) {
    val selection: String? by SelectionDelegate(coreAdapter)
}

