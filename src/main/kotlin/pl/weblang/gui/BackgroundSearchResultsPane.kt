package pl.weblang.gui

import mu.KLogging
import pl.weblang.persistence.SegmentVerificationRepository
import java.awt.Dimension
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import javax.swing.JFrame
import javax.swing.JScrollPane
import javax.swing.JTable

class BackgroundSearchResultsPane(private val segmentVerificationRepository: SegmentVerificationRepository) : JFrame(), ActionListener {
    companion object : KLogging()

    override fun actionPerformed(e: ActionEvent?) {
        minimumSize = Dimension(100, 200)
        isVisible = true
//        layout = BoxLayout(this, BoxLayout.Y_AXIS)
        val table = JTable(SourceSearchTableModel(segmentVerificationRepository))
        table.preferredScrollableViewportSize = Dimension(500, 70)
        table.fillsViewportHeight = true
        add(JScrollPane(table))
        logger.info { "Background search results pane visible" }
    }
}
