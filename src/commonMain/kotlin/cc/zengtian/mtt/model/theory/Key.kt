package cc.zengtian.mtt.model.theory

import cc.zengtian.mtt.model.theory.Accidental.FLAT
import cc.zengtian.mtt.model.theory.Accidental.SHARP
import cc.zengtian.mtt.model.theory.WellTemperedNote.*

enum class Key(val startingNote: Note) {
    C(Note.ofWellTempered(C_, null)),
    F(Note.ofWellTempered(F_, null)),
    B_FLAT(Note.ofWellTempered(AB, FLAT)),
    E_FLAT(Note.ofWellTempered(DE, FLAT)),
    A_FLAT(Note.ofWellTempered(GA, FLAT)),
    D_FLAT(Note.ofWellTempered(CD, FLAT)),
    C_SHARP(Note.ofWellTempered(CD, SHARP)),
    G_FLAT(Note.ofWellTempered(FG, FLAT)),
    F_SHARP(Note.ofWellTempered(FG, SHARP)),
    B(Note.ofWellTempered(B_, null)),
    C_FLAT(Note.ofWellTempered(B_, FLAT)),
    E(Note.ofWellTempered(E_, null)),
    A(Note.ofWellTempered(A_, null)),
    D(Note.ofWellTempered(D_, null)),
    G(Note.ofWellTempered(G_, null));

    companion object {
        private val CACHED_NOTES = mutableMapOf<Pair<Key, Scale>, List<Note>>()
        fun ofNote(note: Note) : Key? {
            return values().find { it.startingNote == note }
        }
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
            val noteInResult = Note.ofWellTempered(
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
            result.add(Note.ofWellTempered(wellTemperedNote, accidental))
        }
        return result
    }
}