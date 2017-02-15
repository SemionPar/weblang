package pl.weblang.gui

import mu.KLogging
import pl.weblang.background.forgetful.MissingGlossaryEntry
import pl.weblang.persistence.MissingGlossaryEntryRepository
import java.awt.BorderLayout
import java.awt.Dimension
import java.awt.FlowLayout
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import javax.swing.*

class MissingGlossaryAlertsPane(private val adapter: ViewModel<MissingGlossaryEntry>,
                                val missingGlossaryEntryRepository: MissingGlossaryEntryRepository) : JFrame(), ActionListener {
    companion object : KLogging()

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

        val data = mutableListOf<String>()
        missingGlossaryEntryRepository.retrieveAll().forEach {
            data.add("File: ${it.file}, Semment: File: ${it.segmentNumber}")
        }

        val list = JList(data.toTypedArray())

        list.addListSelectionListener { event ->
            if (event.valueIsAdjusting)
                return@addListSelectionListener
            logger.info { "Selected: " + list.selectedValue }

//            Core.getEditor().gotoFile()
        }
        contentPane.add(JScrollPane(list), BorderLayout.CENTER)
    }

}

