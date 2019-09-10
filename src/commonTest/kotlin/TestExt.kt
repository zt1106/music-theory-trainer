/**
 * Created by ZengTian on 2019/9/10.
 */
fun Any?.println() {
    this?.apply { println(this) }
}

fun <T : Any?> T.alsoPrintln(): T {
    this.println()
    return this
}