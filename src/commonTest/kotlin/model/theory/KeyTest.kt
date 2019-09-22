package model.theory

import assertEquals
import cc.zengtian.mtt.model.theory.Chord
import cc.zengtian.mtt.model.theory.DiatonicChordType.SEVEN_TH
import cc.zengtian.mtt.model.theory.DiatonicChordType.TRIAD
import cc.zengtian.mtt.model.theory.Key
import cc.zengtian.mtt.model.theory.Note.Companion.A
import cc.zengtian.mtt.model.theory.Note.Companion.B
import cc.zengtian.mtt.model.theory.Note.Companion.B_FLAT
import cc.zengtian.mtt.model.theory.Note.Companion.C
import cc.zengtian.mtt.model.theory.Note.Companion.D
import cc.zengtian.mtt.model.theory.Note.Companion.E
import cc.zengtian.mtt.model.theory.Note.Companion.E_FLAT
import cc.zengtian.mtt.model.theory.Note.Companion.F
import cc.zengtian.mtt.model.theory.Note.Companion.G
import cc.zengtian.mtt.model.theory.Scale.Companion.MAJOR
import cc.zengtian.mtt.model.theory.Scale.Companion.MINOR
import kotlin.test.Test

/**
 * Created by ZengTian on 9/20/2019.
 */
class KeyTest {
    @Test
    fun `test get diatonic chord`() {
        Key.C.getDiatonicChord(MAJOR, TRIAD, 1).assertEquals(Chord(C, E, G))
        Key.C.getDiatonicChord(MAJOR, TRIAD, 7).assertEquals(Chord(B, D, F))
        Key.C.getDiatonicChord(MINOR, TRIAD, 1).assertEquals(Chord(C, E_FLAT, G))
        Key.A.getDiatonicChord(MINOR, TRIAD, 1).assertEquals(Chord(A, C, E))
        Key.C.getDiatonicChord(MAJOR, SEVEN_TH, 1).assertEquals(Chord(C, E, G, B))
        Key.C.getDiatonicChord(MINOR, SEVEN_TH, 1).assertEquals(Chord(C, E_FLAT, G, B_FLAT))
    }
}