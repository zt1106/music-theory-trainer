package cc.zengtian.st

/**
 * Created by ZengTian on 2019/9/5.
 */
import javafx.scene.text.FontWeight
import tornadofx.*

class HelloWorldApp : App(HelloWorld::class, Styles::class)

class Styles : Stylesheet() {
    init {
        label {
            fontSize = 20.px
            fontWeight = FontWeight.BOLD
            backgroundColor += c("#cecece")
        }
    }
}

class HelloWorld : View() {
    override val root = hbox {
        label("Hello world")
    }
}