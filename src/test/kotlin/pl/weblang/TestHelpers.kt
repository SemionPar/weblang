package pl.weblang

import org.apache.commons.lang3.StringUtils


infix fun String?.`should be ignoring whitespace`(other: String) {
    if (this == null) throw AssertionError("Was null, expected $other")
    if (this.removeWhitespaceCharacters() != other.removeWhitespaceCharacters()) throw AssertionError(
            "Was $this, expected $other")
}

private fun String.removeWhitespaceCharacters(): String = StringUtils.deleteWhitespace(this)
