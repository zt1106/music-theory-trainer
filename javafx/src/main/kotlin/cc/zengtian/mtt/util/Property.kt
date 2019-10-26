package cc.zengtian.mtt.util

import javafx.beans.binding.DoubleBinding
import javafx.beans.property.DoubleProperty
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleDoubleProperty
import javafx.beans.property.SimpleStringProperty
import javafx.beans.value.ObservableDoubleValue
import javafx.beans.value.ObservableValue

/**
 * Created by ZengTian on 2019/9/21.
 */
fun javafx.beans.property.Property<String>.bindConstant(init: String) {
    bind(SimpleStringProperty(init))
}

fun javafx.beans.property.Property<Boolean>.bindConstant(init: Boolean) {
    bind(SimpleBooleanProperty(init))
}

fun ObservableValue<*>.addSimpleListener(listener: () -> Unit) {
    addListener { _, _, _ -> listener() }
}

fun minOf(vararg doubleProperties: ObservableDoubleValue) : DoubleProperty {
    require(doubleProperties.isNotEmpty())
    val result = SimpleDoubleProperty(doubleProperties.map { it.get() }.min()!!)
    for (p in doubleProperties) {
        if (p is DoubleBinding) {
            println(p.get())
        }
        p.addSimpleListener {
            result.set(doubleProperties.map { it.get() }.min()!!)
        }
    }
    return result
}
