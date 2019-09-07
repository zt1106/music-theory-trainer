package cc.zengtian.st.model

import kotlin.math.absoluteValue

/**
 * Created by ZengTian on 2019/9/5.
 */
fun main() {
//    println(IntrinsicNote.G.getInrinsicNotesForScale(ScaleSteps.IONIAN))
//    println(Note(IntrinsicNote.FG, Accidental.FLAT))
//    println(IntrinsicNote.C.getInrinsicNotesForScale(ScaleSteps.IONIAN))
//    println(IntrinsicNote.D.getInrinsicNotesForScale(ScaleSteps.DORIAN))
//    println(IntrinsicNote.E.getInrinsicNotesForScale(ScaleSteps.PHRYGIAN))
//    println(IntrinsicNote.F.getInrinsicNotesForScale(ScaleSteps.LYDIAN))
//    println(IntrinsicNote.G.getInrinsicNotesForScale(ScaleSteps.MIXOLYDIAN))
//    println(IntrinsicNote.A.getInrinsicNotesForScale(ScaleSteps.AEOLIAN))
//    println(IntrinsicNote.B.getInrinsicNotesForScale(ScaleSteps.LOCRIAN))
//    println(IntrinsicNote.A.getInrinsicNotesForScale(ScaleSteps.HARMONIC_MINOR))
//    println(IntrinsicNote.A.getInrinsicNotesForScale(ScaleSteps.MELODIC_MINOR_UPPER))
//    println(IntrinsicNote.C.getInrinsicNotesForScale(ScaleSteps.PENTATONIC))
//    println(IntrinsicNote.C.getInrinsicNotesForScale(ScaleSteps.CHROMATIC))
//    println("============-=")

    Key.values().forEach {
        println(it.getMajorScaleNotes())
    }
}

class Note(
    val intrinsicNote: IntrinsicNote,
    val accidental: Accidental?
) {

    fun getUnresolvedIntrinsicNote(): IntrinsicNote {
        return intrinsicNote.getByOffset(-accidental.getOffset())
    }

    override fun toString(): String {
        return accidental.toString() + getUnresolvedIntrinsicNote()
    }
}

enum class Key(private val startingNote: Note) {
    C(Note(IntrinsicNote.C, null)),
    F(Note(IntrinsicNote.F, null)),
    B_FLAT(Note(IntrinsicNote.AB, Accidental.FLAT)),
    E_FLAT(Note(IntrinsicNote.DE, Accidental.FLAT)),
    A_FLAT(Note(IntrinsicNote.GA, Accidental.FLAT)),
    D_FLAT(Note(IntrinsicNote.CD, Accidental.FLAT)),
    C_SHARP(Note(IntrinsicNote.CD, Accidental.SHARP)),
    G_FLAT(Note(IntrinsicNote.FG, Accidental.FLAT)),
    F_SHARP(Note(IntrinsicNote.FG, Accidental.SHARP)),
    B(Note(IntrinsicNote.B, null)),
    C_FLAT(Note(IntrinsicNote.B, Accidental.FLAT)),
    E(Note(IntrinsicNote.E, null)),
    A(Note(IntrinsicNote.A, null)),
    D(Note(IntrinsicNote.D, null)),
    G(Note(IntrinsicNote.G, null));

    fun getNotes(scaleSteps: ScaleSteps): List<Note> {
        if (scaleSteps == ScaleSteps.IONIAN) {
            return getMajorScaleNotes()
        }
//        val intrinsicNotes = startingNote.intrinsicNote.getInrinsicNotesForScale(scaleSteps)
//        val result = ArrayList<Note>(intrinsicNotes.size)
//        result[0] = startingNote
//        for (idx in intrinsicNotes.indices) {
//            if (idx == 0) {
//                continue
//            }
//            val leftNote = result[idx - 1]
//            val leftAccidental = leftNote.accidental
//            val rightIntrinsicNote = intrinsicNotes[idx]
//            if (rightIntrinsicNote.needResolve) {
//                val possibleOne = rightIntrinsicNote.getPossibleOneOffsetNotes()
//
//            }
//        }
            TODO()
    }

    fun getMajorScaleNotes() : List<Note> {
        val startUnresolved = startingNote.getUnresolvedIntrinsicNote()
        val unresolveds = mutableListOf(startUnresolved)
        var next = startUnresolved.getNextnoNeedResolveIntrinsicNote()
        while (!unresolveds.contains(next)) {
            unresolveds.add(next)
            next = next.getNextnoNeedResolveIntrinsicNote()
        }
        val intrinsicNotes = startingNote.intrinsicNote.getInrinsicNotesForScale(ScaleSteps.IONIAN)
        val result = mutableListOf<Note>()
        for (idx in unresolveds.indices) {
            val unresolve = unresolveds[idx]
            val intrinsic = intrinsicNotes[idx]
            val accidental = getAccidentalByOffset(-intrinsic.getOffset(unresolve))
            result.add(Note(intrinsic, accidental))
        }
        return result
    }
}

enum class IntrinsicNote(val needResolve: Boolean) {
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
     * right: > 0
     * left: < 0
     */
    fun getByOffset(offset: Int): IntrinsicNote {
        val result = (ordinal + offset) % 12
        return ofIdx(result.absoluteValue)
    }

    fun getOffset(another: IntrinsicNote) : Int {
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

    fun ofIdx(idx: Int): IntrinsicNote {
        this.ordinal
        return values().find { it.ordinal == idx }!!
    }

    fun getInrinsicNotesForScale(scaleSteps: ScaleSteps): List<IntrinsicNote> {
        val list = mutableListOf<IntrinsicNote>()
        list.add(this)
        for (step in scaleSteps.steps) {
            list.add(list[list.size - 1].getByOffset(step))
        }
        return list
    }

    fun getNextnoNeedResolveIntrinsicNote() : IntrinsicNote{
        var cur = this.getByOffset(1)
        while (cur != this) {
            if (!cur.needResolve) {
                return cur
            }
            cur = cur.getByOffset(1)
        }
        throw IllegalStateException()
    }

    fun getPossibleOneOffsetNotes(): List<Note> {
        return listOf(
            Note(getByOffset(1), Accidental.FLAT),
            Note(getByOffset(-1), Accidental.SHARP)
        )
    }

    fun getPossibleTwoOffsetNotes(): List<Note> {
        return listOf(
            Note(getByOffset(2), Accidental.DOUBLE_FLAT),
            Note(getByOffset(-2), Accidental.DOUBLE_SHARP)
        )
    }
}

enum class ScaleSteps(val steps: List<Int>) {
    IONIAN(listOf(2, 2, 1, 2, 2, 2)),
    DORIAN(listOf(2, 1, 2, 2, 2, 1)),
    PHRYGIAN(listOf(1, 2, 2, 2, 1, 2)),
    LYDIAN(listOf(2, 2, 2, 1, 2, 2)),
    MIXOLYDIAN(listOf(2, 2, 1, 2, 2, 1)),
    AEOLIAN(listOf(2, 1, 2, 2, 1, 2)),
    LOCRIAN(listOf(1, 2, 2, 1, 2, 2)),
    HARMONIC_MINOR(listOf(2, 1, 2, 2, 1, 3)),
    MELODIC_MINOR_UPPER(listOf(2, 1, 2, 2, 2, 2)),
    PENTATONIC(listOf(2, 2, 3, 2)),
    CHROMATIC(listOf(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1))

}

class Interval(val num: Int, val quality: IntervalQuality) {

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

enum class Accidental(val type: AccidentalType) {
    SHARP(AccidentalType.SHARP),
    FLAT(AccidentalType.FLAT),
    DOUBLE_SHARP(AccidentalType.SHARP),
    DOUBLE_FLAT(AccidentalType.FLAT);

    override fun toString(): String {
        return super.toString() + "_"
    }
}

fun Accidental?.getOffset(): Int {
    return when (this) {
        Accidental.DOUBLE_FLAT -> -2
        Accidental.SHARP -> 1
        Accidental.FLAT -> -1
        Accidental.DOUBLE_SHARP -> 2
        null -> 0
    }
}

fun getAccidentalByOffset(offset: Int): Accidental? {
    return when (offset) {
        -2 -> Accidental.DOUBLE_FLAT
        1 -> Accidental.SHARP
        -1 -> Accidental.FLAT
        2 -> Accidental.DOUBLE_SHARP
        0 -> null
        else -> throw IllegalArgumentException(offset.toString())
    }
}

fun Accidental?.toString(): String {
    if (this == null) {
        return ""
    }
    return this.toString()
}