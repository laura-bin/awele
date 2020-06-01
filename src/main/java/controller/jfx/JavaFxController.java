package controller.jfx;

import controller.GameController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class JavaFxController extends Application {

    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view.fxml"));
        Parent root = loader.load();

        stage.setTitle("AWELE");
        //StackPane root = new StackPane();
        //root.getChildren().add(new Label("Hello world"));
        stage.setScene(new Scene(root));
        stage.show();

        new GameController(loader.getController()).start();

        // stage.close();
    }
}
