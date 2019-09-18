package model.theory

import cc.zengtian.mtt.model.theory.ActualChord
import cc.zengtian.mtt.model.theory.ActualNote
import cc.zengtian.mtt.model.theory.Chord
import cc.zengtian.mtt.model.theory.ChordType
import cc.zengtian.mtt.model.theory.Note.Companion.A_FLAT
import cc.zengtian.mtt.model.theory.Note.Companion.B
import cc.zengtian.mtt.model.theory.Note.Companion.C
import cc.zengtian.mtt.model.theory.Note.Companion.D_FLAT
import cc.zengtian.mtt.model.theory.Note.Companion.D_SHARP
import cc.zengtian.mtt.model.theory.Note.Companion.E
import cc.zengtian.mtt.model.theory.Note.Companion.F
import cc.zengtian.mtt.model.theory.Note.Companion.F_SHARP
import cc.zengtian.mtt.model.theory.Note.Companion.G
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Created by ZengTian on 9/18/2019.
 */
class ChordTest {
    @Test
    fun `test dimished 7th annotations`() {
        val chord = ActualChord(ActualNote.C_, ActualNote.DE, ActualNote.FG, ActualNote.A_)
        assertEquals(4, chord.annotations.size)
        chord.annotations.forEachIndexed { idx, annotation ->
            assertEquals(annotation.chordType, ChordType.DIMISHED_7TH)
            assertEquals(idx, annotation.inversion)
        }
    }

    @Test
    fun `test major triad with inversions`() {
        val c0 = Chord(C, G, E)
        val c1 = Chord(E, G, C)
        val c2 = Chord(G, E, C)
        val b0 = Chord(B, D_SHARP, F_SHARP)
        val b1 = Chord(D_SHARP, B, F_SHARP)
        val b2 = Chord(F_SHARP, D_SHARP, B)
        val dF0 = Chord(D_FLAT, F, A_FLAT)
        val dF1 = Chord(F, A_FLAT, D_FLAT)
        val dF2 = Chord(A_FLAT, D_FLAT, F)
        val root = listOf(c0, b0, dF0)
        val first = listOf(c1, b1, dF1)
        val second = listOf(c2, b2, dF2)
        val c = listOf(c0, c1, c2)
        val b = listOf(b0, b1, b2)
        val dF = listOf(dF0, dF1, dF2)
        val all = mutableListOf<Chord>().apply {
            addAll(root)
            addAll(first)
            addAll(second)
        }
        all.forEach {
            assertEquals(1, it.annotations.size)
            assertEquals(ChordType.MAJOR_TRIAD, it.annotations[0].chordType)
        }
        root.forEach { assertEquals(0, it.annotations[0].inversion) }
        first.forEach { assertEquals(1, it.annotations[0].inversion) }
        second.forEach { assertEquals(2, it.annotations[0].inversion) }
        c.forEach { assertEquals(C, it.beforeInversionRootNote(it.annotations[0])) }
        b.forEach { assertEquals(B, it.beforeInversionRootNote(it.annotations[0])) }
        dF.forEach { assertEquals(D_FLAT, it.beforeInversionRootNote(it.annotations[0])) }
    }
}
