package cc.zengtian.mtt.util

import kotlin.properties.ObservableProperty
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * Created by ZengTian on 2019/9/24.
 */
interface Listenable<T> {
    fun addListener(newValBlock: (T) -> Unit)
}

class Property<T>(t: T) : ObservableProperty<T>(t), Listenable<T> {

    private val listeners = mutableListOf<(property: KProperty<*>, oldValue: T, newValue: T) -> Unit>()

    override fun addListener(newValBlock:(T) -> Unit) {
        listeners.add { _: KProperty<*>, _: T, newValue: T -> newValBlock(newValue) }
    }

    override fun afterChange(property: KProperty<*>, oldValue: T, newValue: T) {
        listeners.forEach { listener -> listener(property, oldValue, newValue) }
    }
}

@Suppress("UNCHECKED_CAST")
class LazyProperty<T>  : ReadWriteProperty<Any?, T>, Listenable<T> {

    private var initialized = false

    private var value: T? = null

    private val listeners = mutableListOf<(property: KProperty<*>, oldValue: T?, newValue: T) -> Unit>()

    override fun addListener(newValBlock:(T) -> Unit) {
        listeners.add { _: KProperty<*>, _: T?, newValue: T -> newValBlock(newValue) }
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

class CompositeProperty(vararg props: Listenable<*>) {
    private val allChangedListeners : MutableList<() -> Unit> by lazy { mutableListOf<() -> Unit>() }

    private val anyChangedListeners : MutableList<() -> Unit> by lazy { mutableListOf<() -> Unit>() }
    private val changedSet = mutableSetOf<Listenable<*>>()

    private val propsCount = props.size

    init {
        props.forEach { it.addListener { _ -> changedSet.addInner(it) } }
    }

    private fun MutableSet<Listenable<*>>.addInner(prop : Listenable<*>) {
        add(prop)
        anyChangedListeners.forEach { it() }
        if (changedSet.size == propsCount) {
            allChangedListeners.forEach { it() }
            changedSet.clear()
        }
    }

    fun allChanged(block: () -> Unit) {
        allChangedListeners.add(block)
    }
    fun anyChanged(block: () -> Unit) {
        anyChangedListeners.add(block)
    }

}
