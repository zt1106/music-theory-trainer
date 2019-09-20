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
        val ALL_NON_DOUBLE_ACCIDENTAL_NOTES = ALL_NOTES.filterValues { it.accidental.offset.absoluteValue < 2 }
        // common notes
        val C = of(ActualNote.C, null)
        val C_SHARP = of(ActualNote.C, Accidental.SHARP)
        val D_FLAT = of(ActualNote.D, Accidental.FLAT)
        val D = of(ActualNote.D, null)
        val D_SHARP = of(ActualNote.D, Accidental.SHARP)
        val E_FLAT = of(ActualNote.E, Accidental.FLAT)
        val E = of(ActualNote.E, null)
        val F = of(ActualNote.F, null)
        val F_SHARP = of(ActualNote.F, Accidental.SHARP)
        val G_FLAT = of(ActualNote.G, Accidental.FLAT)
        val G = of(ActualNote.G, null)
        val G_SHARP = of(ActualNote.G, Accidental.SHARP)
        val A_FLAT = of(ActualNote.A, Accidental.FLAT)
        val A = of(ActualNote.A, null)
        val A_SHARP = of(ActualNote.A, Accidental.SHARP)
        val B_FLAT = of(ActualNote.B, Accidental.FLAT)
        val B = of(ActualNote.B, null)
        fun ofActual(actualNote: ActualNote, accidental: Accidental?): Note {
            return ALL_NOTES[getCompositeKey(actualNote, accidental)]
                ?: throw IllegalArgumentException("$actualNote $accidental")
        }

        fun of(actualNote: ActualNote, accidental: Accidental?): Note {
            require(!actualNote.needResolve)
            return ALL_NOTES[getCompositeKey(actualNote.getByOffset(accidental.offset), accidental)]
                ?: throw IllegalArgumentException("$actualNote $accidental")
        }

        private fun getCompositeKey(actualNote: ActualNote, accidental: Accidental?): Int {
            return actualNote.ordinal * 100 + accidental.offset
        }
    }

    val key: Key? by lazy { Key.values().find { it.startingNote == this } }

    val beforeAccidentalActual: ActualNote by lazy { actual.getByOffset(-accidental.offset) }

    init {
        require(!(beforeAccidentalActual.needResolve && accidental != null)) { "invalid note $beforeAccidentalActual $accidental" }
        require(!(actual.needResolve && accidental == null)) { "invalid note $beforeAccidentalActual $accidental" }
    }

    private val toString : String by lazy {
        if (accidental == null) {
            beforeAccidentalActual.toString()
        } else {
            beforeAccidentalActual.toString() + "_" + accidental.toString()
        }
    }

    override fun toString(): String = toString
}
