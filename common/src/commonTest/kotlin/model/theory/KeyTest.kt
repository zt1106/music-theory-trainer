package model.theory

import assertEquals
import cc.zengtian.mtt.theory.Chord
import cc.zengtian.mtt.theory.DiatonicChordType.SEVEN_TH
import cc.zengtian.mtt.theory.DiatonicChordType.TRIAD
import cc.zengtian.mtt.theory.DiatonicScaleType
import cc.zengtian.mtt.theory.Key
import cc.zengtian.mtt.theory.Note.*
import kotlin.test.Test

/**
 * Created by ZengTian on 9/20/2019.
 */
class KeyTest {
    @Test
    fun `test get diatonic chord`() {
        Key.C.getDiatonicChord(1, DiatonicScaleType.MAJOR, TRIAD ).assertEquals(Chord(C, E, G))
        Key.C.getDiatonicChord(7, DiatonicScaleType.MAJOR, TRIAD).assertEquals(Chord(B, D, F))
        Key.C.getDiatonicChord(1, DiatonicScaleType.MINOR, TRIAD ).assertEquals(Chord(C, E_FLAT, G))
        Key.A.getDiatonicChord(1, DiatonicScaleType.MINOR, TRIAD ).assertEquals(Chord(A, C, E))
        Key.C.getDiatonicChord(1, DiatonicScaleType.MAJOR, SEVEN_TH ).assertEquals(Chord(C, E, G, B))
        Key.C.getDiatonicChord(1, DiatonicScaleType.MINOR, SEVEN_TH ).assertEquals(Chord(C, E_FLAT, G, B_FLAT))
    }
}
