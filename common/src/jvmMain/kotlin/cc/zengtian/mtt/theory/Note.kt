package cc.zengtian.mtt.theory

import cc.zengtian.mtt.theory.ActualNote.*
import jm.util.Play

actual fun Note.play(duration: Double) {
    val jMusicNote = toJMusicNote(duration)
    Play.midi(jMusicNote)

}

fun Note.toJMusicNote(duration: Double = 1.0): jm.music.data.Note {
    when (this.actual) {
        C -> TODO()
        CD -> TODO()
        D -> TODO()
        DE -> TODO()
        E -> TODO()
        F -> TODO()
        FG -> TODO()
        G -> TODO()
        GA -> TODO()
        A -> TODO()
        AB -> TODO()
        B -> TODO()
    }
}