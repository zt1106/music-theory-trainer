import cc.zengtian.mtt.model.theory.Accidental.*
import cc.zengtian.mtt.model.theory.Interval
import cc.zengtian.mtt.model.theory.IntervalQuality.*
import cc.zengtian.mtt.model.theory.Key
import cc.zengtian.mtt.model.theory.Note
import cc.zengtian.mtt.model.theory.Scale
import cc.zengtian.mtt.model.theory.WellTemperedNote.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

/**
 * Created by ZengTian on 2019/9/9.
 */
class MusicTheoryTest {
    @Test
    fun `print all scales`() {
        Key.values().forEach { key ->
            Scale.builtInValues().forEach { scale ->
                println("$key $scale ${key.getNotesOfScale(scale)}")
            }
        }
    }

    @Test
    fun `test major scales using all letters`() {
        val letters = listOf(A_, B_, C_, D_, E_, F_, G_)
        Key.values().map { it.getNotesOfScale(Scale.MAJOR).map { it.getBeforeAccidentalWellTemperedNote() } }
            .forEach { beforeAcc ->
                assertTrue(beforeAcc.alsoPrintln().containsAll(letters))
            }
    }

    @Test
    fun `test scales except chromatic using unique letters`() {
        val scales = Scale.builtInValues().toMutableList().apply { remove(Scale.CHROMATIC) }
        Key.values().forEach { key ->
            scales.forEach { scale ->
                val notes = key.getNotesOfScale(scale)
                val beforeAcc = notes.map { it.getBeforeAccidentalWellTemperedNote() }.alsoPrintln()
                assertEquals(notes.size, beforeAcc.distinct().size)
            }
        }
    }

    @Test
    fun `test get interval between notes`() {
        val cg = Interval.of(Note.ofWellTempered(C_, null), Note.ofWellTempered(G_, null))
        assertEquals(Interval.of(5, PERFECT), cg)
        val cf = Interval.of(Note.ofWellTempered(C_, null), Note.ofWellTempered(F_, null))
        assertEquals(Interval.of(4, PERFECT), cf)
        val ge = Interval.of(Note.ofWellTempered(G_, null), Note.ofWellTempered(E_, null))
        assertEquals(Interval.of(6, MAJOR), ge)
        val geFlat = Interval.of(Note.ofWellTempered(G_, null), Note.ofWellTempered(DE, FLAT))
        assertEquals(Interval.of(6, MINOR), geFlat)
        val geDoubleflat = Interval.of(Note.ofWellTempered(G_, null), Note.ofWellTempered(D_, DOUBLE_FLAT))
        assertEquals(Interval.of(6, DIMISHED), geDoubleflat)
        val geSharp = Interval.of(Note.ofWellTempered(G_, null), Note.ofWellTempered(F_, SHARP))
        assertEquals(Interval.of(6, AUGMENTED), geSharp)
        val geDoublesharp = Interval.of(Note.ofWellTempered(G_, null), Note.ofWellTempered(FG, DOUBLE_SHARP))
        assertNull(geDoublesharp)
        val gSharpE = Interval.of(Note.ofWellTempered(GA, SHARP), Note.ofWellTempered(E_, null))
        assertEquals(Interval.of(6, MINOR), gSharpE)
        val eFlatA = Interval.of(Note.ofWellTempered(DE, FLAT), Note.ofWellTempered(A_, null))
        assertEquals(Interval.of(4, AUGMENTED), eFlatA)
    }

    @Test
    fun `test interval inversion`() {
        val aug4 = Interval.of(4, AUGMENTED)
        val dim5 = Interval.of(5, DIMISHED)
        assertEquals(dim5, aug4.getInversion())
        val p5 = Interval.of(5, PERFECT)
        val p4 = Interval.of(4, PERFECT)
        assertEquals(p4, p5.getInversion())
        val maj6 = Interval.of(6, MAJOR)
        val min3 = Interval.of(3, MINOR)
        assertEquals(min3, maj6.getInversion())
    }

    @Test
    fun `test interval get below from above`() {
        val g = Note.ofWellTempered(G_, null)
        val p5 = Interval.of(5, PERFECT)
        val c = Note.ofWellTempered(C_, null)
        assertEquals(c, p5.getbelowFromAbove(g))
        val cFlat = Note.ofWellTempered(B_, FLAT)
        val aug2 = Interval.of(2, AUGMENTED)
        assertNull(aug2.getbelowFromAbove(cFlat))
        val eFlat = Note.ofWellTempered(DE, FLAT)
        val dim7 = Interval.of(7, DIMISHED)
        val fSharp = Note.ofWellTempered(FG, SHARP)
        assertEquals(fSharp, dim7.getbelowFromAbove(eFlat))
        val aFlat = Note.ofWellTempered(GA, FLAT)
        val aug7 = Interval.of(7, AUGMENTED)
        assertNull(aug7.getbelowFromAbove(aFlat))
        val bFlat = Note.of(B_, FLAT)
        val aug5 = Interval.of(5, AUGMENTED)
        val eDoubleflat = Note.of(E_, DOUBLE_FLAT)
        assertEquals(eDoubleflat, aug5.getbelowFromAbove(bFlat))
    }

    @Test
    fun `test interval get above from below`() {
        val cFlat = Note.of(C_, FLAT)
        val min3 = Interval.of(3, MINOR)
        val eFlat = Note.of(E_, FLAT)
        assertEquals(eFlat, min3.getAboveFromBelow(cFlat))
        val e = Note.of(E_, null)
        val aug1 = Interval.of(1, AUGMENTED)
        val eSharp = Note.of(E_, SHARP)
        assertEquals(eSharp, aug1.getAboveFromBelow(e))
    }
}
