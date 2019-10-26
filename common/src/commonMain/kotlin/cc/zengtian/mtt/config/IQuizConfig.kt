package cc.zengtian.mtt.config

/**
 * Created by ZengTian on 10/12/2019.
 */
interface IQuizConfig {
    var questionCount: Int
    var timeout: Long
    var correctDelay: Long
    var wrongDelay: Long
}
