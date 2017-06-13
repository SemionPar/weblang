package pl.weblang.integration.file

import pl.weblang.domain.background.Fragment
import pl.weblang.domain.background.port.SearchServiceProvider
import pl.weblang.domain.background.source.ExactHit
import pl.weblang.domain.background.source.WildcardHit

class FileServiceProvider(fileManager: FileManager, override val name: String) : SearchServiceProvider {

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
