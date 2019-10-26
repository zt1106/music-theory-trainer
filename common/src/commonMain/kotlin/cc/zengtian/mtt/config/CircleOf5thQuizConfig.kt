package cc.zengtian.mtt.config

import cc.zengtian.mtt.config.CircleInversionType.NORMAL
import cc.zengtian.mtt.config.CircleNodeDisplayType.MAJOR_KEY
import cc.zengtian.mtt.config.CircleRotateType.RANDOM
import cc.zengtian.mtt.theory.CircleOfFifthsNode
import kotlinx.serialization.Serializable

/**
 * Created by ZengTian on 10/12/2019.
 */
@Serializable
data class CircleOf5thConfig(
        val nodeConfig: ChooseNodeConfig = ChooseNodeConfig(),
        val locationConfig: ChooseLocationConfig = ChooseLocationConfig(),
        val inversionType: CircleInversionType = NORMAL,
        val cSharpEnabled: Boolean = false,
        val gFlatEnabled: Boolean = false,
        val cFlatEnabled: Boolean = false,
        override var questionCount: Int = 0,
        override var timeout: Long = 0,
        override var correctDelay: Long = 500,
        override var wrongDelay: Long = 3000
) : IQuizConfig

@Serializable
data class ChooseNodeConfig(
    val rotateType: CircleRotateType = RANDOM,
    val referenceNodes: Set<String> = CircleOfFifthsNode.values().map { it.name }.toSet(),
    val referenceNodeDisplays: Set<CircleNodeDisplayType> = setOf(MAJOR_KEY),
    val questionNodeDisplays: Set<CircleNodeDisplayType> = setOf(MAJOR_KEY)
)

@Serializable
data class ChooseLocationConfig(
    val rotateType: CircleRotateType = RANDOM,
    val referenceNodes: Set<String> = CircleOfFifthsNode.values().map { it.name }.toSet(),
    val referenceNodeDisplays: Set<CircleNodeDisplayType> = setOf(MAJOR_KEY),
    val questionNodeDisplays: Set<CircleNodeDisplayType> = setOf(MAJOR_KEY)
)

enum class CircleInversionType {
    NORMAL, INVERSED
}

enum class CircleRotateType {
    FIXED, RANDOM
}

enum class CircleNodeDisplayType {
    MAJOR_KEY, MINOR_KEY, MAJOR_KEY_SIGNATURE
}
