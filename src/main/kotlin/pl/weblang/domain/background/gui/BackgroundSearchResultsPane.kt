package pl.weblang.domain.background.gui

import mu.KLogging
import pl.weblang.domain.background.source.ExactHitVO
import java.awt.Dimension
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import javax.swing.JFrame
import javax.swing.JScrollPane
import javax.swing.JTable

/**
 * Pane displaying background search results
 */
class BackgroundSearchResultsPane(private val adapter: ViewModel<ExactHitVO>) : JFrame(), ActionListener {
    companion object : KLogging()

    override fun actionPerformed(e: ActionEvent?) {
        minimumSize = Dimension(650, 400)
        isVisible = true
        val table = JTable(TableModel(adapter))
        table.preferredScrollableViewportSize = Dimension(500, 70)
        table.fillsViewportHeight = true
        add(JScrollPane(table))
        logger.info { "Background search results pane visible" }
    }
}
