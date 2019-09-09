package cc.zengtian.mtt.ext

/**
 * Created by ZengTian on 2019/9/9.
 */
fun Any?.println() {
    this?.apply { println(this) }
}