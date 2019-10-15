package cc.zengtian.mtt.ui

import cc.zengtian.mtt.controller.BaseQuizController
import cc.zengtian.mtt.util.CompositeProperty
import javafx.geometry.Pos
import javafx.scene.Parent
import javafx.scene.layout.Priority
import tornadofx.*

/**
 * Created by ZengTian on 10/15/2019.
 */
abstract class BaseHeaderQuizFragment<C : BaseQuizController<*>> : Fragment() {

    val ctr by lazy { createController() }

    override val root = vbox {
        hbox {
            spacing = 30.0
            label {
                ctr.correctCountProp.addListener { count -> text = "Correct: $count" }
            }
            label {
                ctr.answeredCountProp.addListener { count -> text = "Answered: $count" }
            }
            label {
                CompositeProperty(ctr.answeredCountProp, ctr.correctCountProp).anyChanged {
                    val rate = String.format("%.2f", ctr.correctCount.toDouble() * 100 / ctr.answeredCount)
                    text = "Rate: $rate%"
                }
            }
            label {
                ctr.usedTimeMedianProp.addListener { milli ->
                    text = "Time Median: ${String.format("%.2f", milli.toDouble() / 1000)}s"
                }
            }
            hbox {
                alignment = Pos.BASELINE_RIGHT
                hgrow = Priority.ALWAYS
                button("Quit").setOnAction {
                    jumpToConfig()
                }
            }
        }
        add(createQuizBody())
        ctr.notifyAllPropsWithOldValue()
    }

    abstract fun createController() : C

    abstract fun createQuizBody() : Parent

    abstract fun jumpToConfig()
}
