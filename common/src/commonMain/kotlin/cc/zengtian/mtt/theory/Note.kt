package cc.zengtian.mtt.theory

import cc.zengtian.mtt.theory.Accidental.*

enum class Note constructor(actual: ActualNote, val accidental: Accidental?) {

    // no accidental
    C(ActualNote.C, null),
    D(ActualNote.D, null),
    E(ActualNote.E, null),
    F(ActualNote.F, null),
    G(ActualNote.G, null),
    A(ActualNote.A, null),
    B(ActualNote.B, null),
    // one accidental
    C_SHARP(ActualNote.C, SHARP),
    C_FLAT(ActualNote.C, FLAT),
    D_SHARP(ActualNote.D, SHARP),
    D_FLAT(ActualNote.D, FLAT),
    E_SHARP(ActualNote.E, SHARP),
    E_FLAT(ActualNote.E, FLAT),
    F_SHARP(ActualNote.F, SHARP),
    F_FLAT(ActualNote.F, FLAT),
    G_SHARP(ActualNote.G, SHARP),
    G_FLAT(ActualNote.G, FLAT),
    A_SHARP(ActualNote.A, SHARP),
    A_FLAT(ActualNote.A, FLAT),
    B_SHARP(ActualNote.B, SHARP),
    B_FLAT(ActualNote.B, FLAT),
    // two accidental
    C_DOUBLE_SHARP(ActualNote.C, DOUBLE_SHARP),
    C_DOUBLE_FLAT(ActualNote.C, DOUBLE_FLAT),
    D_DOUBLE_SHARP(ActualNote.D, DOUBLE_SHARP),
    D_DOUBLE_FLAT(ActualNote.D, DOUBLE_FLAT),
    E_DOUBLE_SHARP(ActualNote.E, DOUBLE_SHARP),
    E_DOUBLE_FLAT(ActualNote.E, DOUBLE_FLAT),
    F_DOUBLE_SHARP(ActualNote.F, DOUBLE_SHARP),
    F_DOUBLE_FLAT(ActualNote.F, DOUBLE_FLAT),
    G_DOUBLE_SHARP(ActualNote.G, DOUBLE_SHARP),
    G_DOUBLE_FLAT(ActualNote.G, DOUBLE_FLAT),
    A_DOUBLE_SHARP(ActualNote.A, DOUBLE_SHARP),
    A_DOUBLE_FLAT(ActualNote.A, DOUBLE_FLAT),
    B_DOUBLE_SHARP(ActualNote.B, DOUBLE_SHARP),
    B_DOUBLE_FLAT(ActualNote.B, DOUBLE_FLAT);

    companion object {
        private val ALL_NOTES = mutableMapOf<Int, Note>().apply {
            values().forEach { note ->
                put(getCompositeKey(note.actual, note.accidental), note)
            }
        }.toMap()
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

    val actual: ActualNote = actual.getByOffset(accidental.offset)

    val key: Key? by lazy { Key.values().find { it.startingNote == this } }

    val beforeAccidentalActual: ActualNote by lazy { this.actual.getByOffset(-accidental.offset) }

    private val toString: String by lazy {
        beforeAccidentalActual.toString() + accidental.toString()
    }

    override fun toString(): String = toString
}

expect fun Note.play(duration: Double = 1.0)
