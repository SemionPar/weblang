package pl.weblang

import org.omegat.core.data.SourceTextEntry
import org.omegat.gui.common.EntryInfoThreadPane
import org.omegat.gui.main.DockableScrollPane
import org.omegat.gui.main.IMainWindow
import org.omegat.util.gui.StaticUIUtils
import java.awt.Dimension

class WeblangPane : EntryInfoThreadPane<WeblangPane> {

    val title = "Weblang"

    constructor(mainWindow: IMainWindow) : super(true) {
        isEditable = false
        StaticUIUtils.makeCaretAlwaysVisible(this)

        text = title
        minimumSize = Dimension(100, 50)

        mainWindow.addDockable(DockableScrollPane(title, title, this, true))

    }

    override fun startSearchThread(p0: SourceTextEntry?) {

    }

    override fun setFoundResult(p0: SourceTextEntry?, p1: WeblangPane?) {

    }

}
