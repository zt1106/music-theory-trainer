package cc.zengtian.mtt.model.theory

import kotlin.math.absoluteValue

class Note private constructor(val wellTemperedNote: WellTemperedNote, val accidental: Accidental?) {

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
        fun of(wellTemperedNote: WellTemperedNote, accidental: Accidental?) : Note {
            return ALL_NOTES[wellTemperedNote to accidental] ?: throw IllegalArgumentException()
        }
    }

    init {
        require(!(getBeforeAccidentalWellTemperedNote().needResolve && accidental != null)) { "invalid note ${getBeforeAccidentalWellTemperedNote()} $accidental" }
        require(!(wellTemperedNote.needResolve && accidental == null)) { "invalid note ${getBeforeAccidentalWellTemperedNote()} $accidental" }
    }

    fun getBeforeAccidentalWellTemperedNote(): WellTemperedNote = wellTemperedNote.getByOffset(-accidental.getOffset())

    fun getKey() : Key? = Key.values().find { it.startingNote == this }

    override fun toString(): String = accidental.getPrefix() + getBeforeAccidentalWellTemperedNote()
}