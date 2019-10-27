package cc.zengtian.mtt.util

import javafx.application.Platform
import javafx.event.EventHandler
import javafx.scene.Scene
import javafx.stage.Stage
import org.controlsfx.dialog.ExceptionDialog
import org.scenicview.ScenicView
import tornadofx.*

/**
 * Created by ZengTian on 2019/10/27.
 */
open class DebugRunner(private val ui: UIComponent) : App() {
    override fun start(stage: Stage) {
        setGlobalExceptionHandler()
        val scene = Scene(ui.root, 1000.0, 800.0)
        stage.scene = scene
        ScenicView.show(scene)
        stage.onCloseRequest = EventHandler { Platform.exit() }
        stage.show()
    }
}

fun setGlobalExceptionHandler() {
    Thread.setDefaultUncaughtExceptionHandler { _, e ->
        val dialog = ExceptionDialog(e)
        Platform.runLater { dialog.show() }
    }
}
