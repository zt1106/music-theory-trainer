package cc.zengtian.mtt.util

import javafx.beans.property.SimpleBooleanProperty
import javafx.event.EventTarget
import javafx.scene.control.CheckBox
import tornadofx.checkbox

/**
 * Created by ZengTian on 2019/9/22.
 */
fun EventTarget.checkbox(text: String? = null, initValue: Boolean = false, op: CheckBox.() -> Unit = {}) {
    checkbox(text, SimpleBooleanProperty(initValue), op)
}