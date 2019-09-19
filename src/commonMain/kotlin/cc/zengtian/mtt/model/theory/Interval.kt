package cc.zengtian.mtt.model.theory

import cc.zengtian.mtt.model.theory.IntervalQuality.*
import kotlin.math.absoluteValue

class Interval private constructor(val num: Int, val quality: IntervalQuality) {

    companion object {
        private val ALL_INTERVALS = mutableMapOf<Int, Interval>().apply {
            for (i in 1..8) {
                if (is1458(i)) {
                    IntervalQuality.valuesOf1458().forEach { q -> this[getCompositeKey(i, q)] = Interval(i, q) }
                } else {
                    IntervalQuality.valuesOf2367().forEach { q -> this[getCompositeKey(i, q)] = Interval(i, q) }
                }
            }
        }.toMap()

        private val PERFECT_OR_MAJOR_STEPS = listOf(0, 2, 4, 5, 7, 9, 11, 12)

        fun values() = ALL_INTERVALS

        fun of(num: Int, quality: IntervalQuality): Interval {
            return ALL_INTERVALS[getCompositeKey(num, quality)] ?: throw IllegalArgumentException("$num $quality")
        }

        fun of(from: ActualNote, to: ActualNote): Interval {
            TODO()
        }

        fun of(from: Note, to: Note): Interval? {
            val fromAccidental = from.accidental
            val fromKey = from.beforeAccidentalActual.keys[0]
            val fromScale = fromKey.getNotesOfScale(Scale.MAJOR)
            val fromScaleBeforeACC = fromScale.map { it.beforeAccidentalActual }
            val toBeforeACC = to.beforeAccidentalActual
            val idx = fromScaleBeforeACC.indexOf(toBeforeACC)
            return of1BaseIndexInMajorScale(idx + 1, to.accidental.offset - fromScale[idx].accidental.offset - fromAccidental.offset)
        }

        private fun getCompositeKey(num: Int, quality: IntervalQuality) : Int {
            return num * 10 + quality.ordinal
        }

        private fun of1BaseIndexInMajorScale(idx: Int, offset: Int): Interval? {
            return if (is1458(idx)) {
                when (offset) {
                    0 -> of(idx, PERFECT)
                    1 -> of(idx, AUGMENTED)
                    -1 -> of(idx, DIMISHED)
                    else -> null
                }
            } else {
                when (offset) {
                    0 -> of(idx, MAJOR)
                    1 -> of(idx, AUGMENTED)
                    -1 -> of(idx, MINOR)
                    -2 -> of(idx, DIMISHED)
                    else -> null
                }
            }
        }

        private fun is1458(i: Int): Boolean {
            check(i in 1..8)
            return (i == 1 || i == 4 || i == 5 || i == 8)
        }
    }

    val inversion: Interval by lazy {
        val inverQuality = when (quality) {
            AUGMENTED -> DIMISHED
            DIMISHED -> AUGMENTED
            MINOR -> MAJOR
            MAJOR -> MINOR
            PERFECT -> PERFECT
        }
        of(9 - num, inverQuality)
    }

    val physicalStep: Int by lazy { offsetToMajorOrPerfect + PERFECT_OR_MAJOR_STEPS[num - 1] }

    private val offsetToMajorOrPerfect: Int by lazy {
        when (quality) {
            AUGMENTED -> 1
            MAJOR -> 0
            PERFECT -> 0
            MINOR -> -1
            DIMISHED -> if (is1458(num)) {
                -1
            } else {
                -2
            }
        }
    }

    fun getbelowFromAbove(above: Note): Note? {
        val aboveBefore = above.beforeAccidentalActual
        val belowBefore = aboveBefore.getNoNeedResolveByOffset(-(num - 1))
        val belowActual = above.actual.getByOffset(-physicalStep)
        val belowOffset = belowBefore.getShortestOffsetTo(belowActual)
        val belowAccidental = if (belowOffset.absoluteValue <= 2) {
            Accidental.getByOffset(belowOffset)
        } else {
            return null
        }
        return Note.ofActual(belowActual, belowAccidental)
    }

    fun getAboveFromBelow(below: Note): Note? {
        val belowBefore = below.beforeAccidentalActual
        val belowKey = Key.ofNote(Note.ofActual(belowBefore, null))!!
        val belowMajorNotes = belowKey.getNotesOfScale(Scale.MAJOR)
        val noteOfNum = belowMajorNotes[num - 1]
        val aboveActual = noteOfNum.actual.getByOffset(offsetToMajorOrPerfect)
        val aboveBefore = noteOfNum.beforeAccidentalActual
        return try {
            Note.ofActual(aboveActual, Accidental.getByOffset(aboveBefore.getShortestOffsetTo(aboveActual)))
        } catch (e: Exception) {
            null
        }
    }

    override fun toString(): String = "${quality}_$num"
}

enum class IntervalQuality {
    AUGMENTED,
    MAJOR,
    PERFECT,
    MINOR,
    DIMISHED;

    companion object {
        fun valuesOf1458(): List<IntervalQuality> = listOf(AUGMENTED, PERFECT, DIMISHED)
        fun valuesOf2367(): List<IntervalQuality> = listOf(AUGMENTED, MAJOR, MINOR, DIMISHED)
    }
}
