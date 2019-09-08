package cc.zengtian.st.model

import cc.zengtian.st.value.IONIAN
import kotlin.math.absoluteValue

/**
 * Created by ZengTian on 2019/9/5.
 */
fun main() {
//    Key.values().forEach { key ->
//        BUILT_IN_SCALE.forEach { scale ->
//            println("$key $scale ${key.getNotesOfScale(scale)} ${key.getAccidentalCountOfScale(scale)}")
//        }
//    }
}

class Note(val wellTemperedNote: WellTemperedNote, val accidental: Accidental?) {

    init {
        require(!(getBeforeAccidentalWellTemperedNote().needResolve && accidental != null)) { "invalid note ${getBeforeAccidentalWellTemperedNote()} $accidental" }
        require(!(wellTemperedNote.needResolve && accidental == null)) { "invalid note ${getBeforeAccidentalWellTemperedNote()} $accidental" }
    }

    fun getBeforeAccidentalWellTemperedNote(): WellTemperedNote = wellTemperedNote.getByOffset(-accidental.getOffset())

    override fun toString(): String = accidental.getPrefix() + getBeforeAccidentalWellTemperedNote()
}

enum class Key(private val startingNote: Note) {
    C(Note(WellTemperedNote.C, null)),
    F(Note(WellTemperedNote.F, null)),
    B_FLAT(Note(WellTemperedNote.AB, Accidental.FLAT)),
    E_FLAT(Note(WellTemperedNote.DE, Accidental.FLAT)),
    A_FLAT(Note(WellTemperedNote.GA, Accidental.FLAT)),
    D_FLAT(Note(WellTemperedNote.CD, Accidental.FLAT)),
    C_SHARP(Note(WellTemperedNote.CD, Accidental.SHARP)),
    G_FLAT(Note(WellTemperedNote.FG, Accidental.FLAT)),
    F_SHARP(Note(WellTemperedNote.FG, Accidental.SHARP)),
    B(Note(WellTemperedNote.B, null)),
    C_FLAT(Note(WellTemperedNote.B, Accidental.FLAT)),
    E(Note(WellTemperedNote.E, null)),
    A(Note(WellTemperedNote.A, null)),
    D(Note(WellTemperedNote.D, null)),
    G(Note(WellTemperedNote.G, null));

    fun getNotesOfScale(scale: Scale): List<Note> {
        val majorScale = getMajorScaleNotes()
        val relativeToMajor = scale.getRelativeStepsToMajor()
        val result = mutableListOf<Note>()
        for (pair in relativeToMajor) {
            val noteInMajor = majorScale[pair.first]
            val accidentalToBeAdded = pair.second
            val noteInResult = Note(
                noteInMajor.wellTemperedNote.getByOffset(accidentalToBeAdded.getOffset()),
                Accidental.getByOffset(noteInMajor.accidental.getOffset() + accidentalToBeAdded.getOffset())
            )
            result.add(noteInResult)
        }
        return result
    }

    fun getAccidentalCountOfScale(scale: Scale): Int {
        val notes = getNotesOfScale(scale)
        return notes.count { it.accidental != null }
    }

    fun getAccidentalNotesOfScale(scale: Scale) : List<Note> = getNotesOfScale(scale).filter { it.accidental != null }

    private fun getMajorScaleNotes(): List<Note> {
        val startUnresolved = startingNote.getBeforeAccidentalWellTemperedNote()
        val unresolveds = mutableListOf(startUnresolved)
        var next = startUnresolved.getNextNoNeedResolveWellTemperedNote()
        while (!unresolveds.contains(next)) {
            unresolveds.add(next)
            next = next.getNextNoNeedResolveWellTemperedNote()
        }
        val wellTemperedNotes = startingNote.wellTemperedNote.getWellTemperedNotesForScale(IONIAN)
        val result = mutableListOf<Note>()
        for (idx in unresolveds.indices) {
            val unresolve = unresolveds[idx]
            val wellTemperedNote = wellTemperedNotes[idx]
            val accidental = Accidental.getByOffset(-wellTemperedNote.getOffset(unresolve))
            result.add(Note(wellTemperedNote, accidental))
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

    init {
        check(steps.isNotEmpty()) {"steps can't be empty"}
        check(steps.count { it <= 0 } == 0) {"step must > 0"}
        check(steps.sum() < 12) {"steps sum must < 12"}
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

class Interval(val num: Int, val quality: IntervalQuality) {

    override fun toString(): String = "${quality}_$num"
}

enum class IntervalQuality {
    AUGMENTED,
    MAJOR,
    MINOR,
    DIMISHED
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