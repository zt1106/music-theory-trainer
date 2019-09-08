package cc.zengtian.st.value

import cc.zengtian.st.model.*
import kotlin.math.absoluteValue

/**
 * Created by ZengTian on 2019/9/8.
 */
val IONIAN = Scale("IONIAN", listOf(2, 2, 1, 2, 2, 2))
val DORIAN = Scale("DORIAN", listOf(2, 1, 2, 2, 2, 1))
val PHRYGIAN = Scale("PHRYGIAN", listOf(1, 2, 2, 2, 1, 2))
val LYDIAN = Scale("LYDIAN", listOf(2, 2, 2, 1, 2, 2))
val MIXOLYDIAN = Scale("MIXOLYDIAN", listOf(2, 2, 1, 2, 2, 1))
val AEOLIAN = Scale("AEOLIAN", listOf(2, 1, 2, 2, 1, 2))
val LOCRIAN = Scale("LOCRIAN", listOf(1, 2, 2, 1, 2, 2))
val HARMONIC_MINOR = Scale("HARMONIC_MINOR", listOf(2, 1, 2, 2, 1, 3))
val MELODIC_MINOR_UPPER = Scale("MELODIC_MINOR_UPPER", listOf(2, 1, 2, 2, 2, 2))
val PENTATONIC = Scale("PENTATONIC", listOf(2, 2, 3, 2))
val CHROMATIC = Scale("CHROMATIC", listOf(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1))

val BUILT_IN_SCALE = listOf(
    IONIAN,
    DORIAN,
    PHRYGIAN,
    LYDIAN,
    MIXOLYDIAN,
    AEOLIAN,
    LOCRIAN,
    HARMONIC_MINOR,
    MELODIC_MINOR_UPPER,
    PENTATONIC,
    CHROMATIC
)

private val ALL_NOTES_INNER = mutableMapOf<Pair<WellTemperedNote, Accidental?>, Note>().apply {
    WellTemperedNote.values().forEach { wellTemperedNote ->
        Accidental.forEachIncludingNull { accidental ->
            try {
                put(wellTemperedNote to accidental, Note(wellTemperedNote, accidental))
            } catch (e: Exception) {
            }
        }
    }
}

val ALL_NOTES = ALL_NOTES_INNER.toMap()

val ALL_NON_DOUBLE_ACCIDENTAL_NOTES = ALL_NOTES_INNER.filterValues { it.accidental.getOffset().absoluteValue < 2 }

fun main() {
    ALL_NON_DOUBLE_ACCIDENTAL_NOTES.forEach{println(it)}
}