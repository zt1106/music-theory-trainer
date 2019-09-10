package cc.zengtian.mtt.model.theory

import kotlin.math.absoluteValue

/**
 * @param needResolve whether need to be transformed to a sharp or flat note in music theory
 * represent 12 well tempered notes
 */
enum class WellTemperedNote(val needResolve: Boolean) {
    C_(false),
    CD(true),
    D_(false),
    DE(true),
    E_(false),
    F_(false),
    FG(true),
    G_(false),
    GA(true),
    A_(false),
    AB(true),
    B_(false);

    val keys: List<Key> by lazy { Key.values().filter { it.startingNote.wTN == this } }

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

    fun getNoNeedResolveByOffset(offset: Int) : WellTemperedNote {
        require(!needResolve)
        var result = this
        repeat(offset.absoluteValue) {
            result = result.getNextNoNeedResolveWellTemperedNote(offset > 0)
        }
        return result
    }

    fun getNextNoNeedResolveWellTemperedNote(toRight: Boolean = true): WellTemperedNote {
        val offset = if (toRight) {
            1
        } else {
            -1
        }
        var cur = this.getByOffset(offset)
        while (cur != this) {
            if (!cur.needResolve) {
                return cur
            }
            cur = cur.getByOffset(offset)
        }
        throw IllegalStateException()
    }

    override fun toString(): String {
        val s = super.toString()
        return if (!needResolve) {
            s.substring(0, 1)
        } else {
            s
        }
    }
}