package cc.zengtian.mtt.model.theory

import kotlin.math.absoluteValue

class Note private constructor(val actual: ActualNote, val accidental: Accidental?) {

    companion object {
        private val ALL_NOTES = mutableMapOf<Pair<ActualNote, Accidental?>, Note>().apply {
            ActualNote.values().forEach { actualNote ->
                Accidental.forEachIncludingNull { accidental ->
                    try {
                        put(actualNote to accidental, Note(actualNote, accidental))
                    } catch (e: Exception) {
                    }
                }
            }
        }.toMap()
        val ALL_NON_DOUBLE_ACCIDENTAL_NOTES = ALL_NOTES.filterValues { it.accidental.getOffset().absoluteValue < 2 }
        fun ofActual(actualNote: ActualNote, accidental: Accidental?): Note {
            return ALL_NOTES[actualNote to accidental] ?: throw IllegalArgumentException("$actualNote $accidental")
        }

        fun of(actualNote: ActualNote, accidental: Accidental?): Note {
            require(!actualNote.needResolve)
            return ALL_NOTES[actualNote.getByOffset(accidental.getOffset()) to accidental] ?: throw IllegalArgumentException("$actualNote $accidental")
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