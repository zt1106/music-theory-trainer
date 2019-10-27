package cc.zengtian.mtt.controller

import cc.zengtian.mtt.config.IQuizConfig
import cc.zengtian.mtt.util.LazyAliasProperty
import cc.zengtian.mtt.util.Storage

/**
 * Created by ZengTian on 2019/10/26.
 */
abstract class BaseQuizConfigController<T : IQuizConfig> {
    abstract val config: T
    var questionCount by LazyAliasProperty { config::questionCount }
    var timeout by LazyAliasProperty { config::timeout }
    var correctDelay by LazyAliasProperty { config::correctDelay }
    var wrongDelay by LazyAliasProperty { config::wrongDelay }

    abstract fun saveUiToConfig()

    inline fun <reified C : T> save(config: C) {
        Storage.saveByClassName(config)
    }
}