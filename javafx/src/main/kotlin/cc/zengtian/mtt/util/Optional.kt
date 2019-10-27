package cc.zengtian.mtt.util

import java.util.*

/**
 * Created by ZengTian on 2019/10/27.
 */
fun <T> Optional<T>.safeGet(): T? {
    return if (isPresent) {
        get()
    } else {
        null
    }
}