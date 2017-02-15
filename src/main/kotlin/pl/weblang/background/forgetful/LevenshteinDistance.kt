package pl.weblang.background.forgetful

import org.apache.commons.lang.StringUtils

/**
 * Calculates Levenshtein distance between segment translation and glossary entry in TL
 */
data class LevenshteinDistance(val segmentTranslation: String, val glossaryEntry: String) {

    val distance: Double = StringUtils.getLevenshteinDistance(segmentTranslation, glossaryEntry).toDouble()
    val perfectEstimate: Double = (segmentTranslation.length - glossaryEntry.length).toDouble()

    /**
     * Check whether distance difference is in range with given floor and ceiling multipliers
     */
    fun fallsIntoRange(floorMultiplier: Double, ceilingMultiplier: Double): Boolean {
        val floorLimit = perfectEstimate * floorMultiplier
        val ceilingLimit = perfectEstimate * ceilingMultiplier
        return distance in (floorLimit..ceilingLimit)
    }

}
