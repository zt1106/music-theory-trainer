package cc.zengtian.mtt.ui

import cc.zengtian.mtt.config.ScaleQuizConfig
import cc.zengtian.mtt.controller.ScaleNoteQuizConfigController
import cc.zengtian.mtt.util.addSimpleListener
import cc.zengtian.mtt.util.checkbox
import javafx.scene.Parent
import tornadofx.*

/**
 * Created by ZengTian on 2019/9/8.
 */
class ScaleNoteQuizConfigView : Fragment() {
    private val c = ScaleNoteQuizConfigController()
    override val root: Parent = vbox {
        hbox {
            spacing = 20.0
            vbox {
                label("Keys")
                c.keyModels.forEach { model ->
                    checkbox(model.data.toString(), model.selected) {
                        setOnAction { model.selected = isSelected }
                    }
                }
            }
            vbox {
                label("Scales")
                c.scaleModels.forEach { model ->
                    checkbox(model.data.toString(), model.selected) {
                        setOnAction { model.selected = isSelected }
                    }
                }
            }
            vbox {
                label("Question Type")
                c.answerTypeModels.forEach { model ->
                    checkbox(model.data.description, model.selected) {
                        setOnAction { model.selected = isSelected }
                    }
                }
                label("Question Count")
                textfield(c.questionCount.toString()) {
                    style {

                    }
                    prefWidth = 15.0
                    textProperty().addSimpleListener {
                        try {
                            c.questionCount = text.toInt()
                        } catch (e: Exception) {
                        }
                    }
                }
            }
            vbox {
                label("Note Display")
                togglegroup {
                    c.noteDisplayModel.data.forEach { type ->
                        radiobutton(c.noteDisplayModel.textGetter(type)) {
                            if (c.noteDisplayModel.selected == type) {
                                isSelected = true
                            }
                            setOnAction { c.noteDisplayModel.selected = type }
                        }
                    }
                }
                label("Key Display")
                togglegroup {
                    c.keyDisplayModel.data.forEach { type ->
                        radiobutton(c.keyDisplayModel.textGetter(type)) {
                            if (c.keyDisplayModel.selected == type) {
                                isSelected = true
                            }
                            setOnAction { c.keyDisplayModel.selected = type }
                        }
                    }
                }
            }
            children.style {
                fontSize = 15.px
            }
        }
        hbox {
            button("Start").setOnAction {
                c.saveUiToConfig()
                c.save(c.config)
                replaceWith<ScaleNoteQuizFragment>()
            }
            button("Reset").setOnAction {
                c.save(ScaleQuizConfig())
                replaceWith<ScaleNoteQuizConfigView>()
            }
            children.style {
                fontSize = 20.px
            }
        }
    }
}
