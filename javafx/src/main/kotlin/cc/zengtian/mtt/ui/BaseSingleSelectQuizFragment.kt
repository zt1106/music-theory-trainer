package cc.zengtian.mtt.ui

import cc.zengtian.mtt.controller.BaseQuizController
import cc.zengtian.mtt.controller.Question
import javafx.scene.Parent
import javafx.scene.control.Alert
import javafx.scene.control.ButtonType
import javafx.scene.layout.Region
import javafx.scene.paint.Color
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.javafx.JavaFx
import kotlinx.coroutines.launch
import tornadofx.*

/**
 * Created by ZengTian on 10/15/2019.
 */
abstract class BaseSingleSelectQuizFragment<C : BaseQuizController<Q>, Q : Question> : BaseHeaderQuizFragment<C>() {

    override fun createQuizBody() = vbox {
        add(createQuizCenter())
        flowpane {
            ctr.curQuestionProp.addListener { question ->
                children.clear()
                generateOptionButtons(question).forEach { wrapper ->
                    add(wrapper.button.apply {
                        style {
                            fontSize = 30.px
                            borderWidth += box(5.px)
                            borderColor += box(Color.TRANSPARENT)
                        }
                        if (wrapper.answer == question.correctAnswer) {
                            question.answerProp.addListener {
                                style(true) {
                                    borderColor += box(Color.GREEN)
                                }
                            }
                        }
                        setOnAction {
                            if (ctr.curQuestion.answered) {
                                return@setOnAction
                            }
                            ctr.answer(wrapper.answer)
                            GlobalScope.launch(Dispatchers.JavaFx) {
                                if (ctr.curQuestion.correct) {
                                    delay(500)
                                } else {
                                    style(true) {
                                        borderColor += box(Color.RED)
                                    }
                                    delay(5000)
                                }
                                val hasNext = ctr.nextQuestion()
                                if (!hasNext) {
                                    val alert = Alert(Alert.AlertType.INFORMATION, "Quiz Finished!", ButtonType.OK)
                                    alert.dialogPane.minHeight = Region.USE_PREF_SIZE
                                    alert.setOnHidden {
                                        replaceWith<ScaleNoteQuizConfigView>()
                                    }
                                    alert.show()
                                }
                            }
                        }
                    })
                }
            }
        }
    }

    abstract fun createQuizCenter(): Parent

    abstract fun generateOptionButtons(question: Q): List<ButtonWithAnswer>
}
