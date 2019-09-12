package cc.zengtian.mtt.ext

/**
 * Created by ZengTian on 2019/9/9.
 */
fun <T : Any> Set<T>.containsNot(t: T): Boolean = !contains(t)

fun <T : Any> T.asSingletonList(): List<T> = listOf(this)
