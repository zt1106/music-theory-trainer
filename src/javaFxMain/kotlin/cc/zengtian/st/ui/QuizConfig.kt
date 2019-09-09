package cc.zengtian.st.ui

import cc.zengtian.mtt.model.theory.Key
import cc.zengtian.mtt.model.theory.Scale
import javafx.beans.property.BooleanProperty
import javafx.beans.property.SimpleBooleanProperty
import javafx.scene.Parent
import tornadofx.*

/**
 * Created by ZengTian on 2019/9/8.
 */
class QuestionConfigView : View() {
    private val controller: QuestionConfigController by inject()
    override val root: Parent = vbox {
        hbox {
            vbox {
                label("select keys")
                controller.keys.forEach {
                    add(checkbox(it.first.toString(), it.second))
                }
            }
            vbox {
                label("select scales")
                controller.scales.forEach {
                    add(checkbox(it.first.toString(), it.second))
                }
            }
        }
        button("start") {
            setOnAction { controller.start() }
        }
    }
}

class QuestionConfigController : Controller() {
    val keys = mutableListOf<Pair<Key, BooleanProperty>>().apply {
        val defaultNotSelected = listOf(Key.C, Key.G_FLAT, Key.C_FLAT, Key.C_SHARP)
        Key.values().forEach {
            if (defaultNotSelected.contains(it)) {
                this.add(Pair(it, SimpleBooleanProperty(false)))
            } else {
                this.add(Pair(it, SimpleBooleanProperty(true)))
            }
        }
    }
    val scales = mutableListOf<Pair<Scale, BooleanProperty>>().apply {
        Scale.builtInValues().forEach {
            if (it == Scale.IONIAN) {
                this.add(Pair(it, SimpleBooleanProperty(true)))
            } else {
                this.add(Pair(it, SimpleBooleanProperty(false)))
            }
        }
    }

    fun start() {
    }
}
