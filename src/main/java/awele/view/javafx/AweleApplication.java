package awele.view.javafx;

import awele.controller.GameController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AweleApplication extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/awele/view/MainContainer.fxml"));
        Parent root = loader.load();

        stage.setTitle("AWELE");
        //StackPane root = new StackPane();
        //root.getChildren().add(new Label("Hello world"));
        stage.setScene(new Scene(root));
        stage.show();
    }
}
