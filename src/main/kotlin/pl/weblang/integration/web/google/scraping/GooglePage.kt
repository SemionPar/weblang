package pl.weblang.integration.web.google.scraping

import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements

sealed class GooglePage {

    abstract fun hasNoResults(): Boolean
    abstract fun parseEntries(): List<Entry>
    abstract fun parseCount(): Long
    abstract val searchItemElements: List<Element>

    companion object {
        operator fun invoke(document: Document): GooglePage = when {
            document.isRegularPage() -> RegularGooglePage(
                    document)
            else -> throw UnknownPageType()
        }
    }

    class RegularGooglePage(val document: Document) : GooglePage() {

        override val searchItemElements: List<Element>
            get() {
                return document.select("div#rso .g")
            }

        override fun hasNoResults(): Boolean {
            val text = document.extract(selector = { document.select(".med .card-section") },
                    extractor = { document.text() })
            return text.contains("did not match any documents") || text.contains("No results found for ")
        }

        override fun parseEntries(): List<Entry> {
            return searchItemElements.map { handleEntryMapping(it) }.filterNotNull()
        }

        override fun parseCount(): Long {
            val countDiv = document.select("#resultStats")
            if (countDiv.emptyOrNoText) return 0
            return countDiv.text().parseFirstNumber()
        }

    }

    protected fun handleEntryMapping(it: Element): Entry? {
        return try {
            it.toEntry()
        } catch (e: ParseException) {
            null
        }
    }
}

private fun Element.toEntry(): Entry {
    val text = try {
        this.extract(selector = { this.select("span.st") }, extractor = { this.text() })
    } catch (e: SelectorException) {
        ""
    }
    val link = try {
        this.extract(selector = { this.select(".r a") }, extractor = { this.attr("href") })
    } catch (e: SelectorException) {
        ""
    }
    if ((text.isBlank()) and (link.isBlank())) throw ParseException("Empty entry, will be omitted") else return Entry(
            link,
            text)
}

private fun Element.extract(selector: (Element) -> Elements, extractor: (Elements) -> String): String {
    val selectedElements = selector.invoke(this)
    if (selectedElements.size == 0)
        throw SelectorException("No elements found with selector")
    try {
        val extraction = extractor.invoke(selectedElements)
        return extraction
    } catch (e: Exception) {
        GoogleParser.logger.info { e }
        throw ExtractionException("Could not use extractor on selected elements")
    }
}

class ExtractionException(message: String) : RuntimeException(message)

class SelectorException(message: String) : RuntimeException(message)

fun String.parseFirstNumber(): Long {
    val firstDigit: Char = this.find(Char::isDigit) ?: throw ParseException("No digit was found in the resultStats")
    val firstDigitIndex = indexOf(firstDigit, 0)
    val spaceAfterFirstDigit: Char = this.find(Char::isWhitespace) ?: throw ParseException(
            "No whitespace after digit was found in the resultStats")
    val spaceAfterFirstDigitIndex = indexOf(spaceAfterFirstDigit, firstDigitIndex)
    return this.substring(firstDigitIndex, spaceAfterFirstDigitIndex).filter(Char::isDigit).toLong()
}

private val Elements.emptyOrNoText: Boolean
    get() {
        return this.isEmpty() or !this.hasText()
    }

class ParseException(message: String) : RuntimeException(message)

data class Entry(val link: String, val text: String)

class UnknownPageType : RuntimeException()
