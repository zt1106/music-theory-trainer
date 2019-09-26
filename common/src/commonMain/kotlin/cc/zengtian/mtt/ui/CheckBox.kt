package cc.zengtian.mtt.ui

import cc.zengtian.mtt.type.MutablePair

/**
 * Created by ZengTian on 2019/9/22.
 */
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