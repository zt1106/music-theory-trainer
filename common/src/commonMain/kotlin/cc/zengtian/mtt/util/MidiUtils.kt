package cc.zengtian.mtt.util

import cc.zengtian.mtt.theory.ActualNote

/**
 * Created by ZengTian on 2019/11/9.
 */
object MidiUtils {
    fun midiValue(actualNote: ActualNote, octave: Int): Int {
        val result = actualNote.ordinal  + 12 * (octave + 1)
        require(result in 0..127) {"invalid midi note"}
        return result
    }
}