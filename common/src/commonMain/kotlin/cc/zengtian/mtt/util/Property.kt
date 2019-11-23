package cc.zengtian.mtt.util

import kotlin.math.E
import kotlin.properties.ObservableProperty
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KMutableProperty0
import kotlin.reflect.KProperty

/**
 * Created by ZengTian on 2019/9/24.
 */
interface Listenable<T> {
    fun addListener(newValBlock: (T) -> Unit)
}

open class Property<T>(t: T) : ObservableProperty<T>(t), Listenable<T> {

    private val listeners = mutableListOf<(oldValue: T, newValue: T) -> Unit>()

    fun notifyAllWithOldValue() {
        val value = getValue(null, ::E)
        listeners.forEach { it(value, value) }
    }

    override fun addListener(newValBlock: (T) -> Unit) {
        listeners.add { _: T, newValue: T -> newValBlock(newValue) }
    }

    override fun afterChange(property: KProperty<*>, oldValue: T, newValue: T) {
        listeners.forEach { listener -> listener(oldValue, newValue) }
    }
}

@Suppress("UNCHECKED_CAST")
class LazyProperty<T> : ReadWriteProperty<Any?, T>, Listenable<T> {

    private var initialized = false

    private var value: T? = null

    private val listeners = mutableListOf<(property: KProperty<*>, oldValue: T?, newValue: T) -> Unit>()

    override fun addListener(newValBlock: (T) -> Unit) {
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
    private val allChangedListeners: MutableList<() -> Unit> by lazy { mutableListOf<() -> Unit>() }

    private val anyChangedListeners: MutableList<() -> Unit> by lazy { mutableListOf<() -> Unit>() }
    private val changedSet = mutableSetOf<Listenable<*>>()

    private val propsCount = props.size

    init {
        props.forEach { it.addListener { _ -> changedSet.addInner(it) } }
    }

    private fun MutableSet<Listenable<*>>.addInner(prop: Listenable<*>) {
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

fun <T> alias(p: KMutableProperty<T>) = AliasProperty(p)

// TODO not working right now, maybe it's a bug
fun <T> lazyAlias(propProvider: () -> KMutableProperty0<T>) = LazyAliasProperty(propProvider)

// TODO fix when writing front-end
class AliasProperty<T>(private val p: KMutableProperty<T>) {
    operator fun getValue(thisRef: Any?, property: KProperty<*>): T = p.getter.call()
    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        p.setter.call(value)
    }
}

class LazyAliasProperty<T>(private val propProvider: () -> KMutableProperty0<T>) {
    private val p by lazy { propProvider() }
    operator fun getValue(thisRef: Any?, property: KProperty<*>): T = p.getter.call()
    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        p.setter.call(value)
    }
}
