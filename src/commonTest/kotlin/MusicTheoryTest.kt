
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
                assertTrue(beforeAcc.containsAll(letters))
            }
    }

    @Test
    fun `test scales except chromatic using unique letters`() {
        val scales = Scale.builtInValues().toMutableList().apply { remove(Scale.CHROMATIC) }
        Key.values().forEach { key ->
            scales.forEach { scale ->
                val notes = key.getNotesOfScale(scale)
                val beforeAcc = notes.map { it.getBeforeAccidentalWellTemperedNote() }
                assertEquals(notes.size, beforeAcc.distinct().size)
            }
        }
    }

    @Test
    fun `test get interval between notes`() {
        val cg = Interval.of(Note.of(C_, null), Note.of(G_, null))
        assertEquals(Interval.of(5, PERFECT), cg)
        val cf = Interval.of(Note.of(C_, null), Note.of(F_, null))
        assertEquals(Interval.of(4, PERFECT), cf)
        val ge = Interval.of(Note.of(G_, null), Note.of(E_, null))
        assertEquals(Interval.of(6, MAJOR), ge)
        val geFlat = Interval.of(Note.of(G_, null), Note.of(DE, FLAT))
        assertEquals(Interval.of(6, MINOR), geFlat)
        val geDoubleflat = Interval.of(Note.of(G_, null), Note.of(D_, DOUBLE_FLAT))
        assertEquals(Interval.of(6, DIMISHED), geDoubleflat)
        val geSharp = Interval.of(Note.of(G_, null), Note.of(F_, SHARP))
        assertEquals(Interval.of(6, AUGMENTED), geSharp)
        val geDoublesharp = Interval.of(Note.of(G_, null), Note.of(FG, DOUBLE_SHARP))
        assertNull(geDoublesharp)
        val gSharpE = Interval.of(Note.of(GA, SHARP), Note.of(E_, null))
        assertEquals(Interval.of(6, MINOR), gSharpE)
        val eFlatA = Interval.of(Note.of(DE, FLAT), Note.of(A_, null))
        assertEquals(Interval.of(4, AUGMENTED), eFlatA)
    }

    @Test
    fun `test interval inversion`() {
        val aug4 = Interval.of(4, AUGMENTED)
        assertEquals(Interval.of(5, DIMISHED), aug4.getInversion())
        val p5 = Interval.of(5, PERFECT)
        assertEquals(Interval.of(4, PERFECT), p5.getInversion())
        val maj6 = Interval.of(6, MAJOR)
        assertEquals(Interval.of(3, MINOR), maj6.getInversion())
    }
}
