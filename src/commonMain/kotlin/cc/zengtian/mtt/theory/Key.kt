package cc.zengtian.mtt.theory

import cc.zengtian.mtt.theory.Accidental.FLAT
import cc.zengtian.mtt.theory.Accidental.SHARP

enum class Key(val startingNote: Note) {
    C(Note.ofActual(ActualNote.C, null)),
    F(Note.ofActual(ActualNote.F, null)),
    B_FLAT(Note.ofActual(ActualNote.AB, FLAT)),
    E_FLAT(Note.ofActual(ActualNote.DE, FLAT)),
    A_FLAT(Note.ofActual(ActualNote.GA, FLAT)),
    D_FLAT(Note.ofActual(ActualNote.CD, FLAT)),
    C_SHARP(Note.ofActual(ActualNote.CD, SHARP)),
    G_FLAT(Note.ofActual(ActualNote.FG, FLAT)),
    F_SHARP(Note.ofActual(ActualNote.FG, SHARP)),
    B(Note.ofActual(ActualNote.B, null)),
    C_FLAT(Note.ofActual(ActualNote.B, FLAT)),
    E(Note.ofActual(ActualNote.E, null)),
    A(Note.ofActual(ActualNote.A, null)),
    D(Note.ofActual(ActualNote.D, null)),
    G(Note.ofActual(ActualNote.G, null));

    companion object {
        private val CACHED_NOTES = mutableMapOf<Pair<Key, Scale>, List<Note>>()
        fun ofNote(note: Note): Key? {
            return values().find { it.startingNote == note }
        }
    }

    private val majorScaleNotes: List<Note> by lazy {
        val startUnresolved = startingNote.beforeAccidentalActual
        val unresolveds = mutableListOf(startUnresolved)
        var next = startUnresolved.getNextNoNeedResolveActualNote()
        while (!unresolveds.contains(next)) {
            unresolveds.add(next)
            next = next.getNextNoNeedResolveActualNote()
        }
        val actualNotes = startingNote.actual.getActualNotesForScale(Scale.MAJOR)
        val result = mutableListOf<Note>()
        for (idx in unresolveds.indices) {
            val unresolve = unresolveds[idx]
            val actualNote = actualNotes[idx]
            val accidental = Accidental.getByOffset(-actualNote.getShortestOffsetTo(unresolve))
            result.add(Note.ofActual(actualNote, accidental))
        }
        result
    }

    fun getNotesOfScale(scale: Scale): List<Note> {
        if (CACHED_NOTES.containsKey(this to scale)) {
            return CACHED_NOTES[this to scale]!!
        }
        val relativeToMajor = scale.stepPairToMajor
        val result = mutableListOf<Note>()
        for (pair in relativeToMajor) {
            val noteInMajor = majorScaleNotes[pair.first]
            val accidentalToBeAdded = pair.second
            val noteInResult = Note.ofActual(
                noteInMajor.actual.getByOffset(accidentalToBeAdded.offset),
                Accidental.getByOffset(noteInMajor.accidental.offset + accidentalToBeAdded.offset)
            )
            result.add(noteInResult)
        }
        CACHED_NOTES[this to scale] = result
        return result
    }

    fun getDiatonicChord(scale: Scale, diatonicChordType: DiatonicChordType, num : Int) : Chord {
        require(scale.noteCount == 7) { "scale: $scale not supported" }
        val scaleNotes = getNotesOfScale(scale)
        val notes = mutableListOf<Note>()
        val offset = num - 1
        for (idx in diatonicChordType.indexInScale) {
            var offsetAdded = idx + offset
            if (offsetAdded > 6) {
                offsetAdded -= 7
            }
            notes.add(scaleNotes[offsetAdded])
        }
        return Chord(notes)
    }

    fun getAccidentalCountOfScale(scale: Scale): Int {
        val notes = getNotesOfScale(scale)
        return notes.count { it.accidental != null }
    }

    fun getAccidentalNotesOfScale(scale: Scale): List<Note> = getNotesOfScale(scale).filter { it.accidental != null }

    override fun toString(): String {
        return startingNote.toString()
    }
}
