package cc.zengtian.mtt.theory

import kotlinx.serialization.Serializable

@Serializable
class Scale private constructor(val name: String, val steps: List<Int>) {

    @Suppress("MemberVisibilityCanBePrivate")
    companion object {
        val MAJOR = Scale("Major(Ionian)", 2, 2, 1, 2, 2, 2)
        val DORIAN = Scale("Dorian", 2, 1, 2, 2, 2, 1)
        val PHRYGIAN = Scale("Phrygian", 1, 2, 2, 2, 1, 2)
        val LYDIAN = Scale("Lydian", 2, 2, 2, 1, 2, 2)
        val MIXOLYDIAN = Scale("Mixolydian", 2, 2, 1, 2, 2, 1)
        val MINOR = Scale("Minor(Aeolian)", 2, 1, 2, 2, 1, 2)
        val LOCRIAN = Scale("Locrian", 1, 2, 2, 1, 2, 2)
        val HARMONIC_MINOR = Scale("Harmonic Minor", 2, 1, 2, 2, 1, 3)
        val MELODIC_MINOR_ASCEND = Scale("Melodic Minor Ascending", 2, 1, 2, 2, 2, 2)
        val PENTATONIC = Scale("Pentatonic", 2, 2, 3, 2)
        val CHROMATIC = Scale("Chromatic", 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1)

        private val BUILT_INS = listOf(
            MAJOR,
            DORIAN,
            PHRYGIAN,
            LYDIAN,
            MIXOLYDIAN,
            MINOR,
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
        val majorToRoot = MAJOR.stepsToRoot
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
