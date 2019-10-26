package cc.zengtian.mtt.ui

import cc.zengtian.mtt.controller.BaseQuizConfigController
import javafx.scene.Parent
import tornadofx.*

/**
 * Created by ZengTian on 2019/10/26.
 */
abstract class BaseQuizConfigView<T : BaseQuizConfigController<*>> : View() {

    private val ctr by lazy { createController() }

    override val root = vbox {
        hbox {
            label("Question Count:")

        }
        add(createBottomPanel())
    }

    abstract fun createController(): T

    abstract fun createBottomPanel(): Parent
}