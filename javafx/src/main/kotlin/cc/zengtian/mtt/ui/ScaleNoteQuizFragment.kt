package cc.zengtian.mtt.ui

import cc.zengtian.mtt.controller.ScaleNoteQuizController
import cc.zengtian.mtt.controller.ScaleNoteQuizController.ScaleQuestionModel
import tornadofx.*

/**
 * Created by ZengTian on 2019/9/22.
 */
class ScaleNoteQuizFragment : BaseSingleSelectQuizFragment<ScaleNoteQuizController, ScaleQuestionModel>() {

    override fun createQuizCenter() = vbox {
        textflow {
            ctr.curQuestionProp.addListener { question ->
                children.clear()
                label(question.questionBody.joinToString(" ") { it.toString() }) {
                    style { fontSize = 40.px }
                }
                // TODO use Text instead of label
            }
        }
    }

    override fun generateOptionButtons(question: ScaleQuestionModel): List<ButtonWithAnswer> {
        return question.options.map { answer ->
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
            button(ansStr) to answer
        }
    }

    override fun createController() = ScaleNoteQuizController()

    override fun jumpToConfig() {
        replaceWith<ScaleNoteQuizConfigView>()
    }
}
