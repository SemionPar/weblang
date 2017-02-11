package pl.weblang.integration.file

import pl.weblang.background.Fragment
import pl.weblang.background.source.DirectHitResults
import pl.weblang.integration.VerifierServiceProvider

class FileServiceProvider(val fileManager: FileManager, override val name: String) : VerifierServiceProvider {

    override fun findWildcardHits(fragment: Fragment): List<SuggestionsPerSource> {
        val files = fileManager.resourceFiles
        return files.map {
            val suggestions = it.tokenizedToWords.containsWithWildcards(fragment.wordSequence)
            SuggestionsPerSource(suggestions, it)
        }
    }\

    override fun findExactHits(fragment: Fragment): DirectHitResults {

        val files = fileManager.resourceFiles
        val hits: DirectHitResults = DirectHitResults()
        val wordSequence = fragment.wordSequence

        for (file in files) {
            val indexOfHit = file.containsInOrder(wordSequence)
            if (indexOfHit != -1) {
                hits.put(file.fileName(), indexOfHit)
            }
        }

        return hits
    }
}
