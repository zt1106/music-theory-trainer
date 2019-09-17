package cc.zengtian.mtt.model.theory

import cc.zengtian.mtt.model.theory.ActualNote.*
import cc.zengtian.mtt.model.theory.ChordType.*
import cc.zengtian.mtt.model.theory.IntervalQuality.*
import cc.zengtian.mtt.util.asSingletonList

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
        val rootAdded = steps.toMutableList().apply { add(0, 0) }.toList()
        repeat(steps.size) {
            val idx = it + 1
            val steps = rootAdded.map { step -> step - rootAdded[idx] }.filter { step -> step != 0 }
            result.add(RelativeChord(steps.toSet()))
        }
        result
    }

    val annotations: List<ChordType> by lazy {
        if (size == 3) {
            // common triads without inversion (major, minor, dimished, augmented)
            if (hasIntervals(Interval.of(3, MINOR), Interval.of(5, PERFECT))) {
                return@lazy MINOR_TRIAD.asSingletonList()
            }
            if (hasIntervals(Interval.of(3, MAJOR), Interval.of(5, PERFECT))) {
                return@lazy MAJOR_TRIAD.asSingletonList()
            }
            if (hasIntervals(Interval.of(3, MINOR), Interval.of(4, PERFECT))) {
                return@lazy DIMISHED_TRIAD.asSingletonList()
            }
            if (hasIntervals(Interval.of(3, MINOR), Interval.of(7, DIMISHED))) {
                return@lazy AUGMENTED_TRIAD.asSingletonList()
            }
            // common triads first inversion TODO
            // common triads second inversion TODO
            // 7th chord omits 5th 3rd TODO
        }
        if (size == 4) {
            // common 7th chords without inversion
            if (hasIntervals(Interval.of(7, MAJOR))) {
                if (hasIntervals(Interval.of(3, MAJOR), Interval.of(5, PERFECT))) {
                    return@lazy MAJOR_MAJOR_7TH.asSingletonList()
                }
                if (hasIntervals(Interval.of(3, MINOR), Interval.of(5, PERFECT))) {
                    return@lazy MINOR_MAJOR_7TH.asSingletonList()
                }
            }
            if (hasIntervals(Interval.of(7, MINOR))) {
                if (hasIntervals(Interval.of(3, MAJOR), Interval.of(5, PERFECT))) {
                    return@lazy DOMINANT_7TH.asSingletonList()
                }
                if (hasIntervals(Interval.of(3, MINOR), Interval.of(4, PERFECT))) {
                    return@lazy HALF_DIMISHED_7TH.asSingletonList()
                }
            }
            // TODO 7th chord inversions
        }
        emptyList<ChordType>()
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

interface ChordAnnotationChecker<T : ChordType> {
    fun isValid(t: T): Boolean
}

fun main() {
    val chord = ActualChord.of(C_, DE, FG, A_)
    println(chord.annotations)
    chord.inversions.forEach { println(it) }
}

open class ActualChord constructor(val actualNote: ActualNote, steps: Set<Int>) : RelativeChord(steps) {
    companion object {
        fun of(vararg actualNotes: ActualNote): ActualChord {
            require(actualNotes.size >= 2)
            val root = actualNotes[0]
            val offsets = actualNotes.filterIndexed { idx, _ -> idx > 0 }.map { it.getStepsToLeft(root) }.toSet()
            return ActualChord(root, offsets)
        }
    }
}

class Chord private constructor(private val rootNote: Note, steps: Set<Int>) : ActualChord(rootNote.actual, steps) {
    companion object {
        fun of(vararg note: Note): Chord {
            TODO()
        }
    }
}

enum class ChordAnnotation(val chordType: ChordType,
                           val inversion: Int) {

}

enum class ChordType(val steps: Set<Int>) {
    MAJOR_TRIAD(setOf(4, 7)),
    MINOR_TRIAD(setOf(3, 7)),
    AUGMENTED_TRIAD(setOf(4, 8)),
    DIMISHED_TRIAD(setOf(3, 6)),
    MAJOR_MAJOR_7TH(setOf(4, 7, 11)),
    DOMINANT_7TH(setOf(4, 7, 10)),
    MINOR_MAJOR_7TH(setOf(3, 7, 11)),
    HALF_DIMISHED_7TH(setOf(3, 6, 10));

    companion object {
        val triads: List<ChordType> by lazy { values().filter { it.isTriad() } }
        val sevens: List<ChordType> by lazy { values().filter { it.is7th() } }
        fun valuesOfSize(chordSize : Int) : List<ChordType> {
            return values().filter { it.steps.size + 1 == chordSize }
        }
    }

    fun isTriad(): Boolean = steps.size == 2

    fun is7th(): Boolean = steps.size == 3
}

// TODO dimished 7 special case, annotations size should be 4