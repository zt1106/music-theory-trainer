package cc.zengtian.mtt.config

import cc.zengtian.mtt.config.CircleOf5thAnswerType.MAJOR_KEY
import cc.zengtian.mtt.config.CircleOf5thLocationType.RELATIVE
import kotlinx.serialization.Serializable

/**
 * Created by ZengTian on 10/12/2019.
 */
@Serializable
data class CircleOf5thConfig(
    val locationTypes: Set<CircleOf5thLocationType> = setOf(RELATIVE),
    val answerTypes: Set<CircleOf5thAnswerType> = setOf(MAJOR_KEY),
    val inversionType: CircleInversionType = CircleInversionType.NORMAL,
    val rotateType: CircleRotateType = CircleRotateType.FIXED,
    val keyDisplayType: KeyDisplayType = KeyDisplayType.LATIN,
    val enableCSharp: Boolean = false,
    val enableGFlat: Boolean = false,
    val enableCFlat: Boolean = false,
    override val questionCount: Int = 0,
    override val timeout: Long = 0
) : BaseQuizConfig {
}

enum class CircleInversionType {
    NORMAL, INVERSED, RANDOM
}

enum class CircleRotateType {
    FIXED, RANDOM
}

enum class CircleOf5thLocationType {
    RELATIVE,
    FIXED
}

enum class CircleOf5thAnswerType {
    MAJOR_KEY, MINOR_KEY, ACC_COUNT, EXTRA_ACC_NOTE
}
