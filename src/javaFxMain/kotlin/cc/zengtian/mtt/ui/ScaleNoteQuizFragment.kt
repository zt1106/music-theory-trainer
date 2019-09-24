package cc.zengtian.mtt.ui

import cc.zengtian.mtt.controller.ScaleNoteQuizController
import cc.zengtian.mtt.util.CompositeProperty
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
class ScaleNoteQuizFragment : Fragment() {

    private val c = ScaleNoteQuizController()

    override val root = vbox {
        hbox {
            spacing = 30.0
            label {
                c.correctCountProp.addListener { count -> text = "Correct: $count" }
            }
            label {
                c.answeredCountProp.addListener { count -> text = "Answered: $count" }
            }
            label {
                CompositeProperty(c.answeredCountProp, c.correctCountProp).anyChanged {
                    val rate = String.format("%.2f", c.correctCount.toDouble() * 100 / c.answeredCount)
                    text = "Rate: $rate %"
                }
            }
        }
        label {
            c.curQuestionProp.addListener{ question -> text = question.questionText }
            style {
                fontSize = 20.px
            }
        }
        flowpane {
            c.curQuestionProp.addListener { question ->
                children.clear()
                question.options.forEach { answer ->
                    button(answer.toString()) {
                        style {
                            fontSize = 15.px
                            borderWidth += box(5.px)
                            borderColor += box(Color.TRANSPARENT)
                        }
                        if (answer == question.correctAnswer) {
                            question.answerProp.addListener {
                                style(true) {
                                    borderColor += box(Color.GREEN)
                                }
                            }
                        }
                        setOnAction {
                            if (c.curQuestion.isAnswered()) {
                                return@setOnAction
                            }
                            c.answer(answer)
                            GlobalScope.launch(Dispatchers.JavaFx) {
                                if (c.curQuestion.isCorrect()) {
                                    delay(500)
                                } else {
                                    style(true) {
                                        borderColor += box(Color.RED)
                                    }
                                    delay(2000)
                                }
                                c.next()
                            }
                        }
                    }
                }
            }
        }
        c.notifyAllProps()
    }
}
