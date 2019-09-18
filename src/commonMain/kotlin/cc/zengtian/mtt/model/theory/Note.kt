package cc.zengtian.mtt.model.theory

import kotlin.math.absoluteValue

class Note private constructor(val actual: ActualNote, val accidental: Accidental?) {

    companion object {
        private val ALL_NOTES = mutableMapOf<Int, Note>().apply {
            ActualNote.values().forEach { actualNote ->
                Accidental.forEachIncludingNull { accidental ->
                    try {
                        put(getCompositeKey(actualNote, accidental), Note(actualNote, accidental))
                    } catch (e: Exception) {
                    }
                }
            }
        }.toMap()
        val ALL_NON_DOUBLE_ACCIDENTAL_NOTES = ALL_NOTES.filterValues { it.accidental.getOffset().absoluteValue < 2 }
        // common notes
        val C = of(ActualNote.C_, null)
        val C_SHARP = of(ActualNote.C_, Accidental.SHARP)
        val D_FLAT = of(ActualNote.D_, Accidental.FLAT)
        val D = of(ActualNote.D_, null)
        val D_SHARP = of(ActualNote.D_, Accidental.SHARP)
        val E_FLAT = of(ActualNote.E_, Accidental.FLAT)
        val E = of(ActualNote.E_, null)
        val F = of(ActualNote.F_, null)
        val F_SHARP = of(ActualNote.F_, Accidental.SHARP)
        val G_FLAT = of(ActualNote.G_, Accidental.FLAT)
        val G = of(ActualNote.G_, null)
        val G_SHARP = of(ActualNote.G_, Accidental.SHARP)
        val A_FLAT = of(ActualNote.A_, Accidental.FLAT)
        val A = of(ActualNote.A_, null)
        val A_SHARP = of(ActualNote.A_, Accidental.SHARP)
        val B_FLAT = of(ActualNote.B_, Accidental.FLAT)
        val B = of(ActualNote.B_, null)
        fun ofActual(actualNote: ActualNote, accidental: Accidental?): Note {
            return ALL_NOTES[getCompositeKey(actualNote, accidental)]
                ?: throw IllegalArgumentException("$actualNote $accidental")
        }

        fun of(actualNote: ActualNote, accidental: Accidental?): Note {
            require(!actualNote.needResolve)
            return ALL_NOTES[getCompositeKey(actualNote.getByOffset(accidental.getOffset()), accidental)]
                ?: throw IllegalArgumentException("$actualNote $accidental")
        }

        private fun getCompositeKey(actualNote: ActualNote, accidental: Accidental?): Int {
            return actualNote.ordinal * 100 + accidental.getOffset()
        }
    }

    val key: Key? by lazy { Key.values().find { it.startingNote == this } }

    val beforeAccidentalActual: ActualNote by lazy { actual.getByOffset(-accidental.getOffset()) }

    init {
        require(!(beforeAccidentalActual.needResolve && accidental != null)) { "invalid note $beforeAccidentalActual $accidental" }
        require(!(actual.needResolve && accidental == null)) { "invalid note $beforeAccidentalActual $accidental" }
    }

    override fun toString(): String = beforeAccidentalActual.toString() + "_" + accidental.toString()
}
