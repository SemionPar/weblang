package pl.weblang.gui

import org.thymeleaf.TemplateEngine
import org.thymeleaf.context.Context
import org.thymeleaf.templatemode.TemplateMode
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver
import pl.weblang.instant.InstantSearchResults

/**
 * Templater converts instant search results to HTML tables
 */
class Templater {

    private val webInstantSearchResultTemplate = "templates/InstantSearchResults.html"
    private val templatePattern = "templates/*"
    private val entityName = "results"

    val templateEngine: TemplateEngine = TemplateEngine().apply {
        addTemplateResolver(ClassLoaderTemplateResolver(javaClass.classLoader).apply {
            order = 1
            resolvablePatterns = setOf(templatePattern)
            templateMode = TemplateMode.HTML
            isCacheable = false
        })
    }

    /**
     * Process value object to HTML
     */
    fun generateHtml(results: InstantSearchResults): String {
        return templateEngine.process(webInstantSearchResultTemplate, Context().apply {
            setVariables(mapOf(Pair(entityName, results)))
        })
    }
}
