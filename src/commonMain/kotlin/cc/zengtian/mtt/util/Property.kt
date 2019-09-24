package cc.zengtian.mtt.util

import kotlin.properties.ObservableProperty
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * Created by ZengTian on 2019/9/24.
 */
class Property<T>(t: T) : ObservableProperty<T>(t) {

    private val listeners = mutableListOf<(property: KProperty<*>, oldValue: T, newValue: T) -> Unit>()

    fun addListener(newValueAcceptor:(T) -> Unit) {
        listeners.add { _: KProperty<*>, _: T, newValue: T -> newValueAcceptor(newValue) }
    }

    override fun afterChange(property: KProperty<*>, oldValue: T, newValue: T) {
        listeners.forEach { listener -> listener(property, oldValue, newValue) }
    }
}

@Suppress("UNCHECKED_CAST")
class LazyProperty<T>  : ReadWriteProperty<Any?, T> {

    private var initialized = false

    private var value: T? = null

    private val listeners = mutableListOf<(property: KProperty<*>, oldValue: T?, newValue: T) -> Unit>()

    fun addListener(newValueAcceptor:(T) -> Unit) {
        listeners.add { _: KProperty<*>, _: T?, newValue: T -> newValueAcceptor(newValue) }
    }

    private fun afterChange(property: KProperty<*>, oldValue: T?, newValue: T): Unit {
        listeners.forEach { listener -> listener(property, oldValue, newValue) }
    }

    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        check(initialized) { "lazy propery not initialized" }
        return value as T
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        val oldValue = this.value
        this.value = value
        initialized = true
        afterChange(property, oldValue, value)
    }
}