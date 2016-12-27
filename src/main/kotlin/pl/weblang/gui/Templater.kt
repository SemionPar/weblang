package pl.weblang.gui

import org.thymeleaf.TemplateEngine
import org.thymeleaf.context.Context
import org.thymeleaf.templatemode.TemplateMode
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver
import pl.weblang.instant.InstantSearchResults

class Templater {

    val templateEngine: TemplateEngine = TemplateEngine().apply {
        addTemplateResolver(ClassLoaderTemplateResolver(javaClass.classLoader).apply {
            order = 1
            resolvablePatterns = setOf("templates/*")
            templateMode = TemplateMode.HTML
            isCacheable = false
        })
    }
    val webInstantSearchResultTemplateName = "templates/InstantSearchResults.html"

    fun process(results: InstantSearchResults): String {
        return templateEngine.process(webInstantSearchResultTemplateName, Context().apply {
            setVariables(mapOf(Pair("results", results)))
        })
    }
}
