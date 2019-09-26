package cc.zengtian.mtt.controller

import cc.zengtian.mtt.config.KeyDisplayType
import cc.zengtian.mtt.config.NoteDisplayType
import cc.zengtian.mtt.config.ScaleQuestionAnswerType
import cc.zengtian.mtt.config.ScaleQuizConfig
import cc.zengtian.mtt.theory.Key
import cc.zengtian.mtt.theory.Scale
import cc.zengtian.mtt.ui.CheckBoxModel
import cc.zengtian.mtt.ui.RadioButtonModel
import cc.zengtian.mtt.ui.data
import cc.zengtian.mtt.ui.selected
import cc.zengtian.mtt.util.Storage
import cc.zengtian.mtt.util.StorageKey

/**
 * Created by ZengTian on 2019/9/20.
 */
class ScaleNoteQuizConfigController {
    private val config: ScaleQuizConfig by lazy {
        Storage.getByClassName() ?: ScaleQuizConfig().apply { Storage.saveByClassName(this) }
    }

    val keyModels: List<CheckBoxModel<Key>> by lazy {
        val selected = config.keys
        Key.values().map { CheckBoxModel(it, selected.contains(it.name)) }
    }

    val scaleModels: List<CheckBoxModel<Scale>> by lazy {
        val selected = config.scales
        val userAdded = Storage.getList<Scale>(StorageKey.USER_ADDED_SCALES.name)
        val scales = Scale.builtInValues().toMutableList().apply { addAll(userAdded) }
        scales.map { CheckBoxModel(it, selected.contains(it.name)) }
    }

    val answerTypeModels: List<CheckBoxModel<ScaleQuestionAnswerType>> by lazy {
        val selected = config.answerTypes
        ScaleQuestionAnswerType.values().map { CheckBoxModel(it, selected.contains(it)) }
    }

    val noteDisplayModel: RadioButtonModel<NoteDisplayType> by lazy {
        RadioButtonModel(NoteDisplayType.values().toList(), config.noteDisplayType, NoteDisplayType::description)
    }

    val keyDisplayModel: RadioButtonModel<KeyDisplayType> by lazy {
        RadioButtonModel(KeyDisplayType.values().toList(), config.keyDisplayType, KeyDisplayType::description)
    }

    var questionCount = config.questionCount
        set(value) {
            field = if (value < 0) {
                0
            } else {
                value
            }
        }

    fun populateConfig(): ScaleQuizConfig {
        val selectedKeys = keyModels.filter { it.selected }.map { it.data.name }.toSet()
        val selectedScales = scaleModels.filter { it.selected }.map { it.data.name }.toSet()
        val selectedTypes = answerTypeModels.filter { it.selected }.map { it.data }.toSet()
        return ScaleQuizConfig(selectedKeys, selectedScales, questionCount, selectedTypes, noteDisplayModel.selected, keyDisplayModel.selected)
    }

    fun save(config: ScaleQuizConfig) {
        Storage.saveByClassName(config)
    }
}
