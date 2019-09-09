
import cc.zengtian.mtt.model.theory.*
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
        val letters = listOf(
            WellTemperedNote.A,
            WellTemperedNote.B,
            WellTemperedNote.C,
            WellTemperedNote.D,
            WellTemperedNote.E,
            WellTemperedNote.F,
            WellTemperedNote.G
        )
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
        val cg = Interval.of(Note.of(WellTemperedNote.C, null), Note.of(WellTemperedNote.G, null))
        assertEquals(Interval.of(5, IntervalQuality.PERFECT), cg)
        val cf = Interval.of(Note.of(WellTemperedNote.C, null), Note.of(WellTemperedNote.F, null))
        assertEquals(Interval.of(4, IntervalQuality.PERFECT), cf)
        val ge = Interval.of(Note.of(WellTemperedNote.G, null), Note.of(WellTemperedNote.E, null))
        assertEquals(Interval.of(6, IntervalQuality.MAJOR), ge)
        val geFlat = Interval.of(Note.of(WellTemperedNote.G, null), Note.of(WellTemperedNote.DE, Accidental.FLAT))
        assertEquals(Interval.of(6, IntervalQuality.MINOR), geFlat)
        val geDoubleflat = Interval.of(Note.of(WellTemperedNote.G, null), Note.of(WellTemperedNote.D, Accidental.DOUBLE_FLAT))
        assertEquals(Interval.of(6, IntervalQuality.DIMISHED), geDoubleflat)
        val geSharp = Interval.of(Note.of(WellTemperedNote.G, null), Note.of(WellTemperedNote.F, Accidental.SHARP))
        assertEquals(Interval.of(6, IntervalQuality.AUGMENTED), geSharp)
        val geDoublesharp = Interval.of(Note.of(WellTemperedNote.G, null), Note.of(WellTemperedNote.FG, Accidental.DOUBLE_SHARP))
        assertNull(geDoublesharp)
        val gSharpE = Interval.of(Note.of(WellTemperedNote.GA, Accidental.SHARP), Note.of(WellTemperedNote.E, null))
        assertEquals(Interval.of(6, IntervalQuality.MINOR), gSharpE)
    }
}
