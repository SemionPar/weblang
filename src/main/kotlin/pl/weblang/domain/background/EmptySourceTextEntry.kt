package pl.weblang.domain.background

import org.omegat.core.data.SourceTextEntry

/**
 * Singleton to abstract out SourceTextEntry null object
 */
object EmptySourceTextEntry {
    val instance = SourceTextEntry(null, 0, null, "", emptyList())
}
