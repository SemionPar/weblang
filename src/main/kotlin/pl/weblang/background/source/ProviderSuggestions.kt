package pl.weblang.background.source

import pl.weblang.integration.VerifierServiceProvider

data class ProviderSuggestions(val suggestionsPerSource: List<SuggestionsPerSource>,
                               val provider: VerifierServiceProvider)
