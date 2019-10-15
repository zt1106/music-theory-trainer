package cc.zengtian.mtt.controller

import cc.zengtian.mtt.util.Property

/**
 * Created by ZengTian on 10/14/2019.
 */
interface Question {
    val answerProp: Property<Any?>
    var answer: Any?
    val correctAnswer: Any
    val correct: Boolean
    val answered: Boolean
}
