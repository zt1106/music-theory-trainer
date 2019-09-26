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

    val up5th by lazy { values().getByOffset(ordinal, 1)}

    fun up5thByStep(step: Int): CircleOfFifthsNode {
        return values().getByOffset(ordinal, step)
    }

    val down4th by lazy { values().getByOffset(ordinal, -1) }

    fun down4thBySetp(step: Int): CircleOfFifthsNode {
        return values().getByOffset(ordinal, -step)
    }

    val opposite by lazy { values().getByOffset(ordinal, 6) }
    val Key.extraAccidentalNote: Note?
        get() = TODO()
    val majorKey = actual.keys.minBy { it.getAccidentalCountOfScale(MAJOR) }!!
    val majorKeys = actual.keys.toList()
    val minorKey = majorKey.relativeMinor
    val minorKeys = actual.keys.map { it.relativeMinor }
}
