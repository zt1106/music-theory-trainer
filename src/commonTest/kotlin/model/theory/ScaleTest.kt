package model.theory

import printlnAnd
import cc.zengtian.mtt.model.theory.ActualNote.*
import cc.zengtian.mtt.model.theory.Key
import cc.zengtian.mtt.model.theory.Scale
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Created by ZengTian on 2019/9/11.
 */
class ScaleTest {
    @Test
    fun `print all scales`() {
        Key.values().forEach { key ->
            Scale.builtInValues().forEach { scale ->
                println("$key $scale ${key.getNotesOfScale(scale)}")
            }
        }
    }

    @Test
    fun `test major scales using all letters`() {
        val letters = listOf(A, B, C, D, E, F, G)
        Key.values().map { key -> key.getNotesOfScale(Scale.MAJOR).map { note -> note.beforeAccidentalActual } }
            .forEach { beforeAcc ->
                assertTrue(beforeAcc.printlnAnd().containsAll(letters))
            }
    }

    @Test
    fun `test scales except chromatic using unique letters`() {
        val scales = Scale.builtInValues().toMutableList().apply { remove(Scale.CHROMATIC) }
        Key.values().forEach { key ->
            scales.forEach { scale ->
                val notes = key.getNotesOfScale(scale)
                val beforeAcc = notes.map { it.beforeAccidentalActual }.printlnAnd()
                assertEquals(notes.size, beforeAcc.distinct().size)
            }
        }
    }
}