package cc.zengtian.mtt.util

/**
 * Created by ZengTian on 2019/9/9.
 */
fun <T : Any> Collection<T>.containsNot(t: T): Boolean = !contains(t)

fun <T : Any> T.asSingletonList(): List<T> = listOf(this)

fun Collection<Long>.median(): Long {
    return sorted().run {
        if (size == 0) {
            return@run 0
        }
        if (size % 2 == 0) {
            (this[size / 2] + this[(size - 1) / 2]) / 2
        } else {
            this[(size - 1) / 2]
        }
    }
}

/**
 * offset > 0 to right
 * offset < 0 to left
 */
fun <T> Array<T>.getByOffset(from: Int, offset: Int): T {
    require(isNotEmpty())
    val mod = (offset + from) % size
    return if (offset > 0) {
        this[mod]
    } else {
        if (mod == 0) {
            this[mod]
        } else {
            this[mod + size]
        }
    }
}

fun <T> List<T>.getByOffset(from: Int, offset: Int): T {
    require(isNotEmpty())
    val mod = (offset + from) % size
    return if (offset > 0) {
        this[mod]
    } else {
        if (mod == 0) {
            this[mod]
        } else {
            this[mod + size]
        }
    }
}