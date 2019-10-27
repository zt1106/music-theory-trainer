package cc.zengtian.mtt;

import cc.zengtian.mtt.ui.QuizTabsView;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.scenicview.ScenicView;
import tornadofx.FX;

import static cc.zengtian.mtt.util.DebugKt.setGlobalExceptionHandler;

/**
 * Created by ZengTian on 2019/10/27.
 */
public class JavaFxAppTest extends Application  {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        setGlobalExceptionHandler();
        primaryStage.getIcons().clear();
        primaryStage.getIcons().add(new Image("/img/piano-top-view.png"));
        FX.registerApplication(this, primaryStage);
        Parent view = FX.find(QuizTabsView.class).getRoot();
        Scene scene = new Scene(view, 1000, 800);
        primaryStage.setScene(scene);
        ScenicView.show(scene);
        primaryStage.show();
    }
}
