package cc.zengtian.st

/**
 * Created by ZengTian on 2019/9/5.
 */
import cc.zengtian.st.ui.QuestionConfigView
import javafx.stage.Stage
import tornadofx.App
import tornadofx.Stylesheet

class FxApp : App(QuestionConfigView::class, Styles::class) {
    override fun start(stage: Stage) {
//        stage.width = 800.0
//        stage.height = 600.0
        super.start(stage)
    }
}

class Styles : Stylesheet() {
//    init {
//        label {
//            fontSize = 20.px
//            fontWeight = FontWeight.BOLD
//            backgroundColor += c("#cecece")
//        }
//    }
}