package cc.zengtian.mtt.controller

import cc.zengtian.mtt.config.IQuizConfig
import cc.zengtian.mtt.util.Storage

/**
 * Created by ZengTian on 2019/10/26.
 */
abstract class BaseQuizConfigController<T : IQuizConfig> {
    abstract val config: T
    var questionCount
        get() = config.questionCount
        set(value) {
            config.questionCount = value
        }
    var timeout
        get() = config.timeout
        set(value) {
            config.timeout = value
        }
    var correctDelay
        get() = config.correctDelay
        set(value) {
            config.correctDelay = value
        }
    var wrongDelay
        get() = config.wrongDelay
        set(value) {
            config.wrongDelay = value
        }

    abstract fun saveUiToConfig()

    inline fun <reified C : T> save(config: C) {
        Storage.saveByClassName(config)
    }
}