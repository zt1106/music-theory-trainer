package cc.zengtian.mtt.util

/**
 * Created by ZengTian on 2019/10/27.
 */
fun Boolean.whenTrue(block: () -> Unit): Boolean {
    if (this) {
        block()
    }
    return this
}

fun Boolean.whenFalse(block: () -> Unit) : Boolean{
    if (!this) {
        block()
    }
    return this
}