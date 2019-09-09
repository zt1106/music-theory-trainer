package cc.zengtian.mtt.model.theory

enum class Key(val startingNote: Note) {
    C(Note.of(WellTemperedNote.C, null)),
    F(Note.of(WellTemperedNote.F, null)),
    B_FLAT(Note.of(WellTemperedNote.AB, Accidental.FLAT)),
    E_FLAT(Note.of(WellTemperedNote.DE, Accidental.FLAT)),
    A_FLAT(Note.of(WellTemperedNote.GA, Accidental.FLAT)),
    D_FLAT(Note.of(WellTemperedNote.CD, Accidental.FLAT)),
    C_SHARP(Note.of(WellTemperedNote.CD, Accidental.SHARP)),
    G_FLAT(Note.of(WellTemperedNote.FG, Accidental.FLAT)),
    F_SHARP(Note.of(WellTemperedNote.FG, Accidental.SHARP)),
    B(Note.of(WellTemperedNote.B, null)),
    C_FLAT(Note.of(WellTemperedNote.B, Accidental.FLAT)),
    E(Note.of(WellTemperedNote.E, null)),
    A(Note.of(WellTemperedNote.A, null)),
    D(Note.of(WellTemperedNote.D, null)),
    G(Note.of(WellTemperedNote.G, null));

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