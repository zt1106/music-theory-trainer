package cc.zengtian.mtt.model.theory

import cc.zengtian.mtt.model.theory.ActualNote.*
import cc.zengtian.mtt.model.theory.ChordAnnotation.*
import cc.zengtian.mtt.model.theory.IntervalQuality.*
import cc.zengtian.mtt.util.asSingletonList

/**
 * @param offsets offsets to root note
 */
open class RelativeChord(offsets: Set<Int>) {

    companion object {
        fun of(vararg offsets: Int) : RelativeChord {

            TODO()
        }
    }

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

    val annotation: List<ChordAnnotation> by lazy {
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
        emptyList<ChordAnnotation>()
    }

    private val toString: String by lazy {
        steps.toString()
    }

    override fun toString(): String = toString
}

interface ChordAnnotationChecker<T : ChordAnnotation> {
    fun isValid(t: T): Boolean
}

fun main() {
    val chord = ActualChord.of(C_, E_, G_)
    println(chord.annotation)
    chord.inversions.forEach { println(it) }
    val set1 = mutableSetOf(1, 4, 5)
    val set2 = setOf(5, 4, 1)
    println(set1.hashCode())
    println(set2.hashCode())
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

enum class ChordAnnotation {
    MAJOR_TRIAD,
    MINOR_TRIAD,
    AUGMENTED_TRIAD,
    DIMISHED_TRIAD,
    MAJOR_MAJOR_7TH,
    DOMINANT_7TH,
    MINOR_MAJOR_7TH,
    HALF_DIMISHED_7TH;

    fun isTriad(): Boolean {
        TODO()
    }

    fun is7th(): Boolean {
        TODO()
    }
}

enum class ChordType

enum class ChordInversion

class ChordMeta

class ChordNotation

class ChordNotationConfig

enum class ChordNotationType
