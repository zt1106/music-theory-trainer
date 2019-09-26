package cc.zengtian.mtt.theory

import cc.zengtian.mtt.util.nextOrFirst
import cc.zengtian.mtt.util.prevOrLast

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

    val up5th by lazy { values()[values().nextOrFirst(ordinal)] }

    fun up5thByStep(step: Int): CircleOfFifthsNode {
        TODO()
    }

    val down4th by lazy { values()[values().prevOrLast(ordinal)] }

    fun down4thBySetp(step: Int): CircleOfFifthsNode {
        TODO()
    }

    val opposite by lazy { }
    val Key.extraAccidentalNote: Note?
        get() = TODO()
    val key = actual.keys[0]
    val keys = actual.keys
}
