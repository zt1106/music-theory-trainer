package cc.zengtian.mtt.controller

import cc.zengtian.mtt.config.ScaleQuestionAnswerType
import cc.zengtian.mtt.config.ScaleQuizConfig
import cc.zengtian.mtt.model.theory.Key
import cc.zengtian.mtt.model.theory.Scale
import cc.zengtian.mtt.type.CheckBoxModel
import cc.zengtian.mtt.type.data
import cc.zengtian.mtt.type.selected
import cc.zengtian.mtt.util.Storage
import cc.zengtian.mtt.util.StorageKeys

/**
 * Created by ZengTian on 2019/9/20.
 */
class ScaleQuizConfigController {
    private val config: ScaleQuizConfig by lazy {
        Storage.getByClassName() ?: ScaleQuizConfig().apply { Storage.saveByClassName(this) }
    }

    val keyModels: List<CheckBoxModel<Key>> by lazy {
        val selected = config.selectedKeys
        Key.values().map { CheckBoxModel(it, selected.contains(it.name)) }
    }

    val scaleModels: List<CheckBoxModel<Scale>> by lazy {
        val selected = config.selectedScales
        val userAdded = Storage.getList<Scale>(StorageKeys.USER_ADDED_SCALES.name)
        val scales = Scale.builtInValues().toMutableList().apply { addAll(userAdded) }
        scales.map { CheckBoxModel(it, selected.contains(it.name)) }
    }

    val answerTypeModels: List<CheckBoxModel<ScaleQuestionAnswerType>> by lazy {
        val selected = config.selectedAnswerType
        ScaleQuestionAnswerType.values().map { CheckBoxModel(it, selected.contains(it)) }
    }

    var questionCount = config.questionCount

    private fun getModel(): ScaleQuizConfig {
        val selectedKeys = keyModels.filter { it.selected }.map { it.data.name }.toSet()
        val selectedScales = scaleModels.filter { it.selected }.map { it.data.name }.toSet()
        val selectedTypes = answerTypeModels.filter { it.selected }.map { it.data }.toSet()
        return ScaleQuizConfig(selectedKeys, selectedScales, questionCount, selectedTypes)
    }

    fun save() {
        Storage.saveByClassName(getModel())
    }
}