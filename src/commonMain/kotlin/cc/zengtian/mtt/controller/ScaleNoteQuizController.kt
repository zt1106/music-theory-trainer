package cc.zengtian.mtt.controller

import cc.zengtian.mtt.config.ScaleQuestionAnswerType
import cc.zengtian.mtt.config.ScaleQuizConfig
import cc.zengtian.mtt.theory.Key
import cc.zengtian.mtt.theory.Note
import cc.zengtian.mtt.theory.Scale
import cc.zengtian.mtt.ui.Underscore
import cc.zengtian.mtt.util.Property
import cc.zengtian.mtt.util.Storage
import kotlin.random.Random

/**
 * Created by ZengTian on 2019/9/22.
 */
class ScaleNoteQuizController {

    private val config: ScaleQuizConfig by lazy { Storage.getByClassName() ?: ScaleQuizConfig() }

    private val scales = Scale.builtInValues().filter { config.scales.contains(it.name) }

    private val keys = Key.values().filter { config.keys.contains(it.name) }

    val curQuestionProp = Property(generateQuestion())
    var curQuestion by curQuestionProp

    val answeredCountProp = Property(0)
    var answeredCount by answeredCountProp
        private set

    val correctCountProp = Property(0)
    var correctCount by correctCountProp
        private set

    val totalCountProp = Property(1)
    var totalCount by totalCountProp
        private set

    fun notifyAllProps() {
        curQuestion = curQuestion
        answeredCount = answeredCount
        correctCount = correctCount
        totalCount = totalCount
    }

    private fun generateQuestion(): ScaleQuestionModel {
        val scale = scales.random()
        val key = keys.random()
        // exclude 1 because too easy
        val num = Random.nextInt(2, scale.noteCount + 1)
        val scaleNotes = key.getNotesOfScale(scale)
        val note = scaleNotes[num - 1]
        val type = config.answerTypes.random()
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
    fun nextQuestion(): Boolean {
        if (totalCount == config.questionCount) {
            return false
        }
        totalCount++
        if (curQuestion.isAnswered()) {
            answeredCount++
        }
        if (curQuestion.isCorrect()) {
            correctCount++
        }
        curQuestion = generateQuestion()
        return true
    }

    inner class ScaleQuestionModel(
        private val num: Int,
        val key: Key,
        val scale: Scale,
        val note: Note,
        private val answerType: ScaleQuestionAnswerType
    ) {
        val answerProp = Property<Any?>(null)
        var answer: Any? by answerProp

        private val numOptions: List<Int> by lazy {
            val list = mutableListOf<Int>()
            repeat(scale.noteCount) {
                list.add(it + 1)
            }
            list
        }

        val options: List<Any> by lazy {
            when (answerType) {
                ScaleQuestionAnswerType.KEY -> keys
                ScaleQuestionAnswerType.SCALE -> scales
                ScaleQuestionAnswerType.NUM -> numOptions
                ScaleQuestionAnswerType.NOTE -> Note.values().toList()
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

        val questionModels: List<Any> by lazy {
            when (answerType) {
                ScaleQuestionAnswerType.KEY -> listOf("In", Underscore, "key,", scale, ",", note, "is the", num, "note?")
                ScaleQuestionAnswerType.SCALE -> listOf("In", key, Underscore, "scale,", note, "is the", num, "note?")
                ScaleQuestionAnswerType.NUM -> listOf(note, "is the", Underscore, "note in", key, scale)
                ScaleQuestionAnswerType.NOTE -> listOf("The", num, "note in", key, scale, "is?")
            }
        }

        fun isCorrect() = correctAnswer == answer
        fun isAnswered() = answer != null
    }
}

