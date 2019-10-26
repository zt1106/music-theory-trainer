package cc.zengtian.mtt.util

import javafx.scene.layout.VBox
import org.controlsfx.dialog.CommandLinksDialog

/**
 * Created by ZengTian on 2019/10/27.
 */
fun openDialog(block: VBox.() -> Unit) {
    val ok = CommandLinksDialog.CommandLinksButtonType("OK", true)
    val cancel = CommandLinksDialog.CommandLinksButtonType("Cancel", false)
    val dialog = CommandLinksDialog(ok, cancel)
    dialog.dialogPane.content = VBox().apply(block)
    dialog.show()
}