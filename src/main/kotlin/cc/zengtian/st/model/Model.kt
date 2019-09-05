package cc.zengtian.st.model

import kotlin.math.absoluteValue

/**
 * Created by ZengTian on 2019/9/5.
 */
fun main() {
    println(IntrinsicNote.G.getInrinsicNotes(KeySteps.MAJOR_SCALE_STEPS))
    println(Note(IntrinsicNote.FG, Accidental.FLAT))
}

class Note(private val intrinsicNote: IntrinsicNote,
           private val accidental: Accidental?) {
    override fun toString(): String {
        val offset = when(accidental) {
            Accidental.DOUBLE_FLAT -> 2
            Accidental.SHARP -> -1
            Accidental.FLAT -> 1
            Accidental.DOUBLE_SHARP -> -2
            null -> 0
        }
        val fromNote = intrinsicNote.getByOffset(offset)
        return accidental.toString() + fromNote
    }
}

enum class Key(private val intrinsicNote: IntrinsicNote,
               val accidentalType: AccidentalType?) {
    C(IntrinsicNote.C, null),
    F(IntrinsicNote.F, AccidentalType.FLAT),
    B_FLAT(IntrinsicNote.AB, AccidentalType.FLAT),
    E_FLAT(IntrinsicNote.DE, AccidentalType.FLAT),
    A_FLAT(IntrinsicNote.GA, AccidentalType.FLAT),
    D_FLAT(IntrinsicNote.CD, AccidentalType.FLAT),
    C_SHARP(IntrinsicNote.CD, AccidentalType.SHARP),
    G_FLAT(IntrinsicNote.FG, AccidentalType.FLAT),
    F_SHARP(IntrinsicNote.FG, AccidentalType.SHARP),
    B(IntrinsicNote.B, AccidentalType.SHARP),
    C_FLAT(IntrinsicNote.B, AccidentalType.FLAT),
    E(IntrinsicNote.E, AccidentalType.SHARP),
    A(IntrinsicNote.A, AccidentalType.SHARP),
    D(IntrinsicNote.D, AccidentalType.SHARP),
    G(IntrinsicNote.G, AccidentalType.SHARP);

    fun getNotes(keySteps: KeySteps) : List<Note> {
        val intrinsicNotes = intrinsicNote.getInrinsicNotes(keySteps)

        TODO()
    }
}

enum class IntrinsicNote {
    C,
    CD,
    D,
    DE,
    E,
    F,
    FG,
    G,
    GA,
    A,
    AB,
    B;

    /**
     * right: > 0
     * left: < 0
     */
    fun getByOffset(offset: Int) : IntrinsicNote {
        val result = (ordinal + offset) % 12
        return ofIdx(result.absoluteValue)
    }

    fun ofIdx(idx: Int) : IntrinsicNote {
        this.ordinal
        return values().find { it.ordinal == idx }!!
    }

    fun getInrinsicNotes(keySteps: KeySteps) : List<IntrinsicNote> {
        val list = mutableListOf<IntrinsicNote>()
        list.add(this)
        for (step in keySteps.steps) {
            list.add(list[list.size - 1].getByOffset(step))
        }
        return list
    }
}

enum class KeySteps(val steps: List<Int>) {
    MAJOR_SCALE_STEPS(listOf(2, 2, 1, 2, 2, 2)),
    NATURAL_MINOR_SCALE_STEPS(listOf(2, 1, 2, 2, 1, 2))
}

class Interval(val num: Int, val quality: IntervalQuality) {

}

enum class IntervalQuality {
    AUGMENTED,
    MAJOR,
    MINOR,
    DINISHED
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
    }}

fun Accidental?.toString() : String {
    if (this == null) {
        return ""
    }
    return this.toString()
}