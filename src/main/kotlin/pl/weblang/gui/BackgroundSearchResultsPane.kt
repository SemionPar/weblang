package pl.weblang.gui

import mu.KLogging
import pl.weblang.persistence.DirectHitsRepository
import java.awt.Dimension
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import javax.swing.JFrame
import javax.swing.JScrollPane
import javax.swing.JTable

class BackgroundSearchResultsPane(private val directHitsRepository: DirectHitsRepository) : JFrame(), ActionListener {
    companion object : KLogging()

    override fun actionPerformed(e: ActionEvent?) {
        minimumSize = Dimension(650, 400)
        isVisible = true
        val table = JTable(SourceSearchTableModel(directHitsRepository))
        table.preferredScrollableViewportSize = Dimension(500, 70)
        table.fillsViewportHeight = true
        add(JScrollPane(table))
        logger.info { "Background search results pane visible" }
    }
}