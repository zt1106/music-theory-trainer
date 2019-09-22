package util

import assertEquals
import assertIsNull
import cc.zengtian.mtt.util.Storage
import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.Serializable
import kotlin.test.Test

/**
 * Created by ZengTian on 2019/9/21.
 */
@ImplicitReflectionSerializer
class StorageTest {
    @Test
    fun `test storage string`() {
        val key = "storage-test-key-123123123"
        val value = "storage-test-value-123123123123"
        Storage.remove(key)
        Storage.getString(key).assertIsNull()
        Storage.saveString(key, value)
        Storage.getString(key).assertEquals(value)
        Storage.remove(key)
        Storage.getString(key).assertIsNull()
    }

    @Serializable
    private data class TestDataObject(
        val string: String,
        val number: Int,
        val map: Map<String, String>,
        val list: List<Int>
    )

    private val testDataObject = TestDataObject(
        "string",
        1,
        mapOf("key" to "value", "key2" to "value2"),
        listOf(1, 2, 3, 4, 5)
    )


    private val testDataObject2 = TestDataObject(
        "string2",
        11,
        mapOf("key3" to "value3", "key21" to "value21"),
        listOf(1, 2, 3, 4, 5, 6, 7)
    )

    @Test
    fun `test storage json`() {
//        val key = "storage-test-json-123123"
//        Storage.remove(key)
//        Storage.save(key, testDataObject)
//        Storage.get<TestDataObject>(key).assertEqualsAnd(testDataObject).println()
//        val list = listOf(testDataObject, testDataObject2)
//        Storage.saveList(key, list)
//        Storage.getList<TestDataObject>(key).printlnAnd().assertEquals(list)
//        val map = mapOf("1" to testDataObject, "2" to testDataObject2)
//        Storage.saveMap(key, map)
//        Storage.getMap<TestDataObject>(key).printlnAnd().assertEquals(map)
//        Storage.remove(key)
    }
}