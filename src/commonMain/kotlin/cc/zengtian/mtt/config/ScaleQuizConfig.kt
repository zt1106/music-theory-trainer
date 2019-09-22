package cc.zengtian.mtt.config

import cc.zengtian.mtt.model.theory.Key
import cc.zengtian.mtt.model.theory.Scale
import cc.zengtian.mtt.util.containsNot
import kotlinx.serialization.Serializable

/**
 * Created by ZengTian on 2019/9/21.
 */
@Serializable
data class ScaleQuizConfig (
    val selectedKeys: Set<String> = Key.values().filter {
        val excluded = listOf(Key.C, Key.G_FLAT, Key.C_FLAT, Key.C_SHARP)
        excluded.containsNot(it)
    }.map { it.name }.toSet(),
    val selectedScales: Set<String> = setOf(Scale.IONIAN.name),
    val questionCount: Int = 0,
    val selectedAnswerType: Set<ScaleQuestionAnswerType> = ScaleQuestionAnswerType.values().toSet(),
    val selectedNoteDisplayType: NoteDisplayType = NoteDisplayType.LATIN
)


enum class ScaleQuestionAnswerType {
    KEY, SCALE, NUM, NOTE
}

enum class NoteDisplayType {
    LATIN, STAFF
}