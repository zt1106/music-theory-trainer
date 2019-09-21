package cc.zengtian.mtt.controller

import cc.zengtian.mtt.config.ScaleQuizConfig
import cc.zengtian.mtt.model.theory.Key
import cc.zengtian.mtt.model.theory.Scale
import cc.zengtian.mtt.type.CheckBoxValue
import cc.zengtian.mtt.type.data
import cc.zengtian.mtt.type.selected
import cc.zengtian.mtt.util.Storage
import cc.zengtian.mtt.util.StorageKeys

/**
 * Created by ZengTian on 2019/9/20.
 */
class ScaleQuizController {
    private val config: ScaleQuizConfig by lazy {
        Storage.getByClassName() ?: ScaleQuizConfig().apply { Storage.saveByClassName(this) }
    }

    val keyValues: List<CheckBoxValue<Key>> by lazy {
        val selected = config.selectedKeys
        Key.values().map { CheckBoxValue(it, selected.contains(it.name)) }
    }

    val scaleValues: List<CheckBoxValue<Scale>> by lazy {
        val selected = config.selectedScales
        val userAdded = Storage.getList<Scale>(StorageKeys.USER_ADDED_SCALES.name)
        val scales = Scale.builtInValues().toMutableList().apply { addAll(userAdded) }
        scales.map { CheckBoxValue(it, selected.contains(it.name)) }
    }

    fun save() {
        val selectedKeys = keyValues.filter { it.selected }.map { it.data.name }.toSet()
        val selectedScales = scaleValues.filter { it.selected }.map { it.data.name }.toSet()
        val config = ScaleQuizConfig(selectedKeys, selectedScales)
        Storage.saveByClassName(config)
    }
}