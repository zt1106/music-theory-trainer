package cc.zengtian.mtt.util

import com.github.aakira.napier.Antilog
import com.github.aakira.napier.Napier
import kotlinx.serialization.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration

/**
 * Created by ZengTian on 2019/9/14.
 */
expect object LocalStorage {
    fun get(key: String): String?
    fun save(key: String, value: String)
}

object Storage {
    val json = Json(JsonConfiguration.Stable)
    fun getString(key: String): String? = LocalStorage.get(key)
    fun saveString(key: String, value: String) = LocalStorage.save(key, value)
    fun saveIfAbsent(key: String, value: String) {
        val old = getString(key)
        if (old == null) {
            saveString(key, value)
        }
    }

    @ImplicitReflectionSerializer
    inline fun <reified T : Any> saveAsJson(key: String, value: T) {
        LocalStorage.save(key, json.stringify(value))
    }

    @ImplicitReflectionSerializer
    inline fun <reified T : Any> get(key: String): T? {
        val str = LocalStorage.get(key) ?: return null
        return try {
            json.parse(str)
        } catch (e: Exception) {
            Logger.debug("parse json error", e)
            null
        }
    }
}

@Serializable
data class Data(val a: Int, val b: String = "42")

fun main(args: Array<String>) {
    Napier.base(object : Antilog() {
        override fun performLog(priority: Napier.Level, tag: String?, throwable: Throwable?, message: String?) {
            println(message)
        }
    })
    Logger.info("hello world")
    // Json also has .Default configuration which provides more reasonable settings,
    // but is subject to change in future versions
    val json = Json(JsonConfiguration.Stable)
    // serializing objects
    val jsonData = json.stringify(Data.serializer(), Data(42))
    // serializing lists
    val jsonList = json.stringify(Data.serializer().list, listOf(Data(42)))
    println(jsonData) // {"a": 42, "b": "42"}
    println(jsonList) // [{"a": 42, "b": "42"}]

    // parsing data back
    val obj = json.parse(Data.serializer(), """{"a":42}""") // b is optional since it has default value
    println(obj) // Data(a=42, b="42")
}