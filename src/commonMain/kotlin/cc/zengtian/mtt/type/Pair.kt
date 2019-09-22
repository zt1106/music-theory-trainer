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

typealias CheckBoxModel<T> = MutablePair<T, Boolean>

var CheckBoxModel<*>.selected
    get() = this.second
    set(value) {
        this.second = value
    }

var <T> CheckBoxModel<T>.data
    get() = this.first
    set(value) {
        this.first = value
    }