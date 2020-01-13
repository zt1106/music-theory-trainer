package cc.zengtian.mtt.theory

/**
 * Created by ZengTian on 2019/11/9.
 */
interface Pitch {
    val frequency: Double
}

class MIDIPitch private constructor(val num: Int) : Pitch {

    companion object {
        private val pitchList = mutableListOf<MIDIPitch>().apply {
            repeat(128) {
                add(MIDIPitch(it))
            }
        }

        fun getByNum(num: Int): MIDIPitch {
            return pitchList[num]
        }

        fun getByNote(note: ActualNote, octave: Int = 4): MIDIPitch {
            val num = 12 * (octave + 1) + note.ordinal
            return pitchList[num]
        }
    }

    override val frequency: Double
        get() {
            TODO()
        }


}
