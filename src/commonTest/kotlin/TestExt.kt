import kotlin.test.assertEquals
import kotlin.test.assertNull

/**
 * Created by ZengTian on 2019/9/10.
 */
fun Any?.println() {
    this?.apply { println(this) }
}

fun <T : Any?> T.printlnAnd(): T {
    this.println()
    return this
}

fun Any?.assertEquals(other: Any, message: String? = null) {
    assertEquals(other, this, message)
}

fun <T : Any?> T.assertEqualsAnd(other: Any, message: String? = null): T {
    this.assertEquals(other, message)
    return this
}

fun <T : Any?> T.assertTrue(block: (T) -> Boolean) {
    kotlin.test.assertTrue { block(this) }
}

fun <T : Any?> T.assertTrueAnd(block: (T) -> Boolean): T {
    this.assertTrue(block)
    return this
}

fun Any?.assertIsNull() {
    assertNull(this)
}
