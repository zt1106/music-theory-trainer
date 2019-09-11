package cc.zengtian.mtt.model.theory

//class Chord private constructor(val rootNote: Note,
//                                val otherNotes: Set<Note>){
//
//    init {
//        require(otherNotes.map { it.wTN }.toMutableSet().apply { this.add(rootNote.wTN) }.size == otherNotes.size + 1) {
//            "duplicate notes found in $rootNote $otherNotes"
//        }
//    }
//
//    val offsetsToRoot : List<Int> by lazy {
//        otherNotes.
//    }
//
//    val type: ChordType? by lazy { TODO() }
//}

open class RootlessChord(private val offsetSet: Set<Int>) {
    init {
        require(offsetSet.size >= 2) { "chord must have at least 3 notes" }
        require(offsetSet.size <= 11) { "chord has too many notes" }
        require(offsetSet.min()!! > 0) { "offset to root must > 0" }
        require(offsetSet.max()!! < 11) { "offset to root must < 11" }
    }

    val offsets: List<Int> by lazy { offsetSet.sorted() }

    val size: Int by lazy { offsetSet.size + 1 }

    val chordType: ChordType? by lazy {

        null
    }
}

class Chord private constructor(val rootNote: Note, offsetSet: Set<Int>) : RootlessChord(offsetSet) {
    companion object {
        fun of(vararg note: Note) : Chord {
            TODO()
        }
    }
}

enum class ChordType {
    MAJOR_TRIAD,
    MINOR_TRIAD,
    AUGMENTED_TRIAD,
    DIMISHED_TRIAD,
    MAJOR_MAJOR_7TH,
    DOMINANT_7TH
}

class ChordMeta

class ChordNotation

class ChordNotationConfig

enum class ChordNotationType
