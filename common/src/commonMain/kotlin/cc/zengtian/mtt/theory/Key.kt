package cc.zengtian.mtt.theory

import cc.zengtian.mtt.theory.Scale.Companion.HARMONIC_MINOR
import cc.zengtian.mtt.theory.Scale.Companion.MAJOR
import cc.zengtian.mtt.theory.Scale.Companion.MINOR

enum class Key(val startingNote: Note) {
    C(Note.C),
    F(Note.F),
    B_FLAT(Note.B_FLAT),
    E_FLAT(Note.E_FLAT),
    A_FLAT(Note.A_FLAT),
    D_FLAT(Note.D_FLAT),
    C_SHARP(Note.C_SHARP),
    G_FLAT(Note.G_FLAT),
    F_SHARP(Note.F_SHARP),
    B(Note.B),
    C_FLAT(Note.C_FLAT),
    E(Note.E),
    A(Note.A),
    D(Note.D),
    G(Note.G);

    companion object {
        private val CACHED_NOTES = mutableMapOf<Pair<Key, Scale>, List<Note>>()
        fun ofNote(note: Note): Key? {
            return values().find { it.startingNote == note }
        }
    }

    val relativeMajor by lazy { getNotesOfScale(MINOR)[2].key!! }
    val relativeMinor by lazy { majorScaleNotes[5].key!! }

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

    fun getDiatonicChord(num: Int, type: DiatonicScaleType = DiatonicScaleType.MAJOR, diatonicChordType: DiatonicChordType = DiatonicChordType.TRIAD): Chord {
        val scale = when (type) {
            DiatonicScaleType.MAJOR -> MAJOR
            DiatonicScaleType.MINOR -> HARMONIC_MINOR
        }
        val scaleNotes = getNotesOfScale(scale)
        val notes = mutableListOf<Note>()
        val offset = num - 1
        for (idx in diatonicChordType.indexInScale) {
            var offsetAdded = idx + offset
            if (offsetAdded > 6) {
                offsetAdded -= 7
            }
            var note = scaleNotes[offsetAdded]
            // special case for minor diatonic chord
            // eg: for a-minor, the third chord is a major triad, not a augmented triad
            if (type == DiatonicScaleType.MINOR && num == 3 && idx == 4) {
                note = relativeMajor.getNotesOfScale(MAJOR)[4]
            }
            notes.add(note)
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
