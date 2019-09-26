package cc.zengtian.mtt.ui

import cc.zengtian.mtt.controller.ScaleNoteQuizController
import cc.zengtian.mtt.util.CompositeProperty
import javafx.geometry.Pos
import javafx.scene.control.Alert
import javafx.scene.control.Alert.AlertType
import javafx.scene.control.ButtonType
import javafx.scene.layout.Priority
import javafx.scene.layout.Region
import javafx.scene.paint.Color
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.javafx.JavaFx
import kotlinx.coroutines.launch
import tornadofx.*


// TODO     id("no.tornado.fxlauncher") version "1.0.20"
// transform to standlone javafx module depends on jvm common and apply this plugin

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
                    text = "Rate: $rate%"
                }
            }
            label {
                c.usedTimeMedianProp.addListener { milli ->
                    text = "Time Median: ${String.format("%.2f", milli.toDouble() / 1000)}s"
                }
            }
            hbox {
                alignment = Pos.BASELINE_RIGHT
                hgrow = Priority.ALWAYS
                button("Quit").setOnAction {
                    replaceWith<ScaleNoteQuizConfigView>()
                }
            }
        }
        textflow {
            c.curQuestionProp.addListener { question ->
                children.clear()
                label(question.questionBody.map { it.toString() }.joinToString(" ")) {
                    style { fontSize = 40.px }
                }
                // TODO use Text instead of label
            }
        }
        flowpane {
            c.curQuestionProp.addListener { question ->
                children.clear()
                question.options.forEach { answer ->
                    val ansStr = if (answer is Int) {
                        when (answer) {
                            1 -> "1st"
                            2 -> "2nd"
                            3 -> "3rd"
                            else -> "${answer}th"
                        }
                    } else {
                        answer.toString()
                    }
                    button(ansStr) {
                        style {
                            fontSize = 30.px
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
                                    delay(5000)
                                }
                                val hasNext = c.nextQuestion()
                                if (!hasNext) {
                                    val alert = Alert(AlertType.INFORMATION, "Quiz Finished!", ButtonType.OK)
                                    alert.dialogPane.minHeight = Region.USE_PREF_SIZE
                                    alert.setOnHidden {
                                        replaceWith<ScaleNoteQuizConfigView>()
                                    }
                                    alert.show()
                                }
                            }
                        }
                    }
                }
            }
        }
        c.notifyAllProps()
    }
}
