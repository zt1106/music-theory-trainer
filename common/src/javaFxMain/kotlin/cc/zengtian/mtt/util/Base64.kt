package cc.zengtian.mtt.util

import java.util.*

/**
 * Created by ZengTian on 2019/9/21.
 */
actual fun String.base64Encode(): String {
    return Base64.getEncoder().encodeToString(this.toByteArray())
}

actual fun String.base64Decode(): String {
    val bytes = Base64.getDecoder().decode(this)
    return String(bytes)
}

