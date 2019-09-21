package cc.zengtian.mtt.ui

import cc.zengtian.mtt.controller.ScaleQuizController
import cc.zengtian.mtt.type.data
import cc.zengtian.mtt.type.selected
import javafx.beans.property.SimpleBooleanProperty
import javafx.scene.Parent
import tornadofx.*

/**
 * Created by ZengTian on 2019/9/8.
 */
class QuestionConfigView : View() {
    private val controller = ScaleQuizController()
    override val root: Parent = vbox {
        hbox {
            vbox {
                label("select keys")
                controller.keyValues.forEach { checkBoxValue ->
                    checkbox(checkBoxValue.data.name, SimpleBooleanProperty(checkBoxValue.selected)) {
                        setOnAction { checkBoxValue.selected = isSelected }
                    }
                }
            }
            vbox {
                label("select scales")
                controller.scaleValues.forEach { checkBoxValue ->
                    checkbox(checkBoxValue.data.name, SimpleBooleanProperty(checkBoxValue.selected)) {
                        setOnAction { checkBoxValue.selected = isSelected }
                    }
                }
            }
        }
        button("start") {
            setOnAction { controller.save() }
        }
    }
}
