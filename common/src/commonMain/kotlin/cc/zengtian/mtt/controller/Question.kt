package cc.zengtian.mtt.controller

/**
 * Created by ZengTian on 10/14/2019.
 */
interface Question {
    var answer: Any?
    val correct: Boolean
    val answered: Boolean
}
