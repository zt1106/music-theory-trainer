package cc.zengtian.mtt.util

import kotlin.js.Date

/**
 * Created by ZengTian on 9/26/2019.
 */
actual object Time {
    actual fun currentMillisecond(): Long {
        return Date().getTime().toLong()
    }
}
