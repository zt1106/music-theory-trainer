package cc.zengtian.mtt.ui

import javafx.scene.control.Button

/**
 * Created by ZengTian on 10/15/2019.
 */
typealias ButtonWithAnswer = Pair<Button, Any>
val ButtonWithAnswer.button
    get() = first
val ButtonWithAnswer.answer
    get() = second
