package model.theory

import cc.zengtian.mtt.model.theory.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

/**
 * Created by ZengTian on 2019/9/11.
 */
class IntervalTest {

    @Test
    fun `test interval inversion`() {
        val aug4 = Interval.of(4, IntervalQuality.AUGMENTED)
        val dim5 = Interval.of(5, IntervalQuality.DIMISHED)
        assertEquals(dim5, aug4.inversion)
        val p5 = Interval.of(5, IntervalQuality.PERFECT)
        val p4 = Interval.of(4, IntervalQuality.PERFECT)
        assertEquals(p4, p5.inversion)
        val maj6 = Interval.of(6, IntervalQuality.MAJOR)
        val min3 = Interval.of(3, IntervalQuality.MINOR)
        assertEquals(min3, maj6.inversion)
    }

    @Test
    fun `test interval get below from above`() {
        val g = Note.ofActual(ActualNote.G_, null)
        val p5 = Interval.of(5, IntervalQuality.PERFECT)
        val c = Note.ofActual(ActualNote.C_, null)
        assertEquals(c, p5.getbelowFromAbove(g))
        val cFlat = Note.ofActual(ActualNote.B_, Accidental.FLAT)
        val aug2 = Interval.of(2, IntervalQuality.AUGMENTED)
        assertNull(aug2.getbelowFromAbove(cFlat))
        val eFlat = Note.ofActual(ActualNote.DE, Accidental.FLAT)
        val dim7 = Interval.of(7, IntervalQuality.DIMISHED)
        val fSharp = Note.ofActual(ActualNote.FG, Accidental.SHARP)
        assertEquals(fSharp, dim7.getbelowFromAbove(eFlat))
        val aFlat = Note.ofActual(ActualNote.GA, Accidental.FLAT)
        val aug7 = Interval.of(7, IntervalQuality.AUGMENTED)
        assertNull(aug7.getbelowFromAbove(aFlat))
        val bFlat = Note.of(ActualNote.B_, Accidental.FLAT)
        val aug5 = Interval.of(5, IntervalQuality.AUGMENTED)
        val eDoubleflat = Note.of(ActualNote.E_, Accidental.DOUBLE_FLAT)
        assertEquals(eDoubleflat, aug5.getbelowFromAbove(bFlat))
    }

    @Test
    fun `test interval get above from below`() {
        val cFlat = Note.of(ActualNote.C_, Accidental.FLAT)
        val min3 = Interval.of(3, IntervalQuality.MINOR)
        val eFlat = Note.of(ActualNote.E_, Accidental.FLAT)
        assertEquals(eFlat, min3.getAboveFromBelow(cFlat))
        val e = Note.of(ActualNote.E_, null)
        val aug1 = Interval.of(1, IntervalQuality.AUGMENTED)
        val eSharp = Note.of(ActualNote.E_, Accidental.SHARP)
        assertEquals(eSharp, aug1.getAboveFromBelow(e))
    }

    @Test
    fun `test get interval between notes`() {
        val cg = Interval.of(Note.ofActual(ActualNote.C_, null), Note.ofActual(ActualNote.G_, null))
        assertEquals(Interval.of(5, IntervalQuality.PERFECT), cg)
        val cf = Interval.of(Note.ofActual(ActualNote.C_, null), Note.ofActual(ActualNote.F_, null))
        assertEquals(Interval.of(4, IntervalQuality.PERFECT), cf)
        val ge = Interval.of(Note.ofActual(ActualNote.G_, null), Note.ofActual(ActualNote.E_, null))
        assertEquals(Interval.of(6, IntervalQuality.MAJOR), ge)
        val geFlat = Interval.of(Note.ofActual(ActualNote.G_, null), Note.ofActual(ActualNote.DE, Accidental.FLAT))
        assertEquals(Interval.of(6, IntervalQuality.MINOR), geFlat)
        val geDoubleflat = Interval.of(Note.ofActual(ActualNote.G_, null), Note.ofActual(ActualNote.D_, Accidental.DOUBLE_FLAT))
        assertEquals(Interval.of(6, IntervalQuality.DIMISHED), geDoubleflat)
        val geSharp = Interval.of(Note.ofActual(ActualNote.G_, null), Note.ofActual(ActualNote.F_, Accidental.SHARP))
        assertEquals(Interval.of(6, IntervalQuality.AUGMENTED), geSharp)
        val geDoublesharp = Interval.of(Note.ofActual(ActualNote.G_, null), Note.ofActual(ActualNote.FG, Accidental.DOUBLE_SHARP))
        assertNull(geDoublesharp)
        val gSharpE = Interval.of(Note.ofActual(ActualNote.GA, Accidental.SHARP), Note.ofActual(ActualNote.E_, null))
        assertEquals(Interval.of(6, IntervalQuality.MINOR), gSharpE)
        val eFlatA = Interval.of(Note.ofActual(ActualNote.DE, Accidental.FLAT), Note.ofActual(ActualNote.A_, null))
        assertEquals(Interval.of(4, IntervalQuality.AUGMENTED), eFlatA)
    }
}