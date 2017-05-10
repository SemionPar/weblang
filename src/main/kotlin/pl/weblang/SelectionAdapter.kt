package pl.weblang

/**
 * Abstraction over selection
 */
class SelectionAdapter(coreAdapter: pl.weblang.CoreAdapter) {
    val selection: String? by SelectionDelegate(coreAdapter)
}

