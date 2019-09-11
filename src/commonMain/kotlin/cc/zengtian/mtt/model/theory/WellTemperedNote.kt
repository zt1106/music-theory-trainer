package cc.zengtian.mtt.model.theory

import kotlin.math.absoluteValue

/**
 * represent 12 well tempered notes
 */
enum class WellTemperedNote {

    C_, CD, D_, DE, E_, F_, FG, G_, GA, A_, AB, B_;

    val needResolve: Boolean by lazy { !name.endsWith("_") }

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

    fun getOffsetTo(another: WellTemperedNote): Int {
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

    fun getNoNeedResolveByOffset(offset: Int): WellTemperedNote {
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

    private val showName: String by lazy {
        if (name.endsWith("_")) {
            name.substring(0, 1)
        } else {
            name
        }
    }

    override fun toString(): String = showName
}
