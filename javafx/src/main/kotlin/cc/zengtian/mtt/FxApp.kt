package cc.zengtian.mtt

/**
 * Created by ZengTian on 2019/9/5.
 */
import cc.zengtian.mtt.ui.QuizTabsView
import javafx.stage.Stage
import tornadofx.App
import tornadofx.Stylesheet

class FxApp : App(QuizTabsView::class, Styles::class) {
    override fun start(stage: Stage) {
        stage.width = 800.0
        stage.height = 600.0
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

package cc.zengtian.mtt;

import cc.zengtian.mtt.ui.QuizTabsView;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import tornadofx.FX;

/**
 * Created by ZengTian on 9/27/2019.
 */
public class JavaFxApp extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        FX.registerApplication(this, primaryStage);
        Parent view = FX.find(QuizTabsView.class).getRoot();
        primaryStage.setScene(new Scene(view, 600, 400));
        primaryStage.show();
    }
}
