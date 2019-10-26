package cc.zengtian.mtt.util

import kotlin.reflect.KProperty

/**
 * Created by ZengTian on 2019/10/26.
 */
fun <T> lazyVar(initializer: () -> T) = LazyVarImpl(initializer)

interface LazyVar<T> {

    var value: T

    fun isInitialized(): Boolean
}

class LazyVarImpl<T>(private val initializer: () -> T) : LazyVar<T> {

    private var myValue : T? = null

    override var value: T
        get() {
            if (myValue == null) {
                myValue = initializer()
            }
            return myValue!!
        }
        set(value) {
            myValue = value
        }

    override fun isInitialized(): Boolean {
        return myValue != null
    }

    override fun toString(): String {
        return if (myValue == null) {
            "not initialized value"
        } else {
            myValue.toString()
        }
    }

    operator fun getValue(thisRef: Any?, property: KProperty<*>): T = value
    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        this.value = value
    }
}