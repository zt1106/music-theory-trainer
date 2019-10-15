package cc.zengtian.mtt.controller

import cc.zengtian.mtt.config.ScaleQuestionAnswerType
import cc.zengtian.mtt.config.ScaleQuizConfig
import cc.zengtian.mtt.controller.ScaleNoteQuizController.ScaleQuestionModel
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
class ScaleNoteQuizController : BaseQuizController<ScaleQuestionModel>() {

    override val config: ScaleQuizConfig by lazy { Storage.getByClassName() ?: ScaleQuizConfig() }

    private val scales = Scale.builtInValues().filter { config.scales.contains(it.name) }

    private val keys = Key.values().filter { config.keys.contains(it.name) }

    override val curQuestionProp = MyProperty(generateQuestion())
    override var curQuestion by curQuestionProp

    override fun generateQuestion(): ScaleQuestionModel {
        val scale = scales.random()
        val key = keys.random()
        // exclude 1 because too easy
        val num = Random.nextInt(2, scale.noteCount + 1)
        val scaleNotes = key.getNotesOfScale(scale)
        val note = scaleNotes[num - 1]
        val type = config.answerTypes.random()
        return ScaleQuestionModel(num, key, scale, note, type)
    }

    inner class ScaleQuestionModel(
        private val num: Int,
        val key: Key,
        val scale: Scale,
        val note: Note,
        private val answerType: ScaleQuestionAnswerType
    ) : Question {
        override val answerProp = Property<Any?>(null)
        override var answer: Any? by answerProp

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
        override val correctAnswer: Any by lazy {
            when (answerType) {
                ScaleQuestionAnswerType.KEY -> key
                ScaleQuestionAnswerType.SCALE -> scale
                ScaleQuestionAnswerType.NUM -> num
                ScaleQuestionAnswerType.NOTE -> note
            }
        }

        val questionBody: List<Any> by lazy {
            val numStr = when (num) {
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

        override val correct: Boolean
        get() = correctAnswer == answer

        override val answered: Boolean
        get() = answer != null
    }
}

