package jmusic.tutorial

import jm.constants.Durations.MINIM
import jm.constants.Durations.QN
import jm.constants.Pitches.C4
import jm.music.data.Note
import jm.music.data.Part
import jm.music.data.Phrase
import jm.music.data.Score
import jm.util.Play
import jm.util.Write


/**
 * Created by ZengTian on 2019/11/9.
 */
fun main() {
    Play.midi(Note(C4, QN))
    //create a middle C minim (half note)

    //create a middle C minim (half note)
    val n = Note(C4, MINIM)

//create a phrase

    //create a phrase
    val phr = Phrase()

//put the note inside the phrase

    //put the note inside the phrase
    phr.addNote(n)

//pack the phrase into a part

    //pack the phrase into a part
    val p = Part()

    p.addPhrase(phr)

//pack the part into a score titled 'Bing'

    //pack the part into a score titled 'Bing'
    val s = Score("Bing")

    s.addPart(p)

//write the score as a MIDI file to disk

    //write the score as a MIDI file to disk
    Write.midi(s, "Bing.mid")
}
