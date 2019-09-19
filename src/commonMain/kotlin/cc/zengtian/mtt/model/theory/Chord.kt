package cc.zengtian.mtt.model.theory

/**
 * @param offsets offsets to root note
 */
open class RelativeChord(offsets: Set<Int>) {

    companion object {
    }

    constructor(vararg offsets: Int) : this(offsets.toSet())

    private val steps: Set<Int>

    init {
        steps = offsets.map {
            val module = it % 12
            if (module < 0) {
                return@map module + 12
            }
            module
        }.toSet()
        require(steps.isNotEmpty()) { "chord must have at least 2 notes" }
        require(steps.min()!! > 0) { "offsets can't have root" }
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

    val annotations: List<ChordAnnotation> by lazy {
        val result = mutableListOf<ChordAnnotation>()
        val inversionsWithItself = inversions.toMutableList().apply { add(0, this@RelativeChord) }
        // all common chords(defined in ChordType) with their inversions
        ChordType.values().forEach { chordType ->
            inversionsWithItself.forEachIndexed { idx, chord ->
                if (chord.steps == chordType.steps) {
                    val inversion = if (idx == 0) {
                        0
                    } else {
                        size - idx
                    }
                    result.add(ChordAnnotation(chordType, inversion))
                }
            }
        }
        // TODO suspended, six
        // minor 7th flat 5
        // TODO ??
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
        actualNotes.filterIndexed { idx, _ -> idx > 0 }.map { it.getStepsToLeft(actualNotes[0]) }.toSet())

}

class Chord private constructor(val root : Note, val others : Set<Note>) : ActualChord(root.actual,
    others.map { it.actual.getStepsToLeft(root.actual) }.toSet()) {

    val notes = others.toMutableSet().apply { add(root) }.toSet()

    constructor(vararg notes : Note) : this(notes[0], notes.filterIndexed { index, _ -> index > 0 }.toSet())

    init {
        require(notes.map { it.actual }.toSet().size == others.size + 1)
    }

    fun beforeInversionRootNote(annotation: ChordAnnotation) : Note {
        require(annotations.contains(annotation))
        val beforeActualNote = annotation.getBeforeInversionRootActualNote(root.actual)
        return notes.find { it.actual == beforeActualNote } ?: throw IllegalStateException()
    }
}

class ChordAnnotation(val chordType: ChordType,
                      val inversion: Int) {
    init {
        require(inversion < chordType.size) {"invalid inversion: $inversion"}
    }

    val beforeInversionRootStep : Int by lazy {
        if (inversion == 0) {
            0
        }else {
            chordType.steps.sorted()[inversion - 1]
        }
    }

    fun getBeforeInversionRootActualNote(root : ActualNote) : ActualNote = root.getByOffset(-beforeInversionRootStep)
}

enum class LeadSheetSymbol {

}

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

    fun realizeToLeadSheet(key: Key) {

    }
}

// TODO change to normal class so new chord type can be added?
enum class ChordType(val steps: Set<Int>) {
    MAJOR_TRIAD(4, 7),
    MINOR_TRIAD(3, 7),
    AUGMENTED_TRIAD(4, 8),
    DIMISHED_TRIAD(3, 6),
    MAJOR_MAJOR_7TH(4, 7, 11),
    DOMINANT_7TH(4, 7, 10),
    MINOR_MAJOR_7TH(3, 7, 11),
    HALF_DIMISHED_7TH(3, 6, 10),
    DIMISHED_7TH(3, 6, 9);

    companion object {
        val triads: List<ChordType> by lazy { values().filter { it.isTriad() } }
        val sevens: List<ChordType> by lazy { values().filter { it.is7th() } }
        fun valuesOfSize(chordSize: Int): List<ChordType> {
            return values().filter { it.steps.size + 1 == chordSize }
        }

    }

    fun of(root: ActualNote) : ActualChord = ActualChord(root, steps)

    constructor(vararg stepsArr: Int) : this(stepsArr.toSet())

    val size : Int by lazy { steps.size + 1 }

    fun isTriad(): Boolean = steps.size == 2

    fun is7th(): Boolean = steps.size == 3
}
