package cc.zengtian.mtt.controller

import cc.zengtian.mtt.model.theory.Key
import kotlin.properties.Delegates

/**
 * Created by ZengTian on 2019/9/20.
 */
class QuestionConfigController {
    val keyList : MutableList<Key> by Delegates.observable(mutableListOf(), { property, oldValue, newValue ->
    })
}