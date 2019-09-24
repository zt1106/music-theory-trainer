package cc.zengtian.mtt.controller

import cc.zengtian.mtt.config.ScaleQuestionAnswerType
import cc.zengtian.mtt.config.ScaleQuizConfig
import cc.zengtian.mtt.model.theory.Key
import cc.zengtian.mtt.model.theory.Note
import cc.zengtian.mtt.model.theory.Scale
import cc.zengtian.mtt.util.Property
import cc.zengtian.mtt.util.Storage
import kotlin.random.Random

/**
 * Created by ZengTian on 2019/9/22.
 */
class ScaleQuizController {

    private val config: ScaleQuizConfig by lazy { Storage.getByClassName() ?: ScaleQuizConfig() }

    private val scales = Scale.builtInValues().filter { config.selectedScales.contains(it.name) }

    private val keys = Key.values().filter { config.selectedKeys.contains(it.name) }

    val curQuestionProp = Property(generateQuestion())
    var curQuestion by curQuestionProp

    var answeredCount = 0
        private set

    var correctCount = 0
        private set

    var totalCount = 1
        private set

    private fun generateQuestion(): ScaleQuestionModel {
        val scale = scales.random()
        val key = keys.random()
        // exclude 1 because too easy
        val num = Random.nextInt(2, scale.noteCount + 1)
        val scaleNotes = key.getNotesOfScale(scale)
        val note = scaleNotes[num - 1]
        val type = config.selectedAnswerType.random()
        return ScaleQuestionModel(num, key, scale, note, type)
    }

    fun answer(answer: Any) {
        if (!curQuestion.isAnswered()) {
            curQuestion.answer = answer
        }
    }

    /**
     * @return has next
     */
    fun next(): Boolean {
        if (totalCount == config.questionCount) {
            return false
        }
        if (curQuestion.isAnswered()) {
            answeredCount++
        }
        if (curQuestion.isCorrect()) {
            correctCount++
        }
        curQuestion = generateQuestion()
        return true
    }
}

data class ScaleQuestionModel(
    val num: Int,
    val key: Key,
    val scale: Scale,
    val note: Note,
    val answerType: ScaleQuestionAnswerType
) {
    var answerProperty = Property<Any?>(null)
    var answer: Any? by answerProperty

    private val numOptions: List<Int> by lazy {
        val list = mutableListOf<Int>()
        repeat(scale.noteCount) {
            list.add(it + 1)
        }
        list
    }
    private val keyOptions: List<Key> by lazy { Key.values().toList() }
    private val scaleOptions: List<Scale> by lazy { Scale.builtInValues() }
    private val noteOptions: List<Note> by lazy { Note.values().toList() }

    val options: List<Any> by lazy {
        when (answerType) {
            ScaleQuestionAnswerType.KEY -> keyOptions
            ScaleQuestionAnswerType.SCALE -> scaleOptions
            ScaleQuestionAnswerType.NUM -> numOptions
            ScaleQuestionAnswerType.NOTE -> noteOptions
        }
    }

    @Suppress("IMPLICIT_CAST_TO_ANY")
    val correctAnswer: Any by lazy {
        when (answerType) {
            ScaleQuestionAnswerType.KEY -> key
            ScaleQuestionAnswerType.SCALE -> scale
            ScaleQuestionAnswerType.NUM -> num
            ScaleQuestionAnswerType.NOTE -> note
        }
    }

    val questionText: String by lazy {
        when (answerType) {
            ScaleQuestionAnswerType.KEY -> "$num $note $scale?"
            ScaleQuestionAnswerType.SCALE -> "$num $note $key?"
            ScaleQuestionAnswerType.NUM -> "$note $key $scale?"
            ScaleQuestionAnswerType.NOTE -> "$num $key $scale?"
        }
    }

    fun isCorrect() = correctAnswer == answer
    fun isAnswered() = answer != null
}

