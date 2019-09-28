package model.theory

import assertEquals
import assertTrue
import cc.zengtian.mtt.theory.*
import cc.zengtian.mtt.theory.ChordSonority.*
import cc.zengtian.mtt.theory.Note.*
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Created by ZengTian on 9/18/2019.
 */
class ChordTest {
    @Test
    fun `test diatonic chord`() {
        Key.C.getDiatonicChord(1).assertTrue { chord ->
            chord.root == C && chord.annotations[0].chordSonority == MAJOR_TRIAD
        }
        Key.C.getDiatonicChord(4).assertTrue { chord ->
            chord.root == F && chord.annotations[0].chordSonority == MAJOR_TRIAD
        }
        Key.C.getDiatonicChord(7).assertTrue { chord ->
            chord.root == B && chord.annotations[0].chordSonority == DIMISHED_TRIAD
        }
        Key.C.getDiatonicChord(6).assertTrue { chord ->
            chord.root == A && chord.annotations[0].chordSonority == MINOR_TRIAD
        }
        // g harmonic minor
        Key.G.getDiatonicChord(1, DiatonicScaleType.MINOR).assertTrue { chord ->
            chord.root == G && chord.annotation!!.chordSonority == MINOR_TRIAD
        }
        Key.G.getDiatonicChord(2, DiatonicScaleType.MINOR).assertTrue { chord ->
            chord.root == A && chord.annotation!!.chordSonority == DIMISHED_TRIAD
        }
        Key.G.getDiatonicChord(3, DiatonicScaleType.MINOR).assertTrue { chord ->
            chord.root == B_FLAT && chord.annotation!!.chordSonority == MAJOR_TRIAD
        }
    }

    @Test
    fun `test dimished 7th annotations`() {
        val chord = ActualChord(ActualNote.C, ActualNote.DE, ActualNote.FG, ActualNote.A)
        assertEquals(4, chord.annotations.size)
        chord.annotations.forEachIndexed { idx, annotation ->
            assertEquals(annotation.chordSonority, DIMISHED_7TH)
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
            assertEquals(MAJOR_TRIAD, it.annotations[0].chordSonority)
        }
        root.forEach { assertEquals(0, it.annotations[0].inversion) }
        first.forEach { assertEquals(1, it.annotations[0].inversion) }
        second.forEach { assertEquals(2, it.annotations[0].inversion) }
        c.forEach { assertEquals(C, it.beforeInversionRootNote(it.annotations[0])) }
        b.forEach { assertEquals(B, it.beforeInversionRootNote(it.annotations[0])) }
        dF.forEach { assertEquals(D_FLAT, it.beforeInversionRootNote(it.annotations[0])) }
    }

    @Test
    fun `test chord sonority`() {
        Chord(C, E, G).annotations[0].chordSonority.assertEquals(MAJOR_TRIAD)
        Chord(C, E_FLAT, G).annotations[0].chordSonority.assertEquals(MINOR_TRIAD)
        Chord(C, E_FLAT, F_SHARP).annotations[0].chordSonority.assertEquals(DIMISHED_TRIAD)
        Chord(C, E, G_SHARP).annotations[0].chordSonority.assertEquals(AUGMENTED_TRIAD)
        Chord(C, E, G, B).annotations[0].chordSonority.assertEquals(MAJOR_MAJOR_7TH)
        Chord(C, E, G, B_FLAT).annotations[0].chordSonority.assertEquals(DOMINANT_7TH)
        Chord(C, E_FLAT, G, B).annotations[0].chordSonority.assertEquals(MINOR_MAJOR_7TH)
        Chord(C, E_FLAT, F_SHARP, B_FLAT).annotations[0].chordSonority.assertEquals(HALF_DIMISHED_7TH)
        Chord(C, E_FLAT, F_SHARP, B_DOUBLE_FLAT).annotations[0].chordSonority.assertEquals(DIMISHED_7TH)
    }
}
