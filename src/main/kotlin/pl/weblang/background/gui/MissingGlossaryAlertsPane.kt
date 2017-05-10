package pl.weblang.background.gui

import com.google.common.collect.Lists
import mu.KLogging
import org.omegat.core.Core
import pl.weblang.background.forgetful.MissingGlossaryEntry
import pl.weblang.persistence.MissingGlossaryEntryRepository
import java.awt.BorderLayout
import java.awt.Dimension
import java.awt.FlowLayout
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import javax.swing.*

class MissingGlossaryAlertsPane(private val adapter: ViewModel<MissingGlossaryEntry>,
                                private val missingGlossaryEntryRepository: MissingGlossaryEntryRepository) : JFrame(), ActionListener {
    companion object : KLogging()

    private val entries: List<MissingGlossaryEntry> get() =
    Lists.newArrayList<MissingGlossaryEntry>(
            missingGlossaryEntryRepository.retrieveAll().iterator())

    override fun actionPerformed(e: ActionEvent?) {
        minimumSize = Dimension(800, 300)
        isVisible = true

        initTable()

        iniList()

        add(JButton("Go to segment"))

        logger.info { "MissingGlossaryAlertsPane visible" }
    }

    private fun initTable() {
        val table = JTable(TableModel(adapter))
        table.preferredScrollableViewportSize = Dimension(700, 200)
        table.fillsViewportHeight = true
        table.selectionModel.addListSelectionListener { }
        add(JScrollPane(table))
    }

    private fun iniList() {
        val contentPane = contentPane
        contentPane.layout = FlowLayout()

        val segmentLabels = mutableListOf<String>()
        entries.forEach {
            segmentLabels.add("File: ${it.file}, Semment: File: ${it.segmentNumber}")
        }

        val list = JList(segmentLabels.toTypedArray())

        list.addListSelectionListener { event ->
            if (event.valueIsAdjusting)
                return@addListSelectionListener
            logger.info { "Selected: " + list.selectedValue }

            val segment = Core.getEditor()
            val selectedEntry = entries[list.selectedIndex]
            segment.gotoFile(resolveFileIndexByName(selectedEntry.file))
            segment.gotoEntry(selectedEntry.segmentNumber)

        }
        contentPane.add(JScrollPane(list), BorderLayout.CENTER)
    }

    fun resolveFileIndexByName(fileName: String): Int {
        val projectFiles = Core.getProject().projectFiles
        val file = projectFiles.find { it.filePath.endsWith(fileName) }
        return projectFiles.indexOf(file)
    }

}

