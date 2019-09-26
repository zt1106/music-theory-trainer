package cc.zengtian.mtt.controller

import cc.zengtian.mtt.config.ScaleQuestionAnswerType
import cc.zengtian.mtt.config.ScaleQuizConfig
import cc.zengtian.mtt.theory.Key
import cc.zengtian.mtt.theory.Note
import cc.zengtian.mtt.theory.Scale
import cc.zengtian.mtt.ui.Underscore
import cc.zengtian.mtt.util.Property
import cc.zengtian.mtt.util.Storage
import cc.zengtian.mtt.util.Time
import cc.zengtian.mtt.util.median
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

    private var timestamp = Time.currentMillisecond()

    private fun resetTimestamp() {
        timestamp = Time.currentMillisecond()
    }

    private var usedTimeList: MutableList<Long> = mutableListOf()

    val usedTimeMedianProp = Property<Long>(0)
    var usedTimeMedian by usedTimeMedianProp

    fun notifyAllProps() {
        curQuestion = curQuestion
        answeredCount = answeredCount
        correctCount = correctCount
        usedTimeMedian = usedTimeMedian
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
            answeredCount++
            curQuestion.answer = answer
            if (curQuestion.isCorrect()) {
                correctCount++
            }
            usedTimeList.add(Time.currentMillisecond() - timestamp)
            // TODO performance issue?
            usedTimeMedian = usedTimeList.median()
        }
    }

    /**
     * @return has next
     */
    fun nextQuestion(): Boolean {
        if (answeredCount == config.questionCount) {
            return false
        }
        curQuestion = generateQuestion()
        resetTimestamp()
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

        // also exclude 1 because no such questions
        private val numOptions: List<Int> by lazy {
            val list = mutableListOf<Int>()
            repeat(scale.noteCount - 1) {
                list.add(it + 2)
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

        val questionBody: List<Any> by lazy {
            val numStr = when(num) {
                1 -> "1st"
                2 -> "2nd"
                3 -> "3rd"
                else -> "${num}th"
            }

            when (answerType) {
                ScaleQuestionAnswerType.KEY -> listOf("In key", Underscore, scale, ",", note, "is the", numStr, "note?")
                ScaleQuestionAnswerType.SCALE -> listOf("In", key, Underscore, "scale,", note, "is the", numStr, "note?")
                ScaleQuestionAnswerType.NUM -> listOf(note, "is the", Underscore, "note in", key, scale)
                ScaleQuestionAnswerType.NOTE -> listOf("The", numStr, "note in", key, scale, "is?")
            }
        }

        fun isCorrect() = correctAnswer == answer
        fun isAnswered() = answer != null
    }
}

