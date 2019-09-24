package cc.zengtian.mtt.ui

import cc.zengtian.mtt.controller.ScaleNoteQuizConfigController
import cc.zengtian.mtt.util.checkbox
import javafx.scene.Parent
import tornadofx.*

/**
 * Created by ZengTian on 2019/9/8.
 */
class ScaleNoteQuizConfigView : View() {
    private val controller = ScaleNoteQuizConfigController()
    override val root: Parent = vbox {
        hbox {
            vbox {
                label("select keys")
                controller.keyModels.forEach { model ->
                    checkbox(model.data.name, model.selected) {
                        setOnAction { model.selected = isSelected }
                    }
                }
            }
            vbox {
                label("select scales")
                controller.scaleModels.forEach { model ->
                    checkbox(model.data.name, model.selected) {
                        setOnAction { model.selected = isSelected }
                    }
                }
            }
            vbox {
                label("select question type")
                controller.answerTypeModels.forEach { model ->
                    checkbox(model.data.name, model.selected) {
                        setOnAction { model.selected = isSelected }
                    }
                }
            }
            vbox {
                label("select note display")
                togglegroup {
                    controller.noteDisplayModel.data.forEach { type ->
                        radiobutton(controller.noteDisplayModel.textGetter(type)) {
                            if (controller.noteDisplayModel.selected == type) {
                                isSelected = true
                            }
                            setOnAction { controller.noteDisplayModel.selected = type }
                        }
                    }
                }
            }
        }
        button("start") {
            setOnAction {
                controller.save()
                replaceWith<ScaleNoteQuizFragment>()
            }
        }
    }
}
