package cc.zengtian.mtt.model.theory

import kotlinx.serialization.Serializable

@Serializable
class Scale private constructor(val name: String, val steps: List<Int>) {

    @Suppress("MemberVisibilityCanBePrivate")
    companion object {
        val IONIAN = Scale("IONIAN", 2, 2, 1, 2, 2, 2)
        val DORIAN = Scale("DORIAN", 2, 1, 2, 2, 2, 1)
        val PHRYGIAN = Scale("PHRYGIAN", 1, 2, 2, 2, 1, 2)
        val LYDIAN = Scale("LYDIAN", 2, 2, 2, 1, 2, 2)
        val MIXOLYDIAN = Scale("MIXOLYDIAN", 2, 2, 1, 2, 2, 1)
        val AEOLIAN = Scale("AEOLIAN", 2, 1, 2, 2, 1, 2)
        val LOCRIAN = Scale("LOCRIAN", 1, 2, 2, 1, 2, 2)
        val HARMONIC_MINOR = Scale("HARMONIC_MINOR", 2, 1, 2, 2, 1, 3)
        val MELODIC_MINOR_ASCEND = Scale("MELODIC_MINOR_UPPER", 2, 1, 2, 2, 2, 2)
        val PENTATONIC = Scale("PENTATONIC", 2, 2, 3, 2)
        val CHROMATIC = Scale("CHROMATIC", 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1)

        val MAJOR = Scale("MAJOR", 2, 2, 1, 2, 2, 2)
        val MINOR = Scale("NATURAL_MINOR", 2, 1, 2, 2, 1, 2)

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
    
    constructor(name: String, vararg stepsArr: Int) : this(name, stepsArr.toList())

    private val stepsToRoot: List<Int> by lazy {
        val inc = mutableListOf<Int>()
        inc.add(0)
        for (step in steps) {
            inc.add(inc[inc.size - 1] + step)
        }
        inc
    }

    val stepPairToMajor: List<Pair<Int, Accidental?>> by lazy {
        val majorToRoot = IONIAN.stepsToRoot
        val thisToRoot = stepsToRoot
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
        result as List<Pair<Int, Accidental?>>
    }

    val noteCount: Int by lazy { steps.size + 1 }

    init {
        check(steps.isNotEmpty()) { "steps can't be empty" }
        check(steps.count { it <= 0 } == 0) { "step must > 0" }
        check(steps.sum() < 12) { "steps sum must < 12" }
    }

    override fun toString(): String = name

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Scale) return false

        if (steps != other.steps) return false

        return true
    }

    override fun hashCode(): Int {
        return steps.hashCode()
    }
}
