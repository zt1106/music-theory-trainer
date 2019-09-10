package cc.zengtian.mtt.model.theory

class Chord private constructor(val rootNote: Note,
                                val otherNote: Set<Note>){
    val type by lazy {  }
}

enum class ChordType {
    MAJOR,
    MINOR,
    AUGMENTED,
    DIMISHED
}