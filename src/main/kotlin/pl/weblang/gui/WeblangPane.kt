package pl.weblang.gui

import org.omegat.core.data.SourceTextEntry
import org.omegat.gui.common.EntryInfoThreadPane
import org.omegat.gui.main.DockableScrollPane
import org.omegat.gui.main.IMainWindow
import org.omegat.util.gui.IPaneMenu
import org.omegat.util.gui.StaticUIUtils
import java.awt.Dimension
import javax.swing.JPopupMenu

class WeblangPane : EntryInfoThreadPane<WeblangPane>, IPaneMenu {
    val title = "Weblang"

    constructor(mainWindow: IMainWindow) : super(true) {
        isEditable = false
        StaticUIUtils.makeCaretAlwaysVisible(this)

        text = title
        minimumSize = Dimension(100, 50)

        mainWindow.addDockable(DockableScrollPane(title, title, this, true))
    }

    override fun populatePaneMenu(menu: JPopupMenu?) {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun startSearchThread(p0: SourceTextEntry?) {

    }

    override fun setFoundResult(p0: SourceTextEntry?, p1: WeblangPane?) {

    }

}
