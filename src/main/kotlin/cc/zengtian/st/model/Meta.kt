package cc.zengtian.st.model

/**
 * Created by ZengTian on 2019/9/5.
 */
fun main() {
//    IntrinsicNote.values().forEach { note ->
//        ScaleSteps.values().forEach { scale ->
//            run {
//                println("$note $scale --- ${note.getInrinsicNotesForScale(scale)}")
//            }
//        }
//    }
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
        return accidental.getPrefix() + getUnresolvedIntrinsicNote()
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
        val majorScale = getMajorScaleNotes()

        TODO()
    }

    fun getMajorScaleNotes(): List<Note> {
        val startUnresolved = startingNote.getUnresolvedIntrinsicNote()
        val unresolveds = mutableListOf(startUnresolved)
        var next = startUnresolved.getNextNoNeedResolveIntrinsicNote()
        while (!unresolveds.contains(next)) {
            unresolveds.add(next)
            next = next.getNextNoNeedResolveIntrinsicNote()
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

enum class IntrinsicNote(private val needResolve: Boolean) {
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
    fun getByOffset(offset: Int): IntrinsicNote {
        val result = (ordinal + offset) % 12
        return if (result >= 0) {
            ofIdx(result)
        } else {
            ofIdx(result + 12)
        }
    }

    fun getOffset(another: IntrinsicNote): Int {
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

    fun getNextNoNeedResolveIntrinsicNote(): IntrinsicNote {
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

    fun getPrefix(): String {
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

fun Accidental?.getPrefix(): String {
    if (this == null) {
        return ""
    }
    return this.getPrefix()
}