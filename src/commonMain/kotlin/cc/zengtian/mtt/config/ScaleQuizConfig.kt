package cc.zengtian.mtt.config

import cc.zengtian.mtt.config.ScaleQuestionAnswerType.*
import cc.zengtian.mtt.theory.Key
import cc.zengtian.mtt.theory.Scale
import cc.zengtian.mtt.util.containsNot
import kotlinx.serialization.Serializable

/**
 * Created by ZengTian on 2019/9/21.
 */
@Serializable
data class ScaleQuizConfig (
    val keys: Set<String> = Key.values().filter {
        val excluded = listOf(Key.C, Key.G_FLAT, Key.C_FLAT, Key.C_SHARP)
        excluded.containsNot(it)
    }.map { it.name }.toSet(),
    val scales: Set<String> = setOf(Scale.MAJOR.name),
    val questionCount: Int = 0,
    val answerTypes: Set<ScaleQuestionAnswerType> = setOf(NOTE, NUM),
    val noteDisplayType: NoteDisplayType = NoteDisplayType.LATIN
) {
    init {
        require(keys.isNotEmpty()) {"keys can't be empty"}
        require(scales.isNotEmpty()) {"scales can't be empty"}
        require(answerTypes.isNotEmpty()) {"answer type can't be empty"}
        require(questionCount >= 0) {"question count can't be nagetive"}
        if (keys.size == 1) {
            require(answerTypes.containsNot(KEY)) {"need at least 2 keys"}
        }
        if (scales.size == 1) {
            require(answerTypes.containsNot(SCALE)) {"need at least 2 scales"}
        }
    }
}


enum class ScaleQuestionAnswerType {
    KEY, SCALE, NUM, NOTE
}

enum class NoteDisplayType {
    LATIN, STAFF
}
