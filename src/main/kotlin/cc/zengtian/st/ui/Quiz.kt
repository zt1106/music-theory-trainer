package cc.zengtian.st.ui

import cc.zengtian.st.model.Key
import cc.zengtian.st.model.Note
import cc.zengtian.st.model.Scale
import tornadofx.Fragment
import tornadofx.label
import tornadofx.vbox
import kotlin.random.Random

/**
 * Created by ZengTian on 2019/9/8.
 */
class ScaleNoteDetermineQuiz : Fragment() {

    private val model: ScaleNoteDetermineQuizModel by param()

    private val questionLabel = label()

    override val root = vbox {
        add(questionLabel)

    }


}

data class ScaleNoteDetermineQuizModel(
    private val questionCount: Int,
    private val selectedKeys: List<Key>,
    private val selectedScale: List<Scale>
) {
    private var curIdx: Int = 0

    private var correctCount: Int = 0

    val questionModels = generateQuestionModels()

    init {
        check(questionCount > 0)
        check(selectedKeys.isNotEmpty())
        check(selectedScale.isNotEmpty())
    }

    fun curQuestionModel(): ScaleNoteDetermineQuestionModel {
        return questionModels[curIdx]
    }

    private fun generateQuestionModels(): List<ScaleNoteDetermineQuestionModel> {
        val result = mutableListOf<ScaleNoteDetermineQuestionModel>()
        if (result.size < questionCount) {
            result.add(generateQuestionModel())
        }
        return result
    }

    private fun generateQuestionModel(): ScaleNoteDetermineQuestionModel {
        selectedKeys.random().let { key ->
            selectedScale.random().let { scale ->
                val idx = Random.nextInt(scale.getNoteCount() - 1)
                val note = key.getNotesOfScale(scale)[idx]
                return ScaleNoteDetermineQuestionModel(key, scale, idx, note)
            }
        }
    }
}

class ScaleNoteDetermineQuestionModel(
    val key: Key,
    val scale: Scale,
    val noteIdx: Int,
    val answer: Note
)