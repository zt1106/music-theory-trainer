package cc.zengtian.mtt.theory

import cc.zengtian.mtt.config.ChordAnnotationConfig
import cc.zengtian.mtt.theory.SuspensionType.*
import cc.zengtian.mtt.util.containsNot
import kotlinx.serialization.Serializable

/**
 * @param offsets offsets to root note
 */
open class RelativeChord(offsets: Set<Int>) {

    companion object {
    }

    constructor(vararg offsets: Int) : this(offsets.toSet())

    val steps: Set<Int>

    init {
        steps = offsets.map {
            val module = it % 12
            if (module < 0) {
                return@map module + 12
            }
            module
        }.toSet()
        require(steps.isNotEmpty()) { "chord must have at least 2 notes" }
        require(steps.min()!! > 0) { "offsets can't be root" }
    }

    private val size: Int by lazy { steps.size + 1 }

    private fun hasIntervals(vararg intervals: Interval): Boolean {
        return intervals.all { steps.contains(it.physicalStep) }
    }

    val inversions: List<RelativeChord> by lazy {
        val result = mutableListOf<RelativeChord>()
        val rootAdded = steps.toMutableList().apply {
            sort()
            add(0, 0)
        }.toList()
        repeat(steps.size) {
            val idx = it + 1
            val steps = rootAdded.map { step -> step - rootAdded[idx] }.filter { step -> step != 0 }
            result.add(RelativeChord(steps.toSet()))
        }
        result
    }

    val annotation: ChordAnnotation? by lazy {
        if (annotations.isNotEmpty()) {
            annotations[0]
        } else {
            null
        }
    }

    val annotations: List<ChordAnnotation> by lazy {
        val result = mutableListOf<ChordAnnotation>()
        val inversionsWithItself = inversions.toMutableList().apply { add(0, this@RelativeChord) }
        // all common chords(defined in ChordType) with their inversions
        ChordSonority.enabledBuiltInValues.forEach { sonority ->
            val omissionStrategy = sonority.omissionStrategy
            val mustHave = steps.filter { omissionStrategy.steps.containsNot(it) }
            val selfWithInversions = if (sonority.inverseable) {
                inversionsWithItself
            } else {
                listOf(this)
            }
            selfWithInversions.forEachIndexed { idx, chord ->


            }



            inversionsWithItself.forEachIndexed { idx, chord ->
                if (chord.steps == sonority.steps) {
                    val inversion = if (idx == 0) {
                        0
                    } else {
                        size - idx
                    }
                    result.add(ChordAnnotation(sonority, inversion))
                }
            }
        }
        result.sortBy { it.inversion }
        result
    }

    private val toString: String by lazy {
        steps.toString()
    }

    override fun toString(): String = toString

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is RelativeChord) return false

        if (steps != other.steps) return false

        return true
    }

    override fun hashCode(): Int {
        return steps.hashCode()
    }
}

open class ActualChord constructor(val actualNote: ActualNote, steps: Set<Int>) : RelativeChord(steps) {
    companion object {
    }

    constructor(vararg actualNotes: ActualNote) : this(actualNotes[0],
            actualNotes.filterIndexed { idx, _ -> idx > 0 }.map { it.getStepsToLeft(actualNotes[0]) }.toSet()
    )

}

class Chord private constructor(val root: Note, val others: Set<Note>) : ActualChord(root.actual,
        others.map { it.actual.getStepsToLeft(root.actual) }.toSet()
) {

    val notes = others.toMutableSet().apply { add(root) }.toSet()

    constructor(vararg notes: Note) : this(notes[0], notes.filterIndexed { index, _ -> index > 0 }.toSet())

    constructor(notes: List<Note>) : this(notes[0], notes.filterIndexed { index, _ -> index > 0 }.toSet())

    init {
        require(notes.map { it.actual }.toSet().size == others.size + 1)
    }

    fun beforeInversionRootNote(annotation: ChordAnnotation): Note {
        require(annotations.contains(annotation))
        val beforeActualNote = annotation.getBeforeInversionRootActualNote(root.actual)
        return notes.find { it.actual == beforeActualNote } ?: throw IllegalStateException()
    }

    private val toString by lazy { "${root}_${others.map { it.toString() }.joinToString("_")}" }

    override fun toString(): String = toString
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false
        if (!super.equals(other)) return false

        other as Chord

        if (root != other.root) return false
        if (others != other.others) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + root.hashCode()
        result = 31 * result + others.hashCode()
        return result
    }
}

// TODO put sus there
class ChordAnnotation(
        val chordSonority: ChordSonority,
        val inversion: Int,
        val suspension: SuspensionType? = null
) {
    init {
        require(inversion < chordSonority.size) { "invalid inversion: $inversion" }
        if (!chordSonority.inverseable) {
            require(inversion == 0)
        }
        suspension?.apply { require(chordSonority.suspendable) }
    }

    val beforeInversionRootStep: Int by lazy {
        if (inversion == 0) {
            0
        } else {
            chordSonority.steps.sorted()[inversion - 1]
        }
    }

    fun getBeforeInversionRootActualNote(root: ActualNote): ActualNote = root.getByOffset(-beforeInversionRootStep)
}

class LeadSheetAnnotation(val root: Note, val annotation: ChordAnnotation) {

}

class RomanNumAnnotation

/**
 * figured bass notation for triads and 7ths without additional accidentals
 */
enum class FiguredBass(val size: Int, val inversion: Int) {
    NULL(3, 0),
    SIX(3, 1),
    SIX_FOUR(3, 2),
    SEVEN(4, 0),
    SIX_FIVE(4, 1),
    FOUR_THREE(4, 2),
    FOUR_TWO(4, 3);

    fun realizeToChordAnnotation(root: Note, key: Key): ChordAnnotation {
        TODO()
    }
}

// TODO change Note to enum class? if Note need to be inherited? NO!

// TODO change to normal class so new chord type can be added?

@Serializable
data class ChordSonority(val abbr: String,
                         val steps: Set<Int>,
                         val inverseable: Boolean = true,
                         val omissionStrategy: OmissionStrategy = OmissionStrategy()) {
    companion object {
        // built in chords implementation reference: https://en.wikibooks.org/wiki/Music_Theory/Complete_List_of_Chord_Patterns
        // not exactly the same though
        val builtInMap = mutableMapOf<ChordSonority, Boolean>()
        val enabledBuiltInValues
            get() = builtInMap.filter { it.value }.map { it.key }

        // major
        val MAJOR_TRIAD = ChordSonority(abbr = "", steps = stepsOf(3.n, 5.n)).also { builtInMap[it] = true }
        val MAJOR_MAJOR_7TH = ChordSonority(abbr = "maj7", steps = stepsOf(3.n, 5.n, 7.n), omissionStrategy = OmissionStrategy(stepsOf(5.n))).also { builtInMap[it] = true }
        val MAJOR_9TH = ChordSonority(abbr = "maj9", steps = stepsOf(3.n, 5.n, 7.n, 9.n), omissionStrategy = OmissionStrategy(stepsOf(5.n))).also { builtInMap[it] = true }
        val MAJOR_13TH = ChordSonority(abbr = "maj13", steps = stepsOf(3.n, 5.n, 7.n, 9.n, 11.n, 13.n), omissionStrategy = OmissionStrategy(stepsOf(5.n, 9.n, 11.n))).also { builtInMap[it] = true }
        val MAJOR_6TH = ChordSonority(abbr = "6", steps = stepsOf(3.n, 5.n, 6.n), omissionStrategy = OmissionStrategy(stepsOf(5.n), inverseable = false)).also { builtInMap[it] = true }
        val MAJOR_6TH_9TH = ChordSonority(abbr = "6/9", steps = stepsOf(3.n, 5.n, 6.n, 9.n), omissionStrategy = OmissionStrategy(stepsOf(5.n), inverseable = false)).also { builtInMap[it] = true }
        val MAJOR_LYDIAN = ChordSonority(abbr = "maj7#11", steps = stepsOf(3.n, 5.n, 7.n, 9.n, 11.n++, 13.n), omissionStrategy = OmissionStrategy(stepsOf(5.n, 7.n, 9.n, 13.n))).also { builtInMap[it] = true }
        val MAJOR_7TH_FLAT6 = ChordSonority(abbr = "maj7♭6", steps = stepsOf(3.n, 5.n, 7.n, 9.n, 11.n, 13.n--), omissionStrategy = OmissionStrategy(stepsOf(5.n, 7.n, 9.n, 11.n))).also { builtInMap[it] = true }
        // dominant
        val DOMINANT_7TH = ChordSonority(abbr = "7", steps = stepsOf(3.n, 5.n, 7.n--), omissionStrategy = OmissionStrategy(stepsOf(5.n))).also { builtInMap[it] = true }
        val DOMINANT_9TH = ChordSonority(abbr = "9", steps = stepsOf(3.n, 5.n, 7.n--, 9.n), omissionStrategy = OmissionStrategy(stepsOf(5.n))).also { builtInMap[it] = true }
        val DOMINANT_13TH = ChordSonority(abbr = "13", steps = stepsOf(3.n, 5.n, 7.n--, 9.n, 13.n), omissionStrategy = OmissionStrategy(stepsOf(5.n, 9.n))).also { builtInMap[it] = true }
        val DOMINANT_LYDIAN_7TH = ChordSonority(abbr = "7♯11", steps = stepsOf(3.n, 5.n, 7.n--, 11.n++, 13.n), omissionStrategy = OmissionStrategy(stepsOf(5.n, 9.n, 13.n))).also { builtInMap[it] = true }
        val DOMINANT_FLAT9 = ChordSonority(abbr = "7♭9", steps = stepsOf(3.n, 5.n, 7.n--, 9.n--), omissionStrategy = OmissionStrategy(stepsOf(5.n))).also { builtInMap[it] = true }
        val DOMINANT_SHARP9 = ChordSonority(abbr = "7♯9", steps = stepsOf(3.n, 5.n, 7.n--, 9.n++), omissionStrategy = OmissionStrategy(stepsOf(5.n))).also { builtInMap[it] = true }
        // TODO altered chord??
        // TODO 11th chord??
        // minor
        val MINOR_TRIAD = ChordSonority(abbr = "m", steps = stepsOf(3.n--, 5.n)).also { builtInMap[it] = true }
        val MINOR_MINOR_7TH = ChordSonority(abbr = "m7", steps = stepsOf(3.n--, 5.n, 7.n--), omissionStrategy = OmissionStrategy(stepsOf(5.n))).also { builtInMap[it] = true }
        val MINOR_MAJOR_7TH = ChordSonority(abbr = "m/maj7", steps = stepsOf(3.n--, 5.n, 7.n), omissionStrategy = OmissionStrategy(stepsOf(5.n))).also { builtInMap[it] = true }
        val MINOR_6TH = ChordSonority(abbr = "m6", steps = stepsOf(3.n--, 5.n, 6.n), omissionStrategy = OmissionStrategy(stepsOf(5.n), inverseable = false)).also { builtInMap[it] = true }
        val MINOR_9TH = ChordSonority(abbr = "m9", steps = stepsOf(3.n--, 5.n, 7.n--, 9.n), omissionStrategy = OmissionStrategy(stepsOf(5.n))).also { builtInMap[it] = true }
        val MINOR_11TH = ChordSonority(abbr = "m11", steps = stepsOf(3.n--, 5.n, 7.n--, 9.n, 11.n), omissionStrategy = OmissionStrategy(stepsOf(5.n, 9.n))).also { builtInMap[it] = true }
        val MINOR_13TH = ChordSonority(abbr = "m13", steps = stepsOf(3.n--, 5.n, 7.n--, 9.n, 13.n), omissionStrategy = OmissionStrategy(stepsOf(5.n, 9.n))).also { builtInMap[it] = true }
        // dimished
        val DIMISHED_TRIAD = ChordSonority(abbr = "dim", steps = stepsOf(3.n--, 5.n--)).also { builtInMap[it] = true }
        val DIMISHED_7TH = ChordSonority(abbr = "dim7", steps = stepsOf(3.n--, 5.n--, 6.n)).also { builtInMap[it] = true }
        val HALF_DIMISHED_7TH = ChordSonority(abbr = "m7♭5", steps = stepsOf(3.n--, 5.n--, 7.n--)).also { builtInMap[it] = true }
        // other
        val FIFTH = ChordSonority(abbr = "5", steps = stepsOf(5.n), inverseable = false).also { builtInMap[it] = true }
        val AUGMENTED_TRIAD = ChordSonority(abbr = "aug", steps = stepsOf(3.n, 5.n++)).also { builtInMap[it] = true }
        val AUGMENTED_MAJOR_7TH = ChordSonority(abbr = "maj7♯5", steps = stepsOf(3.n, 5.n++, 7.n)).also { builtInMap[it] = true }
    }

    init {
        require(steps.containsAll(omissionStrategy.steps.filter { it != 0 }))
        if (omissionStrategy.steps.isNotEmpty()) {
            require(omissionStrategy.leastCount < omissionStrategy.steps.size)
        }
    }

    val size: Int by lazy { steps.size + 1 }

    val suspendable = if (steps.contains(2) || steps.contains(5)) {
        false
    } else {
        steps.contains(4)
    }

    private val mustHaveSteps = steps.filter { omissionStrategy.steps.containsNot(it) }

    private fun annotateSuspension(chord: RelativeChord, config: ChordAnnotationConfig): SuspensionType? {
        if (!config.susEnabled || !suspendable) {
            return null
        }
        if (!chord.steps.containsAll(mustHaveSteps.filter { it != 4 })) {
            return null
        }
        chord.steps.filter { it == 2 || it == 4 || it == 5 }.apply {
            if (isEmpty() || size == 3) {
                return null
            }
            return if (size == 2) {
                if (contains(4)) {
                    null
                } else {
                    SUS2SUS4
                }
            } else {
                when {
                    contains(2) -> SUS2
                    contains(5) -> SUS4
                    else -> null
                }
            }
        }
    }

    fun annotate(chord: RelativeChord, config: ChordAnnotationConfig): ChordAnnotation? {
        // whether contains "must have" notes
        var suspensionType: SuspensionType? = null
//        if (!sonority.suspendable) {
//            if (!chord.steps.containsAll(mustHave)) {
//                return@forEachIndexed
//            }
//        }
//        // deal with suspension
//        else {

//        }
        // deal with non-optional extra notes
        TODO()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as ChordSonority

        if (steps != other.steps) return false

        return true
    }


    override fun hashCode(): Int {
        return steps.hashCode()
    }

    @Serializable
    data class OmissionStrategy(
            val steps: Set<Int> = emptySet(),
            val leastCount: Int = 0,
            val inverseable: Boolean = true
    )

    @Serializable
    data class AdditionStrategy(
            val steps: Set<Int>
    )

    @Serializable
    data class AlterationStrategy(
            val steps: Set<Int>
    )
}

enum class MajorScaleNoteNum(val num: Int? = null) {
    ONE(1),
    ONE_TWO(),
    TWO(2),
    TWO_THREE(),
    THREE(3),
    FOUR(4),
    FOUR_FIVE(),
    FIVE(5),
    FIVE_SIX(),
    SIX(6),
    SIX_SEVEN(),
    SEVEN(7);

    val step: Int
        get() = ordinal

    operator fun inc(): MajorScaleNoteNum {
        require(num != null)
        require(num != 3 && num != 7)
        return values()[step + 1]
    }

    operator fun dec(): MajorScaleNoteNum {
        require(num != null)
        require(num != 4 && num != 1)
        return values()[step - 1]
    }
}

private var Int.n: MajorScaleNoteNum
    get() {
        require(this > 0)
        val mod = this % 7
        return MajorScaleNoteNum.values().find { it.num == mod }!!
    }
    set(_) {}

private fun stepsOf(vararg nums: MajorScaleNoteNum): Set<Int> {
    return nums.map { it.step }.toSet()
}

enum class DiatonicChordType(val indexInScale: List<Int>) {
    TRIAD(0, 2, 4),
    SEVEN_TH(0, 2, 4, 6);

    constructor(vararg indexes: Int) : this(indexes.toList())

    val size = indexInScale.size
}

enum class DiatonicScaleType {
    MAJOR, MINOR
}

enum class SuspensionType {
    SUS2, SUS4, SUS2SUS4
}

interface ChordSonorityAnnotator<T> {
    fun validate(chord: RelativeChord): Boolean
    fun annotate(chord: RelativeChord): T
    fun tryAnnotate(chord: RelativeChord): T? {
        return if (validate(chord)) {
            annotate(chord)
        } else {
            null
        }
    }
}

class MustHaveNotesAnnotator<>
