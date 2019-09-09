package cc.zengtian.mtt.model.theory

import kotlin.math.absoluteValue

@Suppress("unused")
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

    fun getPrefix(): String = super.toString() + "_"

    fun getType(): AccidentalType {
        return if (offset > 0) {
            AccidentalType.SHARP
        } else {
            AccidentalType.FLAT
        }
    }
}

fun Accidental?.getOffset(): Int = this?.offset ?: 0
fun Accidental?.getPrefix(): String = this?.getPrefix() ?: ""
operator fun Accidental?.unaryMinus() : Accidental? = this?.let {
    Accidental.getByOffset(-it.offset)
}

enum class AccidentalType {
    SHARP,
    FLAT
}