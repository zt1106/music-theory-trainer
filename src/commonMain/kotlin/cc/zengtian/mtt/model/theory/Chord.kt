package cc.zengtian.mtt.model.theory

class Chord private constructor(val rootNote: Note,
                                val otherNote: Set<Note>){
    val type: ChordType by lazy { TODO() }
}

enum class ChordType {
    MAJOR_TRIAD,
    MINOR_TRIAD,
    AUGMENTED_TRIAD,
    DIMISHED_TRIAD,
    MAJOR_MAJOR_7TH,
    DOMINANT_7TH
}