package cc.zengtian.mtt.ui

import cc.zengtian.mtt.util.fixedtab
import tornadofx.View
import tornadofx.tabpane

/**
 * Created by ZengTian on 9/25/2019.
 */
class QuizTabsView : View() {
    override val root = tabpane {
        fixedtab<ScaleNoteQuizConfigView>("Note in Scale")
    }
}
