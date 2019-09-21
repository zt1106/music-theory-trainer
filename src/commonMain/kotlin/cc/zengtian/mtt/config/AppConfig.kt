package cc.zengtian.mtt.config

import kotlinx.serialization.Serializable

/**
 * Created by ZengTian on 2019/9/16.
 */
@Serializable
data class AppConfig(val windowSize: Pair<Int, Int>)