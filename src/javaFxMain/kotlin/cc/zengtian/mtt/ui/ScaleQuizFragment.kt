package cc.zengtian.mtt.ui

import cc.zengtian.mtt.controller.ScaleQuizController
import javafx.scene.paint.Color
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.javafx.JavaFx
import kotlinx.coroutines.launch
import tornadofx.*

/**
 * Created by ZengTian on 2019/9/22.
 */
class ScaleQuizFragment : Fragment() {

    private val controller = ScaleQuizController()

    override val root = vbox {
        label {
            controller.curQuestionChangedListeners.add {question -> text = question.questionText }
            style {
                fontSize = 20.px
            }
        }
        flowpane {
            controller.curQuestionChangedListeners.add {question ->
                children.clear()
                question.options.forEach { answer ->
                    button(answer.toString()) {
                        style {
                            fontSize = 15.px
                            borderWidth += box(5.px)
                            borderColor += box(Color.TRANSPARENT)
                        }
                        if (answer == question.correctAnswer) {
                            question.answeredListeners.add {
                                style(true) {
                                    borderColor += box(Color.GREEN)
                                }
                            }
                        }
                        setOnAction {
                            if (controller.curQuestion.isAnswered()) {
                                return@setOnAction
                            }
                            controller.answer(answer)
                            GlobalScope.launch(Dispatchers.JavaFx) {
                                if (controller.curQuestion.isCorrect()) {
                                    delay(500)
                                } else {
                                    style(true) {
                                        borderColor += box(Color.RED)
                                    }
                                    delay(2000)
                                }
                                controller.next()
                            }
                        }
                    }
                }
            }
        }
        controller.notifyCurQuestionChanged()
    }
}
