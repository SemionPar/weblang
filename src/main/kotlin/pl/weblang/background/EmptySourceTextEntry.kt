package pl.weblang.background

import org.omegat.core.data.SourceTextEntry

object EmptySourceTextEntry {
    val instance = SourceTextEntry(null, 0, null, "", emptyList())
}
