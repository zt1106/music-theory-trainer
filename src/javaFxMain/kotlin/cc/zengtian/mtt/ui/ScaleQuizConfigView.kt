package cc.zengtian.mtt.ui

import cc.zengtian.mtt.controller.ScaleQuizConfigController
import cc.zengtian.mtt.type.data
import cc.zengtian.mtt.type.selected
import javafx.beans.property.SimpleBooleanProperty
import javafx.scene.Parent
import tornadofx.*

/**
 * Created by ZengTian on 2019/9/8.
 */
class ScaleQuizConfigView : View() {
    private val controller = ScaleQuizConfigController()
    override val root: Parent = vbox {
        hbox {
            vbox {
                label("select keys")
                controller.keyModels.forEach { checkBoxValue ->
                    checkbox(checkBoxValue.data.name, SimpleBooleanProperty(checkBoxValue.selected)) {
                        setOnAction { checkBoxValue.selected = isSelected }
                    }
                }
            }
            vbox {
                label("select scales")
                controller.scaleModels.forEach { checkBoxValue ->
                    checkbox(checkBoxValue.data.name, SimpleBooleanProperty(checkBoxValue.selected)) {
                        setOnAction { checkBoxValue.selected = isSelected }
                    }
                }
            }
        }
        button("start") {
            setOnAction {
                controller.save()
                replaceWith<ScaleQuizFragment>()
            }
        }
    }
}
