package cc.zengtian.mtt.util

import kotlinx.serialization.UnstableDefault
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration

/**
 * Created by ZengTian on 2019/9/21.
 */
@UseExperimental(UnstableDefault::class)
val Json = Json(JsonConfiguration.Default)

