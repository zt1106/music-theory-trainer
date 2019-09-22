package cc.zengtian.mtt.controller

import cc.zengtian.mtt.config.ScaleQuestionAnswerType
import cc.zengtian.mtt.config.ScaleQuizConfig
import cc.zengtian.mtt.model.theory.Key
import cc.zengtian.mtt.model.theory.Note
import cc.zengtian.mtt.model.theory.Scale
import cc.zengtian.mtt.util.Storage
import kotlin.random.Random

/**
 * Created by ZengTian on 2019/9/22.
 */
class ScaleQuizController {

    private val config: ScaleQuizConfig by lazy { Storage.getByClassName() ?: ScaleQuizConfig() }

    private val scales = Scale.builtInValues().filter { config.selectedScales.contains(it.name) }

    private val keys = Key.values().filter { config.selectedKeys.contains(it.name) }

    var cur: ScaleQuestionModel = generateQuestion()
        private set

    var answeredCount = 0
        private set

    var correctCount = 0
        private set

    var totalCount = 1
        private set

    private fun generateQuestion(): ScaleQuestionModel {
        val scale = scales.random()
        val key = keys.random()
        val num = Random.nextInt(1, scale.noteCount + 1)
        val scaleNotes = key.getNotesOfScale(scale)
        val note = scaleNotes[num - 1]
        val type = config.selectedAnswerType.random()
        return ScaleQuestionModel(num, key, scale, note, type)
    }

    /**
     * @return has next
     */
    fun next(): Boolean {
        if (totalCount == config.questionCount) {
            return false
        }
        if (cur.isAnswered()) {
            answeredCount++
        }
        if (cur.isCorrect()) {
            correctCount++
        }
        cur = generateQuestion()
        return true
    }
}

data class ScaleQuestionModel(
    val num: Int,
    val key: Key,
    val scale: Scale,
    val note: Note,
    val answerType: ScaleQuestionAnswerType,
    var userAnswer: Any? = null
) {
    val numOptions: List<Int> by lazy {
        val list = mutableListOf<Int>()
        repeat(scale.noteCount) {
            list.add(it + 1)
        }
        list
    }
    val keyOptions: List<Key> by lazy { Key.values().toList() }
    val scaleOptions: List<Scale> by lazy { Scale.builtInValues() }
    val noteOptions: List<Note> by lazy { Note.values().toList() }
    @Suppress("IMPLICIT_CAST_TO_ANY")
    val correctAnswer: Any by lazy {
        when (answerType) {
            ScaleQuestionAnswerType.KEY -> key
            ScaleQuestionAnswerType.SCALE -> scale
            ScaleQuestionAnswerType.NUM -> num
            ScaleQuestionAnswerType.NOTE -> note
        }
    }

    fun isCorrect() = correctAnswer == userAnswer
    fun isAnswered() = userAnswer != null
}
