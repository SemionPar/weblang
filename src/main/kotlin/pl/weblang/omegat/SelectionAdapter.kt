package pl.weblang.omegat

/**
 * Abstraction over selection
 */
class SelectionAdapter(coreAdapter: pl.weblang.omegat.CoreAdapter) {
    val selection: String? by SelectionDelegate(coreAdapter)
}

