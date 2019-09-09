package cc.zengtian.st.model

import kotlin.math.absoluteValue

/**
 * Created by ZengTian on 2019/9/5.
 */
fun main() {
//    Key.values().forEach { key ->
//        Scale.builtInValues().forEach { scale ->
//            println("$key $scale ${key.getNotesOfScale(scale)} accidental count: ${key.getAccidentalCountOfScale(scale)}")
//        }
//    }
//    Interval.values().forEach { println(it) }
    Interval.of(Note.of(WellTemperedNote.F, null), Note.of(WellTemperedNote.B, Accidental.DOUBLE_SHARP)).println()

}

class Note private constructor(val wellTemperedNote: WellTemperedNote, val accidental: Accidental?) {

    companion object {
        private val ALL_NOTES = mutableMapOf<Pair<WellTemperedNote, Accidental?>, Note>().apply {
            WellTemperedNote.values().forEach { wellTemperedNote ->
                Accidental.forEachIncludingNull { accidental ->
                    try {
                        put(wellTemperedNote to accidental, Note(wellTemperedNote, accidental))
                    } catch (e: Exception) {
                    }
                }
            }
        }.toMap()
        val ALL_NON_DOUBLE_ACCIDENTAL_NOTES = ALL_NOTES.filterValues { it.accidental.getOffset().absoluteValue < 2 }
        fun of( wellTemperedNote: WellTemperedNote,  accidental: Accidental?) : Note {
            return ALL_NOTES[wellTemperedNote to accidental] ?: throw IllegalArgumentException()
        }
    }

    init {
        require(!(getBeforeAccidentalWellTemperedNote().needResolve && accidental != null)) { "invalid note ${getBeforeAccidentalWellTemperedNote()} $accidental" }
        require(!(wellTemperedNote.needResolve && accidental == null)) { "invalid note ${getBeforeAccidentalWellTemperedNote()} $accidental" }
    }

    fun getBeforeAccidentalWellTemperedNote(): WellTemperedNote = wellTemperedNote.getByOffset(-accidental.getOffset())

    fun getKey() : Key? = Key.values().find { it.startingNote == this }

    override fun toString(): String = accidental.getPrefix() + getBeforeAccidentalWellTemperedNote()
}

enum class Key(val startingNote: Note) {
    C(Note.of(WellTemperedNote.C, null)),
    F(Note.of(WellTemperedNote.F, null)),
    B_FLAT(Note.of(WellTemperedNote.AB, Accidental.FLAT)),
    E_FLAT(Note.of(WellTemperedNote.DE, Accidental.FLAT)),
    A_FLAT(Note.of(WellTemperedNote.GA, Accidental.FLAT)),
    D_FLAT(Note.of(WellTemperedNote.CD, Accidental.FLAT)),
    C_SHARP(Note.of(WellTemperedNote.CD, Accidental.SHARP)),
    G_FLAT(Note.of(WellTemperedNote.FG, Accidental.FLAT)),
    F_SHARP(Note.of(WellTemperedNote.FG, Accidental.SHARP)),
    B(Note.of(WellTemperedNote.B, null)),
    C_FLAT(Note.of(WellTemperedNote.B, Accidental.FLAT)),
    E(Note.of(WellTemperedNote.E, null)),
    A(Note.of(WellTemperedNote.A, null)),
    D(Note.of(WellTemperedNote.D, null)),
    G(Note.of(WellTemperedNote.G, null));

    companion object {
        private val CACHED_NOTES = mutableMapOf<Pair<Key, Scale>, List<Note>>()
    }

    fun getNotesOfScale(scale: Scale): List<Note> {
        if (CACHED_NOTES.containsKey(this to scale)) {
            return CACHED_NOTES[this to scale]!!
        }
        val majorScale = getMajorScaleNotes()
        val relativeToMajor = scale.getRelativeStepsToMajor()
        val result = mutableListOf<Note>()
        for (pair in relativeToMajor) {
            val noteInMajor = majorScale[pair.first]
            val accidentalToBeAdded = pair.second
            val noteInResult = Note.of(
                noteInMajor.wellTemperedNote.getByOffset(accidentalToBeAdded.getOffset()),
                Accidental.getByOffset(noteInMajor.accidental.getOffset() + accidentalToBeAdded.getOffset())
            )
            result.add(noteInResult)
        }
        CACHED_NOTES[this to scale] = result
        return result
    }

    fun getAccidentalCountOfScale(scale: Scale): Int {
        val notes = getNotesOfScale(scale)
        return notes.count { it.accidental != null }
    }

    fun getAccidentalNotesOfScale(scale: Scale): List<Note> = getNotesOfScale(scale).filter { it.accidental != null }

    private fun getMajorScaleNotes(): List<Note> {
        val startUnresolved = startingNote.getBeforeAccidentalWellTemperedNote()
        val unresolveds = mutableListOf(startUnresolved)
        var next = startUnresolved.getNextNoNeedResolveWellTemperedNote()
        while (!unresolveds.contains(next)) {
            unresolveds.add(next)
            next = next.getNextNoNeedResolveWellTemperedNote()
        }
        val wellTemperedNotes = startingNote.wellTemperedNote.getWellTemperedNotesForScale(Scale.IONIAN)
        val result = mutableListOf<Note>()
        for (idx in unresolveds.indices) {
            val unresolve = unresolveds[idx]
            val wellTemperedNote = wellTemperedNotes[idx]
            val accidental = Accidental.getByOffset(-wellTemperedNote.getOffset(unresolve))
            result.add(Note.of(wellTemperedNote, accidental))
        }
        return result
    }
}

/**
 * @param needResolve whether need to be transformed to a sharp or flat note in music theory
 * represent 12 well tempered notes
 */
enum class WellTemperedNote(val needResolve: Boolean) {
    C(false),
    CD(true),
    D(false),
    DE(true),
    E(false),
    F(false),
    FG(true),
    G(false),
    GA(true),
    A(false),
    AB(true),
    B(false);


    /**
     * high: > 0
     * low: < 0
     */
    fun getByOffset(offset: Int): WellTemperedNote {
        val result = (ordinal + offset) % 12
        return if (result >= 0) {
            ofIdx(result)
        } else {
            ofIdx(result + 12)
        }
    }

    fun getOffset(another: WellTemperedNote): Int {
        if (another == this) {
            return 0
        }
        val rightDistance = if (another.ordinal > this.ordinal) {
            another.ordinal - this.ordinal
        } else {
            another.ordinal + 12 - this.ordinal
        }
        val leftDistance = if (another.ordinal < this.ordinal) {
            this.ordinal - another.ordinal
        } else {
            this.ordinal + 12 - another.ordinal
        }
        return if (rightDistance < leftDistance) {
            rightDistance
        } else {
            -leftDistance
        }
    }

    private fun ofIdx(idx: Int): WellTemperedNote = values()[idx]

    fun getWellTemperedNotesForScale(scale: Scale): List<WellTemperedNote> {
        val list = mutableListOf<WellTemperedNote>()
        list.add(this)
        for (step in scale.steps) {
            list.add(list[list.size - 1].getByOffset(step))
        }
        return list
    }

    fun getNextNoNeedResolveWellTemperedNote(): WellTemperedNote {
        var cur = this.getByOffset(1)
        while (cur != this) {
            if (!cur.needResolve) {
                return cur
            }
            cur = cur.getByOffset(1)
        }
        throw IllegalStateException()
    }
}

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

class Interval private constructor(val num: Int, val quality: IntervalQuality) {

    companion object {
        private val ALL_INTERVALS = mutableMapOf<Pair<Int, IntervalQuality>, Interval>().apply {
            for (i in 1..8) {
                if (i == 1 || i == 4 || i == 5 || i == 8) {
                    IntervalQuality.valuesOf1458().forEach { q ->
                        this[i to q] = Interval(i, q)
                    }
                } else {
                    IntervalQuality.valuesOf2367().forEach { q ->
                        this[i to q] = Interval(i, q)
                    }
                }
            }
        }.toMap()

        fun values() = ALL_INTERVALS

        fun of(num: Int, quality: IntervalQuality) : Interval {
            return ALL_INTERVALS[num to quality] ?: throw IllegalArgumentException()
        }

        fun of(from: WellTemperedNote, to: WellTemperedNote) : Interval {
            TODO()
        }

        fun of(from: Note, to: Note): Interval? {
            var offset = 0
            val fromKey = from.getKey() ?: TODO()
            val fromScale = fromKey.getNotesOfScale(Scale.MAJOR)
            val fromScaleBeforeACC = fromScale.map { it.getBeforeAccidentalWellTemperedNote() }
            val toBeforeACC = to.getBeforeAccidentalWellTemperedNote()
            val idx = fromScaleBeforeACC.indexOf(toBeforeACC)
            return of1BaseIndexInMajorScale(idx + 1, to.accidental.getOffset() - fromScale[idx].accidental.getOffset())
        }

        private fun of1BaseIndexInMajorScale(idx: Int, offset: Int) : Interval? {
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

        private fun is1458(i: Int) : Boolean{
            check(i in 1..8)
            return (i == 1 || i == 4 || i == 5 || i == 8)
        }
    }

    fun reverse() : Interval {
        TODO()
    }

    fun getPhysicalInterval() : Int {
        TODO()
    }

    override fun toString(): String = "${quality}_$num"
}

fun Any?.println() {
    this?.apply { println(this) }
}

enum class IntervalQuality {
    AUGMENTED,
    MAJOR,
    PERFECT,
    MINOR,
    DIMISHED;

    companion object {
        fun valuesOf1458() : List<IntervalQuality> = listOf(AUGMENTED, PERFECT, DIMISHED)
        fun valuesOf2367() : List<IntervalQuality> = listOf(AUGMENTED, MAJOR, MINOR, DIMISHED)
    }
}

enum class AccidentalType {
    SHARP,
    FLAT
}

class Chord

@Suppress("unused")
enum class Accidental(val offset: Int) {

    SHARP(1),
    FLAT(-1),
    DOUBLE_SHARP(2),
    DOUBLE_FLAT(-2);

    companion object {
        fun getByOffset(offset: Int): Accidental? {
            check(offset.absoluteValue <= 2) { "wrong offset $offset" }
            return values().find { it.offset == offset }
        }

        fun forEachIncludingNull(action: (Accidental?) -> Unit) {
            for (element in values()) action(element)
            action(null)
        }
    }

    fun getPrefix(): String = super.toString() + "_"

    fun getType(): AccidentalType {
        return if (offset > 0) {
            AccidentalType.SHARP
        } else {
            AccidentalType.FLAT
        }
    }
}

fun Accidental?.getOffset(): Int = this?.offset ?: 0

fun Accidental?.getPrefix(): String = this?.getPrefix() ?: ""

operator fun Accidental?.unaryMinus() : Accidental? = this?.let { Accidental.getByOffset(-it.offset) }
