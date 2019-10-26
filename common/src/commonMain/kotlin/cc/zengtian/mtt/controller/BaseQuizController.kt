package cc.zengtian.mtt.controller

import cc.zengtian.mtt.config.IQuizConfig
import cc.zengtian.mtt.util.Property
import cc.zengtian.mtt.util.Time
import cc.zengtian.mtt.util.median

/**
 * Created by ZengTian on 10/14/2019.
 */
abstract class BaseQuizController<Q : Question> {

    private val properties: MutableList<Property<*>> = mutableListOf()

    abstract val config: IQuizConfig

    abstract val curQuestionProp : MyProperty<Q>
    abstract var curQuestion: Q

    val answeredCountProp = MyProperty(0)
    var answeredCount by answeredCountProp
        private set

    val correctCountProp = MyProperty(0)
    var correctCount by correctCountProp
        private set

    private var timestamp = Time.currentMillisecond()
    private var usedTimeList: MutableList<Long> = mutableListOf()
    val usedTimeMedianProp = Property<Long>(0)
    var usedTimeMedian by usedTimeMedianProp

    fun answer(answer: Any) {
        if (!curQuestion.answered) {
            answeredCount++
            curQuestion.answer = answer
            if (curQuestion.correct) {
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
        timestamp = Time.currentMillisecond()
        return true
    }

    abstract fun generateQuestion(): Q

    fun notifyAllPropsWithOldValue() {
        properties.forEach { it.notifyAllWithOldValue() }
    }

    inner class MyProperty<T>(t: T) : Property<T>(t) {
        init {
            properties.add(this)
        }
    }
}
