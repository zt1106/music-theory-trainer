package cc.zengtian.mtt.theory

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

    override fun toString(): String {
        return when (this) {
            SHARP -> "\u266F"
            FLAT -> "\u266D"
            DOUBLE_SHARP -> "\uD834\uDD2A"
            DOUBLE_FLAT -> "\uD834\uDD2B"
        }
    }
}

val Accidental?.offset: Int
    get() = this?.offset ?: 0

val Accidental?.type: AccidentalType?
    get() = this?.type

operator fun Accidental?.unaryMinus(): Accidental? = this?.let {
    Accidental.getByOffset(-it.offset)
}

fun Accidental?.toString(): String = this?.toString() ?: ""

enum class AccidentalType {
    SHARP,
    FLAT
}
