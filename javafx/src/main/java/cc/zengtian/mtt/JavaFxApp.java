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
        primaryStage.setScene(new Scene(view, 1000, 800));
        primaryStage.show();
    }
}
