package cc.zengtian.mtt.util

import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleStringProperty
import javafx.beans.value.ObservableValue

/**
 * Created by ZengTian on 2019/9/21.
 */
fun javafx.beans.property.Property<String>.simpleBind(init: String) {
    bind(SimpleStringProperty(init))
}

fun javafx.beans.property.Property<Boolean>.simpleBind(init: Boolean) {
    bind(SimpleBooleanProperty(init))
}

fun ObservableValue<*>.addSimpleListener(listener: () -> Unit) {
    addListener { _, _, _ -> listener() }
}
