package model.theory

import cc.zengtian.mtt.model.theory.Accidental.*
import cc.zengtian.mtt.model.theory.ActualNote.*
import cc.zengtian.mtt.model.theory.Interval
import cc.zengtian.mtt.model.theory.IntervalQuality.*
import cc.zengtian.mtt.model.theory.Note
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

/**
 * Created by ZengTian on 2019/9/11.
 */
class IntervalTest {

    @Test
    fun `test interval inversion`() {
        val aug4 = Interval.of(4, AUGMENTED)
        val dim5 = Interval.of(5, DIMISHED)
        assertEquals(dim5, aug4.inversion)
        val p5 = Interval.of(5, PERFECT)
        val p4 = Interval.of(4, PERFECT)
        assertEquals(p4, p5.inversion)
        val maj6 = Interval.of(6, MAJOR)
        val min3 = Interval.of(3, MINOR)
        assertEquals(min3, maj6.inversion)
    }

    @Test
    fun `test interval get below from above`() {
        val p5 = Interval.of(5, PERFECT)
        assertEquals(Note.C, p5.getbelowFromAbove(Note.G))
        val cFlat = Note.ofActual(B, FLAT)
        val aug2 = Interval.of(2, AUGMENTED)
        assertNull(aug2.getbelowFromAbove(cFlat))
        val eFlat = Note.ofActual(DE, FLAT)
        val dim7 = Interval.of(7, DIMISHED)
        val fSharp = Note.ofActual(FG, SHARP)
        assertEquals(fSharp, dim7.getbelowFromAbove(eFlat))
        val aFlat = Note.ofActual(GA, FLAT)
        val aug7 = Interval.of(7, AUGMENTED)
        assertNull(aug7.getbelowFromAbove(aFlat))
        val bFlat = Note.of(B, FLAT)
        val aug5 = Interval.of(5, AUGMENTED)
        val eDoubleflat = Note.of(E, DOUBLE_FLAT)
        assertEquals(eDoubleflat, aug5.getbelowFromAbove(bFlat))
    }

    @Test
    fun `test interval get above from below`() {
        val cFlat = Note.of(C, FLAT)
        val min3 = Interval.of(3, MINOR)
        val eFlat = Note.of(E, FLAT)
        assertEquals(eFlat, min3.getAboveFromBelow(cFlat))
        val e = Note.of(E, null)
        val aug1 = Interval.of(1, AUGMENTED)
        val eSharp = Note.of(E, SHARP)
        assertEquals(eSharp, aug1.getAboveFromBelow(e))
    }

    @Test
    fun `test get interval between notes`() {
        val cg = Interval.of(Note.ofActual(C, null), Note.ofActual(G, null))
        assertEquals(Interval.of(5, PERFECT), cg)
        val cf = Interval.of(Note.ofActual(C, null), Note.ofActual(F, null))
        assertEquals(Interval.of(4, PERFECT), cf)
        val ge = Interval.of(Note.ofActual(G, null), Note.ofActual(E, null))
        assertEquals(Interval.of(6, MAJOR), ge)
        val geFlat = Interval.of(Note.ofActual(G, null), Note.ofActual(DE, FLAT))
        assertEquals(Interval.of(6, MINOR), geFlat)
        val geDoubleflat = Interval.of(Note.ofActual(G, null), Note.ofActual(D, DOUBLE_FLAT))
        assertEquals(Interval.of(6, DIMISHED), geDoubleflat)
        val geSharp = Interval.of(Note.ofActual(G, null), Note.ofActual(F, SHARP))
        assertEquals(Interval.of(6, AUGMENTED), geSharp)
        val geDoublesharp = Interval.of(Note.ofActual(G, null), Note.ofActual(FG, DOUBLE_SHARP))
        assertNull(geDoublesharp)
        val gSharpE = Interval.of(Note.ofActual(GA, SHARP), Note.ofActual(E, null))
        assertEquals(Interval.of(6, MINOR), gSharpE)
        val eFlatA = Interval.of(Note.ofActual(DE, FLAT), Note.ofActual(A, null))
        assertEquals(Interval.of(4, AUGMENTED), eFlatA)
    }
}