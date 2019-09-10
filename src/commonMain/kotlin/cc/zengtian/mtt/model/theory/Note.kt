package cc.zengtian.mtt.model.theory

import kotlin.math.absoluteValue

class Note private constructor(val wTN: WellTemperedNote, val accidental: Accidental?) {

    companion object {
        private val ALL_NOTES = mutableMapOf<Pair<WellTemperedNote, Accidental?>, Note>().apply {
            WellTemperedNote.values().forEach { wellTemperedNote ->
                Accidental.forEachIncludingNull { accidental ->
                    try {
                        put(wellTemperedNote to accidental, Note(wellTemperedNote, accidental))
                    } catch (e: Exception) {
                    }
                }
            }
        }.toMap()
        val ALL_NON_DOUBLE_ACCIDENTAL_NOTES = ALL_NOTES.filterValues { it.accidental.getOffset().absoluteValue < 2 }
        fun ofWellTempered(wellTemperedNote: WellTemperedNote, accidental: Accidental?): Note {
            return ALL_NOTES[wellTemperedNote to accidental] ?: throw IllegalArgumentException("$wellTemperedNote $accidental")
        }

        fun of(wellTemperedNote: WellTemperedNote, accidental: Accidental?): Note {
            require(!wellTemperedNote.needResolve)
            return ALL_NOTES[wellTemperedNote.getByOffset(accidental.getOffset()) to accidental] ?: throw IllegalArgumentException("$wellTemperedNote $accidental")
        }
    }

    val key: Key? by lazy { Key.values().find { it.startingNote == this } }

    val beforeAccidentalWTN: WellTemperedNote by lazy { wTN.getByOffset(-accidental.getOffset()) }

    init {
        require(!(beforeAccidentalWTN.needResolve && accidental != null)) { "invalid note $beforeAccidentalWTN $accidental" }
        require(!(wTN.needResolve && accidental == null)) { "invalid note $beforeAccidentalWTN $accidental" }
    }
    
    override fun toString(): String = beforeAccidentalWTN.toString() + "_" + accidental.toString()
}