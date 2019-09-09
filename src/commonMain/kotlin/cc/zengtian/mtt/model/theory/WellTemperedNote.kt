package cc.zengtian.mtt.model.theory

/**
 * @param needResolve whether need to be transformed to a sharp or flat note in music theory
 * represent 12 well tempered notes
 */
enum class WellTemperedNote(val needResolve: Boolean) {
    C(false),
    CD(true),
    D(false),
    DE(true),
    E(false),
    F(false),
    FG(true),
    G(false),
    GA(true),
    A(false),
    AB(true),
    B(false);


    /**
     * high: > 0
     * low: < 0
     */
    fun getByOffset(offset: Int): WellTemperedNote {
        val result = (ordinal + offset) % 12
        return if (result >= 0) {
            ofIdx(result)
        } else {
            ofIdx(result + 12)
        }
    }

    fun getOffset(another: WellTemperedNote): Int {
        if (another == this) {
            return 0
        }
        val rightDistance = if (another.ordinal > this.ordinal) {
            another.ordinal - this.ordinal
        } else {
            another.ordinal + 12 - this.ordinal
        }
        val leftDistance = if (another.ordinal < this.ordinal) {
            this.ordinal - another.ordinal
        } else {
            this.ordinal + 12 - another.ordinal
        }
        return if (rightDistance < leftDistance) {
            rightDistance
        } else {
            -leftDistance
        }
    }

    private fun ofIdx(idx: Int): WellTemperedNote = values()[idx]

    fun getWellTemperedNotesForScale(scale: Scale): List<WellTemperedNote> {
        val list = mutableListOf<WellTemperedNote>()
        list.add(this)
        for (step in scale.steps) {
            list.add(list[list.size - 1].getByOffset(step))
        }
        return list
    }

    fun getNextNoNeedResolveWellTemperedNote(): WellTemperedNote {
        var cur = this.getByOffset(1)
        while (cur != this) {
            if (!cur.needResolve) {
                return cur
            }
            cur = cur.getByOffset(1)
        }
        throw IllegalStateException()
    }
}