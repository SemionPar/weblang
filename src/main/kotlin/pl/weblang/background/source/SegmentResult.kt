package pl.weblang.background.source

data class SegmentResult(val directHitResults: List<DirectHitResults>,
                         val suggestions: List<ProviderSuggestions> = emptyList())
