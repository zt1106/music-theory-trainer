package cc.zengtian.mtt.util

import javafx.scene.control.ButtonType
import javafx.scene.control.Dialog
import javafx.scene.control.Label
import javafx.scene.image.Image
import javafx.scene.layout.VBox
import javafx.stage.Stage

/**
 * open simple dialog
 * @return is ok
 */
fun openOkCancelDialog(title: String = "", block: VBox.() -> Unit): Boolean {
    val dialog = Dialog<ButtonType>()
    dialog.title = title
    // buttons
    dialog.dialogPane.buttonTypes.add(ButtonType.OK)
    dialog.dialogPane.buttonTypes.add(ButtonType.CANCEL)
    // remove icon
    val dialogStage = dialog.dialogPane.scene.window as Stage
    dialogStage.icons.add(Image("/img/piano-top-view.png"))
    // x button close like cancel
    val window = dialog.dialogPane.scene.window
    window.setOnCloseRequest { _ ->
        dialog.result = ButtonType.CANCEL
        window.hide()
    }
    // remove extra ui
    dialog.headerText = null;
    dialog.graphic = null;
    dialog.dialogPane.content = VBox().apply(block)
    return dialog.showAndWait().safeGet()?.let {
        when (it) {
            ButtonType.OK -> true
            else -> false
        }
    } ?: false
}

/**
 * show message in dialog
 */
fun openMessageDialog(msg: String) {
    val dialog = Dialog<ButtonType>()
    dialog.title = ""
    // buttons
    dialog.dialogPane.buttonTypes.add(ButtonType.OK)
    // remove icon
    val dialogStage = dialog.dialogPane.scene.window as Stage
    dialogStage.icons.add(Image("/img/piano-top-view.png"))
    // x button close like cancel
    val window = dialog.dialogPane.scene.window
    window.setOnCloseRequest { _ ->
        dialog.result = ButtonType.OK
        window.hide()
    }
    // remove extra ui
    dialog.headerText = null;
    dialog.graphic = null;
    dialog.dialogPane.content = Label(msg)
    dialog.showAndWait()
}