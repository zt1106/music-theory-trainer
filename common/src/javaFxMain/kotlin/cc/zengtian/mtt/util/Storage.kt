package cc.zengtian.mtt.util

import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.util.*

/**
 * Created by ZengTian on 2019/9/14.
 */
actual object LocalStorage {

    private val propertiesFile: File by lazy {
        val home = System.getProperty("user.home")
        var file = File(home, LOCAL_STORAGE_FILE_NAME)
        if (!file.exists()) {
            try {
                file.createNewFile()
            } catch (e: Exception) {
                Logger.error("create properties file error", e)
                file = File.createTempFile("mtt", "temp")
                file.deleteOnExit()
            }
        }
        file
    }

    private val properties: Properties by lazy {
        val properties = Properties()
        properties.load(FileReader(propertiesFile))
        properties
    }

    actual fun get(key: String): String? {
        return properties.getProperty(key)
    }

    actual fun save(key: String, value: String) {
        properties.setProperty(key, value)
        persist()
    }

    actual fun remove(key: String) {
        properties.remove(key)
        persist()
    }

    private fun persist() {
        properties.store(FileWriter(propertiesFile), null);
    }
}