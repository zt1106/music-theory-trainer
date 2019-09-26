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

fun List<*>.nextOrFirst(idx: Int): Int {
    return if (idx < size - 1) {
        idx + 1
    } else {
        0
    }
}

fun List<*>.prevOrLast(idx: Int): Int {
    return if (idx == 0) {
        size - 1
    } else {
        idx - 1
    }
}

fun Array<*>.nextOrFirst(idx: Int): Int {
    return if (idx < size - 1) {
        idx + 1
    } else {
        0
    }
}

fun Array<*>.prevOrLast(idx: Int): Int {
    return if (idx == 0) {
        size - 1
    } else {
        idx - 1
    }
}

/**
 * offset > 0 to right
 * offset < 0 to left
 */
//fun <T> Array<T>.getByOffset(from: Int, offset: Int): T {
//    require(isNotEmpty())
//    return if (offset > 0) {
//        val mod = offset +  % size
//
//    } else {
//
//    }
//}
