package cc.zengtian.mtt.ui

import cc.zengtian.mtt.controller.ScaleQuizController
import tornadofx.*

/**
 * Created by ZengTian on 2019/9/22.
 */
class ScaleQuizFragment : Fragment() {

    private val controller = ScaleQuizController()

    override val root = vbox {
        label("hello world") {
            style {
                fontSize = 20.px
            }
        }
    }
}