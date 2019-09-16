package cc.zengtian.mtt.util

import com.github.aakira.napier.Napier

/**
 * Created by ZengTian on 2019/9/14.
 */
typealias Logger = Napier

fun Napier.info(s: String) = i(s)

fun Napier.debug(s: String) = d(s)

fun Napier.debug(s: String, e: Exception) = d(s, e)