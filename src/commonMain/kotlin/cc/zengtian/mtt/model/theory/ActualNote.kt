package cc.zengtian.mtt.model.theory

import kotlin.math.absoluteValue

/**
 * represent 12 well tempered notes
 */
enum class ActualNote {

    C_, CD, D_, DE, E_, F_, FG, G_, GA, A_, AB, B_;

    val needResolve: Boolean by lazy { !name.endsWith("_") }

    val keys: List<Key> by lazy { Key.values().filter { it.startingNote.actual == this } }

    /**
     * high: > 0
     * low: < 0
     */
    fun getByOffset(offset: Int): ActualNote {
        val result = (ordinal + offset) % 12
        return if (result >= 0) {
            ofIdx(result)
        } else {
            ofIdx(result + 12)
        }
    }

    fun getStepsToLeft(another: ActualNote): Int {
        return if (another.ordinal <= this.ordinal) {
            this.ordinal - another.ordinal
        } else {
            this.ordinal + 12 - another.ordinal
        }
    }

    fun getStepsToRight(another: ActualNote): Int {
        return if (another.ordinal >= this.ordinal) {
            another.ordinal - this.ordinal
        } else {
            another.ordinal + 12 - this.ordinal
        }
    }

    fun getShortestOffsetTo(another: ActualNote): Int {
        val left = getStepsToLeft(another)
        val right = getStepsToRight(another)
        return if (right < left) {
            right
        } else {
            -left
        }
    }

    private fun ofIdx(idx: Int): ActualNote = values()[idx]

    fun getActualNotesForScale(scale: Scale): List<ActualNote> {
        val list = mutableListOf<ActualNote>()
        list.add(this)
        for (step in scale.steps) {
            list.add(list[list.size - 1].getByOffset(step))
        }
        return list
    }

    fun getNoNeedResolveByOffset(offset: Int): ActualNote {
        require(!needResolve)
        var result = this
        repeat(offset.absoluteValue) {
            result = result.getNextNoNeedResolveActualNote(offset > 0)
        }
        return result
    }

    fun getNextNoNeedResolveActualNote(toRight: Boolean = true): ActualNote {
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

    private val toString: String by lazy {
        if (name.endsWith("_")) {
            name.substring(0, 1)
        } else {
            name
        }
    }

    override fun toString(): String = toString
}


