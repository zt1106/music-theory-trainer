package cc.zengtian.mtt.ui

import cc.zengtian.mtt.controller.CircleOf5thQuizController
import javafx.scene.paint.Color
import tornadofx.*
import kotlin.math.cos
import kotlin.math.sin

/**
 * Created by ZengTian on 10/12/2019.
 */
class CircleOf5thQuizFragment : BaseHeaderQuizFragment<CircleOf5thQuizController>() {

    override fun createQuizBody() = stackpane {
        prefHeight = 800.0
        prefWidth = 800.0
        anchorpane {
            val squareLengthProp =
                cc.zengtian.mtt.util.minOf(this@stackpane.heightProperty(), this@stackpane.widthProperty())
            val strokeProp = squareLengthProp.divide(300)
            val centerXYProp = squareLengthProp.divide(2)
            maxWidthProperty().bind(squareLengthProp)
            maxHeightProperty().bind(squareLengthProp)
            val circle = circle {
                centerXProperty().bind(centerXYProp)
                centerYProperty().bind(centerXYProp)
                radiusProperty().bind(squareLengthProp.divide(3))
                strokeWidthProperty().bind(strokeProp)
                stroke = Color.BLACK
                fill = Color.TRANSPARENT
            }
            repeat(6) {idx ->
                line {
                    val radians = Math.toRadians(15.0 + (idx * 30))
                    val lineLength = circle.radiusProperty().plus(squareLengthProp.divide(10))
                    startXProperty().bind(centerXYProp.minus(lineLength.multiply(sin(radians))))
                    startYProperty().bind(centerXYProp.minus(lineLength.multiply(cos(radians))))
                    endXProperty().bind(centerXYProp.minus(startXProperty()).plus(centerXYProp))
                    endYProperty().bind(centerXYProp.minus(startYProperty()).plus(centerXYProp))
                    stroke = Color.BLACK
                    strokeWidthProperty().bind(strokeProp)
                }
            }
            repeat(12) {idx ->
                val radians = Math.toRadians(idx * 15.0)
                val smallBlockRadius = circle.radiusProperty().divide(2)
                val mediumBlockRadius = circle.radiusProperty().divide(1.1)
                val bigBlockRadius = circle.radiusProperty().multiply(1.2)
            }
        }
    }

    override fun createController() = CircleOf5thQuizController()

    override fun jumpToConfig() {
        TODO()
        replaceWith<ScaleNoteQuizConfigView>()
    }
}
