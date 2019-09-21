package cc.zengtian.mtt.util

/**
 * Created by ZengTian on 2019/9/9.
 */
fun <T : Any> Collection<T>.containsNot(t: T): Boolean = !contains(t)

fun <T : Any> T.asSingletonList(): List<T> = listOf(this)
