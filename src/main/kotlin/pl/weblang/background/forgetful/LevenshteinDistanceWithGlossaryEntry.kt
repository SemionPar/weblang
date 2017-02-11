package pl.weblang.background.forgetful

import org.omegat.gui.glossary.GlossaryEntry

data class LevenshteinDistanceWithGlossaryEntry(val levenshteinDistance: Int, val glossaryEntry: GlossaryEntry)
