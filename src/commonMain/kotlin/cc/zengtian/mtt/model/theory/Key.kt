package cc.zengtian.mtt.model.theory

import cc.zengtian.mtt.model.theory.Accidental.FLAT
import cc.zengtian.mtt.model.theory.Accidental.SHARP
import cc.zengtian.mtt.model.theory.WellTemperedNote.*

enum class Key(val startingNote: Note) {
    C(Note.of(C_, null)),
    F(Note.of(F_, null)),
    B_FLAT(Note.of(AB, FLAT)),
    E_FLAT(Note.of(DE, FLAT)),
    A_FLAT(Note.of(GA, FLAT)),
    D_FLAT(Note.of(CD, FLAT)),
    C_SHARP(Note.of(CD, SHARP)),
    G_FLAT(Note.of(FG, FLAT)),
    F_SHARP(Note.of(FG, SHARP)),
    B(Note.of(B_, null)),
    C_FLAT(Note.of(B_, FLAT)),
    E(Note.of(E_, null)),
    A(Note.of(A_, null)),
    D(Note.of(D_, null)),
    G(Note.of(G_, null));

    companion object {
        private val CACHED_NOTES = mutableMapOf<Pair<Key, Scale>, List<Note>>()
    }

    fun getNotesOfScale(scale: Scale): List<Note> {
        if (CACHED_NOTES.containsKey(this to scale)) {
            return CACHED_NOTES[this to scale]!!
        }
        val majorScale = getMajorScaleNotes()
        val relativeToMajor = scale.getRelativeStepsToMajor()
        val result = mutableListOf<Note>()
        for (pair in relativeToMajor) {
            val noteInMajor = majorScale[pair.first]
            val accidentalToBeAdded = pair.second
            val noteInResult = Note.of(
                noteInMajor.wellTemperedNote.getByOffset(accidentalToBeAdded.getOffset()),
                Accidental.getByOffset(noteInMajor.accidental.getOffset() + accidentalToBeAdded.getOffset())
            )
            result.add(noteInResult)
        }
        CACHED_NOTES[this to scale] = result
        return result
    }

    fun getAccidentalCountOfScale(scale: Scale): Int {
        val notes = getNotesOfScale(scale)
        return notes.count { it.accidental != null }
    }

    fun getAccidentalNotesOfScale(scale: Scale): List<Note> = getNotesOfScale(scale).filter { it.accidental != null }

    private fun getMajorScaleNotes(): List<Note> {
        val startUnresolved = startingNote.getBeforeAccidentalWellTemperedNote()
        val unresolveds = mutableListOf(startUnresolved)
        var next = startUnresolved.getNextNoNeedResolveWellTemperedNote()
        while (!unresolveds.contains(next)) {
            unresolveds.add(next)
            next = next.getNextNoNeedResolveWellTemperedNote()
        }
        val wellTemperedNotes = startingNote.wellTemperedNote.getWellTemperedNotesForScale(Scale.IONIAN)
        val result = mutableListOf<Note>()
        for (idx in unresolveds.indices) {
            val unresolve = unresolveds[idx]
            val wellTemperedNote = wellTemperedNotes[idx]
            val accidental =
                Accidental.getByOffset(-wellTemperedNote.getOffset(unresolve))
            result.add(Note.of(wellTemperedNote, accidental))
        }
        return result
    }
}