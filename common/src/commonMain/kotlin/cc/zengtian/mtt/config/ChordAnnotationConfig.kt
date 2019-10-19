package cc.zengtian.mtt.config

import kotlinx.serialization.Serializable

/**
 * Created by ZengTian on 2019/10/19.
 */
@Serializable
data class ChordAnnotationConfig(
        val susEnabled: Boolean = true
)