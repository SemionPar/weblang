package pl.weblang.integration.file

import pl.weblang.background.Fragment
import pl.weblang.background.source.ExactHit
import pl.weblang.background.source.WildcardHit
import pl.weblang.integration.VerifierServiceProvider

class FileServiceProvider(fileManager: FileManager, override val name: String) : VerifierServiceProvider {

    private val files: List<ResourceFile> = fileManager.resourceFiles

    override fun findWildcardHits(fragment: Fragment): WildcardHit {

        val hits: WildcardHit = WildcardHit(this)
        val wordSequence = fragment.wordSequence

        for (file in files) {
            val match = file.containsWithWildcards(wordSequence)
            hits.put(file.fileName(), match)
        }

        return hits
    }

    override fun findExactHits(fragment: Fragment): ExactHit {

        val hits: ExactHit = ExactHit(this)
        val wordSequence = fragment.wordSequence

        for (file in files) {
            val indexOfHit = file.containsInOrder(wordSequence)
            if (indexOfHit.isHit) {
                hits.put(file.fileName(), indexOfHit)
            }
        }

        return hits
    }
}
