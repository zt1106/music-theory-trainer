package cc.zengtian.mtt.model.theory

class Interval private constructor(val num: Int, val quality: IntervalQuality) {

    companion object {
        private val ALL_INTERVALS = mutableMapOf<Pair<Int, IntervalQuality>, Interval>().apply {
            for (i in 1..8) {
                if (is1458(i)) {
                    IntervalQuality.valuesOf1458().forEach { q -> this[i to q] = Interval(i, q) }
                } else {
                    IntervalQuality.valuesOf2367().forEach { q -> this[i to q] = Interval(i, q) }
                }
            }
        }.toMap()

        fun values() = ALL_INTERVALS

        fun of(num: Int, quality: IntervalQuality): Interval {
            return ALL_INTERVALS[num to quality] ?: throw IllegalArgumentException("$num $quality")
        }

        fun of(from: WellTemperedNote, to: WellTemperedNote): Interval {
            TODO()
        }

        fun of(from: Note, to: Note): Interval? {
            val fromAccidental = from.accidental
            val fromKey = from.getBeforeAccidentalWellTemperedNote().getKeys()[0]
            val fromScale = fromKey.getNotesOfScale(Scale.MAJOR)
            val fromScaleBeforeACC = fromScale.map { it.getBeforeAccidentalWellTemperedNote() }
            val toBeforeACC = to.getBeforeAccidentalWellTemperedNote()
            val idx = fromScaleBeforeACC.indexOf(toBeforeACC)
            return of1BaseIndexInMajorScale(idx + 1, to.accidental.getOffset() - fromScale[idx].accidental.getOffset() - fromAccidental.getOffset())
        }

        private fun of1BaseIndexInMajorScale(idx: Int, offset: Int): Interval? {
            return if (is1458(idx)) {
                when (offset) {
                    0 -> of(idx, IntervalQuality.PERFECT)
                    1 -> of(idx, IntervalQuality.AUGMENTED)
                    -1 -> of(idx, IntervalQuality.DIMISHED)
                    else -> null
                }
            } else {
                when (offset) {
                    0 -> of(idx, IntervalQuality.MAJOR)
                    1 -> of(idx, IntervalQuality.AUGMENTED)
                    -1 -> of(idx, IntervalQuality.MINOR)
                    -2 -> of(idx, IntervalQuality.DIMISHED)
                    else -> null
                }
            }
        }

        private fun is1458(i: Int): Boolean {
            check(i in 1..8)
            return (i == 1 || i == 4 || i == 5 || i == 8)
        }
    }

    fun getInversion(): Interval {
        val inverQuality = when (quality) {
            IntervalQuality.AUGMENTED -> IntervalQuality.DIMISHED
            IntervalQuality.DIMISHED -> IntervalQuality.AUGMENTED
            IntervalQuality.MINOR -> IntervalQuality.MAJOR
            IntervalQuality.MAJOR -> IntervalQuality.MINOR
            IntervalQuality.PERFECT -> IntervalQuality.PERFECT
        }
        return of(9 - num, inverQuality)
    }

    fun getPhysicalInterval(): Int {
        TODO()
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