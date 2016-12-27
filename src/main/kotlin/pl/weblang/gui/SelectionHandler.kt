package pl.weblang.gui

import org.omegat.core.Core
import kotlin.reflect.KProperty

class SelectionHandler {

    val selection by SelectionDelegate()

}

class SelectionDelegate {
    operator fun getValue(thisRef: Any?, property: KProperty<*>): String? = Core.getEditor().selectedText
}
