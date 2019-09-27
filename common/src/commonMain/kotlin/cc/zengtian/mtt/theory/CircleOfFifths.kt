package cc.zengtian.mtt.theory

import cc.zengtian.mtt.theory.Scale.Companion.MAJOR
import cc.zengtian.mtt.util.getByOffset

/**
 * Created by ZengTian on 9/26/2019.
 */
enum class CircleOfFifthsNode(val actual: ActualNote) {
    C(ActualNote.C),
    G(ActualNote.G),
    D(ActualNote.D),
    A(ActualNote.A),
    E(ActualNote.E),
    B(ActualNote.B),
    FG(ActualNote.FG),
    CD(ActualNote.CD),
    GA(ActualNote.GA),
    DE(ActualNote.DE),
    AB(ActualNote.AB),
    F(ActualNote.F);

    val up5th by lazy { values().getByOffset(ordinal, 1) }

    fun up5thByStep(step: Int): CircleOfFifthsNode {
        return values().getByOffset(ordinal, step)
    }

    val down4th by lazy { values().getByOffset(ordinal, -1) }

    fun down4thByStep(step: Int): CircleOfFifthsNode {
        return values().getByOffset(ordinal, -step)
    }

    fun down4thStepTo(node: CircleOfFifthsNode): Int {
        var step = 0
        while (down4thByStep(step) != node) {
            step++
        }
        return step
    }

    fun up5thStepTo(node: CircleOfFifthsNode): Int {
        var step = 0
        while (up5thByStep(step) != node) {
            step ++
        }
        return step
    }

    /**
     * < 0: down 4th
     * > 0: up 5th
     */
    fun nearestOffsetTo(node : CircleOfFifthsNode): Int {
        val down = down4thStepTo(node)
        val up = up5thStepTo(node)
        return if (up > down) {
            up
        } else {
            -down
        }
    }

    val opposite by lazy { values().getByOffset(ordinal, 6) }
    // eg: d(2 sharps)'s extra sharp note compared with g(1 sharp) is c sharp
    // sharp and flat is different
    val Key.extraAccidentalNote: Note?
        get() {
            val acc = startingNote.accidental
            return when(acc.type) {
                AccidentalType.FLAT -> {
                    val oneStep4thDown = down4th
                    Note.ofActual(oneStep4thDown.actual, Accidental.FLAT)
                }
                AccidentalType.SHARP -> {
                    val twoStep4thDown = down4thByStep(2)
                    Note.of(twoStep4thDown.actual, Accidental.SHARP)
                }
                null -> null
            }
        }
    val majorKey = if (actual == ActualNote.FG) {
        // special case for f sharp, because f-sharp and g-flat both have 6 accidentals
        Key.F_SHARP
    } else {
        actual.keys.minBy { it.getAccidentalCountOfScale(MAJOR) }!!
    }
    val majorKeys = actual.keys.toList()
    val minorKey = majorKey.relativeMinor
    val minorKeys = actual.keys.map { it.relativeMinor }
}
