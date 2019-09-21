package cc.zengtian.mtt.util

import kotlinx.serialization.*
import kotlinx.serialization.json.Json

/**
 * Created by ZengTian on 2019/9/14.
 */
expect object LocalStorage {
    fun get(key: String): String?
    fun save(key: String, value: String)
    fun remove(key: String)
}

object Storage {
    fun remove(key: String) = LocalStorage.remove(key)
    fun getString(key: String): String? = LocalStorage.get(key)
    fun saveString(key: String, value: String) = LocalStorage.save(key, value)
    fun saveStringIfAbsent(key: String, value: String) {
        val old = getString(key)
        if (old == null) {
            saveString(key, value)
        }
    }

    @UseExperimental(ImplicitReflectionSerializer::class)
    inline fun <reified T : Any> save(key: String, value: T) {
        LocalStorage.save(key, Json.stringify(value).base64Encode())
    }

    @UseExperimental(ImplicitReflectionSerializer::class)
    inline fun <reified T : Any> get(key: String): T? {
        val str = LocalStorage.get(key) ?: return null
        return try {
            Json.parse(str.base64Decode())
        } catch (e: Exception) {
            Logger.debug("parse json error", e)
            null
        }
    }

    @UseExperimental(ImplicitReflectionSerializer::class)
    inline fun <reified T : Any> saveList(key: String, list: List<T>) {
        LocalStorage.save(key, Json.stringify(list).base64Encode())
    }

    @UseExperimental(ImplicitReflectionSerializer::class)
    inline fun <reified T : Any> getList(key: String): List<T> {
        val str = LocalStorage.get(key) ?: return emptyList()
        return try {
            Json.parseList(str.base64Decode())
        } catch (e: Exception) {
            Logger.debug("parse json error", e)
            emptyList()
        }
    }

    @UseExperimental(ImplicitReflectionSerializer::class)
    inline fun <reified V : Any> saveMap(key: String, map: Map<String, V>) {
        LocalStorage.save(key, Json.stringify(map).base64Encode())
    }

    @UseExperimental(ImplicitReflectionSerializer::class)
    inline fun <reified V : Any> getMap(key: String): Map<String, V> {
        val str = LocalStorage.get(key) ?: return emptyMap()
        return try {
            Json.parseMap(str.base64Decode())
        } catch (e: Exception) {
            Logger.debug("parse json error", e)
            emptyMap()
        }
    }
}