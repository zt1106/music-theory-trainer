package cc.zengtian.mtt.util

import javafx.beans.property.SimpleBooleanProperty
import javafx.event.EventTarget
import javafx.scene.control.CheckBox
import javafx.scene.control.Tab
import javafx.scene.control.TabPane
import tornadofx.UIComponent
import tornadofx.checkbox
import tornadofx.tab

/**
 * Created by ZengTian on 2019/9/22.
 */
fun EventTarget.checkbox(text: String? = null, initValue: Boolean = false, op: CheckBox.() -> Unit = {}) {
    checkbox(text, SimpleBooleanProperty(initValue), op)
}

/**
 * non closable tab with title
 */
inline fun <reified T : UIComponent> TabPane.fixedtab(title: String, noinline op: Tab.() -> Unit = {}) : Tab {
    val tab = tab<T>(op)
    tab.apply {
        textProperty().simpleBind(title)
        closableProperty().simpleBind(false)
    }
    return tab
}
