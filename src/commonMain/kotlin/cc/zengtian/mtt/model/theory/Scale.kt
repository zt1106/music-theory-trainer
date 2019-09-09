package cc.zengtian.mtt.model.theory

class Scale(private val name: String, val steps: List<Int>) {

    @Suppress("MemberVisibilityCanBePrivate")
    companion object {
        val IONIAN = Scale("IONIAN", listOf(2, 2, 1, 2, 2, 2))
        val DORIAN = Scale("DORIAN", listOf(2, 1, 2, 2, 2, 1))
        val PHRYGIAN = Scale("PHRYGIAN", listOf(1, 2, 2, 2, 1, 2))
        val LYDIAN = Scale("LYDIAN", listOf(2, 2, 2, 1, 2, 2))
        val MIXOLYDIAN = Scale("MIXOLYDIAN", listOf(2, 2, 1, 2, 2, 1))
        val AEOLIAN = Scale("AEOLIAN", listOf(2, 1, 2, 2, 1, 2))
        val LOCRIAN = Scale("LOCRIAN", listOf(1, 2, 2, 1, 2, 2))
        val HARMONIC_MINOR = Scale("HARMONIC_MINOR", listOf(2, 1, 2, 2, 1, 3))
        val MELODIC_MINOR_ASCEND = Scale("MELODIC_MINOR_UPPER", listOf(2, 1, 2, 2, 2, 2))
        val PENTATONIC = Scale("PENTATONIC", listOf(2, 2, 3, 2))
        val CHROMATIC = Scale("CHROMATIC", listOf(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1))

        val MAJOR = Scale("MAJOR", listOf(2, 2, 1, 2, 2, 2))
        val MINOR = Scale("NATURAL_MINOR", listOf(2, 1, 2, 2, 1, 2))

        private val BUILT_INS = listOf(
            IONIAN,
            DORIAN,
            PHRYGIAN,
            LYDIAN,
            MIXOLYDIAN,
            AEOLIAN,
            LOCRIAN,
            HARMONIC_MINOR,
            MELODIC_MINOR_ASCEND,
            PENTATONIC,
            CHROMATIC
        )
        fun builtInValues() = BUILT_INS
    }

    init {
        check(steps.isNotEmpty()) { "steps can't be empty" }
        check(steps.count { it <= 0 } == 0) { "step must > 0" }
        check(steps.sum() < 12) { "steps sum must < 12" }
    }

    private fun getRelativeStepsToRoot(): List<Int> {
        val inc = mutableListOf<Int>()
        inc.add(0)
        for (step in steps) {
            inc.add(inc[inc.size - 1] + step)
        }
        return inc
    }

    fun getRelativeStepsToMajor(): List<Pair<Int, Accidental?>> {
        val majorToRoot = IONIAN.getRelativeStepsToRoot()
        val thisToRoot = getRelativeStepsToRoot()
        val result = ArrayList<Pair<Int, Accidental?>?>(thisToRoot.size)
        for (idx in thisToRoot.indices) {
            result.add(null)
        }
        for (idx in thisToRoot.indices) {
            val distance = thisToRoot[idx]
            if (majorToRoot.contains(distance)) {
                result[idx] = Pair(majorToRoot.indexOf(distance), null)
            }
        }
        for (idx in result.indices) {
            if (result[idx] != null) {
                continue
            }
            val unresolved = thisToRoot[idx]
            val smallerInMajor = majorToRoot.find { unresolved - it == 1 }!!
            val biggerInMajor = majorToRoot.find { it - unresolved == 1 }!!
            if (thisToRoot.contains(smallerInMajor) && thisToRoot.contains(biggerInMajor)) {
                // TODO better ways to determine sharp or flat？
                result[idx] = Pair(majorToRoot.indexOf(biggerInMajor), Accidental.FLAT)
            } else if (!thisToRoot.contains(smallerInMajor) && !thisToRoot.contains(biggerInMajor)) {
                // TODO better ways to determine sharp or flat？
                result[idx] = Pair(majorToRoot.indexOf(biggerInMajor), Accidental.FLAT)
            } else if (thisToRoot.contains(smallerInMajor)) {
                result[idx] = Pair(majorToRoot.indexOf(biggerInMajor), Accidental.FLAT)
            } else {
                result[idx] = Pair(majorToRoot.indexOf(smallerInMajor), Accidental.SHARP)
            }
        }
        @Suppress("UNCHECKED_CAST")
        return result as List<Pair<Int, Accidental?>>
    }

    fun getNoteCount(): Int = steps.size + 1

    override fun toString(): String = name

    override fun equals(other: Any?): Boolean {
        if (other == null || other !is Scale) {
            return false
        }
        if (steps.size != other.steps.size) {
            return false
        }
        for (idx in steps.indices) {
            if (steps[idx] != other.steps[idx]) {
                return false
            }
        }
        return true
    }

    override fun hashCode(): Int {
        return steps.hashCode()
    }
}