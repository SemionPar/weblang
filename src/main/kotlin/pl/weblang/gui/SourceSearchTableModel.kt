package pl.weblang.gui

import javax.swing.table.AbstractTableModel

class SourceSearchTableModel(private val adapter: ExactHitsViewModel) : AbstractTableModel() {


    override fun getColumnCount() = adapter.getColumnCount()

    override fun getColumnName(column: Int) = adapter.getColumnName(column)

    override fun getRowCount() = adapter.getRowCount()

    override fun getValueAt(rowIndex: Int, columnIndex: Int) = adapter.getValueAt(rowIndex, columnIndex)

}
