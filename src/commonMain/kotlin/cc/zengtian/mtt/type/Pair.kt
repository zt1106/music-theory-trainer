package cc.zengtian.mtt.type

/**
 * Created by ZengTian on 2019/9/21.
 */
data class MutablePair<A, B>(
    var first: A,
    var second: B
) {
    override fun toString(): String = "($first, $second)"
}

