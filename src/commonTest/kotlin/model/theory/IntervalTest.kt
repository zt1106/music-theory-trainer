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
        val g = Note.ofWellTempered(WellTemperedNote.G_, null)
        val p5 = Interval.of(5, IntervalQuality.PERFECT)
        val c = Note.ofWellTempered(WellTemperedNote.C_, null)
        assertEquals(c, p5.getbelowFromAbove(g))
        val cFlat = Note.ofWellTempered(WellTemperedNote.B_, Accidental.FLAT)
        val aug2 = Interval.of(2, IntervalQuality.AUGMENTED)
        assertNull(aug2.getbelowFromAbove(cFlat))
        val eFlat = Note.ofWellTempered(WellTemperedNote.DE, Accidental.FLAT)
        val dim7 = Interval.of(7, IntervalQuality.DIMISHED)
        val fSharp = Note.ofWellTempered(WellTemperedNote.FG, Accidental.SHARP)
        assertEquals(fSharp, dim7.getbelowFromAbove(eFlat))
        val aFlat = Note.ofWellTempered(WellTemperedNote.GA, Accidental.FLAT)
        val aug7 = Interval.of(7, IntervalQuality.AUGMENTED)
        assertNull(aug7.getbelowFromAbove(aFlat))
        val bFlat = Note.of(WellTemperedNote.B_, Accidental.FLAT)
        val aug5 = Interval.of(5, IntervalQuality.AUGMENTED)
        val eDoubleflat = Note.of(WellTemperedNote.E_, Accidental.DOUBLE_FLAT)
        assertEquals(eDoubleflat, aug5.getbelowFromAbove(bFlat))
    }

    @Test
    fun `test interval get above from below`() {
        val cFlat = Note.of(WellTemperedNote.C_, Accidental.FLAT)
        val min3 = Interval.of(3, IntervalQuality.MINOR)
        val eFlat = Note.of(WellTemperedNote.E_, Accidental.FLAT)
        assertEquals(eFlat, min3.getAboveFromBelow(cFlat))
        val e = Note.of(WellTemperedNote.E_, null)
        val aug1 = Interval.of(1, IntervalQuality.AUGMENTED)
        val eSharp = Note.of(WellTemperedNote.E_, Accidental.SHARP)
        assertEquals(eSharp, aug1.getAboveFromBelow(e))
    }

    @Test
    fun `test get interval between notes`() {
        val cg = Interval.of(Note.ofWellTempered(WellTemperedNote.C_, null), Note.ofWellTempered(WellTemperedNote.G_, null))
        assertEquals(Interval.of(5, IntervalQuality.PERFECT), cg)
        val cf = Interval.of(Note.ofWellTempered(WellTemperedNote.C_, null), Note.ofWellTempered(WellTemperedNote.F_, null))
        assertEquals(Interval.of(4, IntervalQuality.PERFECT), cf)
        val ge = Interval.of(Note.ofWellTempered(WellTemperedNote.G_, null), Note.ofWellTempered(WellTemperedNote.E_, null))
        assertEquals(Interval.of(6, IntervalQuality.MAJOR), ge)
        val geFlat = Interval.of(Note.ofWellTempered(WellTemperedNote.G_, null), Note.ofWellTempered(WellTemperedNote.DE, Accidental.FLAT))
        assertEquals(Interval.of(6, IntervalQuality.MINOR), geFlat)
        val geDoubleflat = Interval.of(Note.ofWellTempered(WellTemperedNote.G_, null), Note.ofWellTempered(WellTemperedNote.D_, Accidental.DOUBLE_FLAT))
        assertEquals(Interval.of(6, IntervalQuality.DIMISHED), geDoubleflat)
        val geSharp = Interval.of(Note.ofWellTempered(WellTemperedNote.G_, null), Note.ofWellTempered(WellTemperedNote.F_, Accidental.SHARP))
        assertEquals(Interval.of(6, IntervalQuality.AUGMENTED), geSharp)
        val geDoublesharp = Interval.of(Note.ofWellTempered(WellTemperedNote.G_, null), Note.ofWellTempered(WellTemperedNote.FG, Accidental.DOUBLE_SHARP))
        assertNull(geDoublesharp)
        val gSharpE = Interval.of(Note.ofWellTempered(WellTemperedNote.GA, Accidental.SHARP), Note.ofWellTempered(WellTemperedNote.E_, null))
        assertEquals(Interval.of(6, IntervalQuality.MINOR), gSharpE)
        val eFlatA = Interval.of(Note.ofWellTempered(WellTemperedNote.DE, Accidental.FLAT), Note.ofWellTempered(WellTemperedNote.A_, null))
        assertEquals(Interval.of(4, IntervalQuality.AUGMENTED), eFlatA)
    }
}