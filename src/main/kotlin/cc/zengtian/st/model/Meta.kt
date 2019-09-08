package cc.zengtian.st.model

import kotlin.math.absoluteValue

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
//    Key.values().forEach {
//        println(it.getMajorScaleNotes())
//    }
//    println(IONIAN.getRelativeStepsToRoot())
//    println(HARMONIC_MINOR.getRelativeStepsToRoot())
//    println(HARMONIC_MINOR.getRelativeStepsToMajor())
//    println(Key.C.getNotes(HARMONIC_MINOR))

    Key.values().forEach { key ->
        BUILT_IN_SCALE_STEPS.forEach { scale ->
            println("$key $scale ${key.getNotes(scale)}")
        }
    }
}

class Note(
    val nativeNote: NativeNote,
    val accidental: Accidental?
) {
    fun getUnresolvedNativeNote(): NativeNote {
        return nativeNote.getByOffset(-accidental.getOffset())
    }

    override fun toString(): String {
        return accidental.getPrefix() + getUnresolvedNativeNote()
    }
}

enum class Key(private val startingNote: Note) {
    C(Note(NativeNote.C, null)),
    F(Note(NativeNote.F, null)),
    B_FLAT(Note(NativeNote.AB, Accidental.FLAT)),
    E_FLAT(Note(NativeNote.DE, Accidental.FLAT)),
    A_FLAT(Note(NativeNote.GA, Accidental.FLAT)),
    D_FLAT(Note(NativeNote.CD, Accidental.FLAT)),
    C_SHARP(Note(NativeNote.CD, Accidental.SHARP)),
    G_FLAT(Note(NativeNote.FG, Accidental.FLAT)),
    F_SHARP(Note(NativeNote.FG, Accidental.SHARP)),
    B(Note(NativeNote.B, null)),
    C_FLAT(Note(NativeNote.B, Accidental.FLAT)),
    E(Note(NativeNote.E, null)),
    A(Note(NativeNote.A, null)),
    D(Note(NativeNote.D, null)),
    G(Note(NativeNote.G, null));

    fun getNotes(scaleSteps: ScaleSteps): List<Note> {
        val majorScale = getMajorScaleNotes()
        val relativeToMajor = scaleSteps.getRelativeStepsToMajor()
        val result = mutableListOf<Note>()
        for (pair in relativeToMajor) {
            val noteInMajor = majorScale[pair.first]
            val accidentalToBeAdded = pair.second
            val noteInResult = Note(
                noteInMajor.nativeNote.getByOffset(accidentalToBeAdded.getOffset()),
                getAccidentalByOffset(noteInMajor.accidental.getOffset() + accidentalToBeAdded.getOffset())
            )
            result.add(noteInResult)
        }
        return result
    }

    fun getMajorScaleNotes(): List<Note> {
        val startUnresolved = startingNote.getUnresolvedNativeNote()
        val unresolveds = mutableListOf(startUnresolved)
        var next = startUnresolved.getNextNoNeedResolveNativeNote()
        while (!unresolveds.contains(next)) {
            unresolveds.add(next)
            next = next.getNextNoNeedResolveNativeNote()
        }
        val nativeNotes = startingNote.nativeNote.getNativeNotesForScale(IONIAN)
        val result = mutableListOf<Note>()
        for (idx in unresolveds.indices) {
            val unresolve = unresolveds[idx]
            val native = nativeNotes[idx]
            val accidental = getAccidentalByOffset(-native.getOffset(unresolve))
            result.add(Note(native, accidental))
        }
        return result
    }
}

enum class NativeNote(private val needResolve: Boolean) {
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
    fun getByOffset(offset: Int): NativeNote {
        val result = (ordinal + offset) % 12
        return if (result >= 0) {
            ofIdx(result)
        } else {
            ofIdx(result + 12)
        }
    }

    fun getOffset(another: NativeNote): Int {
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

    fun ofIdx(idx: Int): NativeNote {
        return values()[idx]
    }

    fun getNativeNotesForScale(scaleSteps: ScaleSteps): List<NativeNote> {
        val list = mutableListOf<NativeNote>()
        list.add(this)
        for (step in scaleSteps.steps) {
            list.add(list[list.size - 1].getByOffset(step))
        }
        return list
    }

    fun getNextNoNeedResolveNativeNote(): NativeNote {
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

class ScaleSteps(val name: String, val steps: List<Int>) {

    fun getRelativeStepsToRoot(): List<Int> {
        val inc = mutableListOf<Int>()
        inc.add(0)
        for (step in steps) {
            inc.add(inc.get(inc.size - 1) + step)
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
                result[idx] = Pair(majorToRoot.indexOf(biggerInMajor), Accidental.FLAT)
            } else if (!thisToRoot.contains(smallerInMajor) && !thisToRoot.contains(biggerInMajor)) {
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

    override fun toString(): String {
        return name
    }
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

enum class Accidental(val offset: Int) {
    SHARP(1),
    FLAT(-1),
    DOUBLE_SHARP(2),
    DOUBLE_FLAT(-2);

    fun getPrefix(): String {
        return super.toString() + "_"
    }

    fun getType(): AccidentalType {
        return if (offset > 0) {
            AccidentalType.SHARP
        } else {
            AccidentalType.FLAT
        }
    }
}

fun Accidental?.getOffset(): Int {
    return this?.offset ?: 0
}

fun getAccidentalByOffset(offset: Int): Accidental? {
    check(offset.absoluteValue <= 2) { "wrong offset $offset" }
    return Accidental.values().find { it.offset == offset }
}

fun Accidental?.getPrefix(): String {
    if (this == null) {
        return ""
    }
    return this.getPrefix()
}