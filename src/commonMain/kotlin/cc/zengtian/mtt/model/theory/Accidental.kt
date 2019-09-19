package cc.zengtian.mtt.model.theory

import kotlin.math.absoluteValue

enum class Accidental(val offset: Int) {

    SHARP(1),
    FLAT(-1),
    DOUBLE_SHARP(2),
    DOUBLE_FLAT(-2);

    companion object {
        fun getByOffset(offset: Int): Accidental? {
            check(offset.absoluteValue <= 2) { "wrong offset $offset" }
            return values().find { it.offset == offset }
        }

        fun forEachIncludingNull(action: (Accidental?) -> Unit) {
            for (element in values()) action(element)
            action(null)
        }
    }

    val type: AccidentalType by lazy {
        if (offset > 0) {
            AccidentalType.SHARP
        } else {
            AccidentalType.FLAT
        }
    }
}

val Accidental?.offset: Int
    get() = this?.offset ?: 0

operator fun Accidental?.unaryMinus(): Accidental? = this?.let {
    Accidental.getByOffset(-it.offset)
}

fun Accidental?.toString(): String = this?.toString() ?: ""

enum class AccidentalType {
    SHARP,
    FLAT
}