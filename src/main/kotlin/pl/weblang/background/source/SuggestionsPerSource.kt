package pl.weblang.background.source

import pl.weblang.integration.file.ResourceFile

data class SuggestionsPerSource(val matches: List<Match<String>>, val file: ResourceFile)
