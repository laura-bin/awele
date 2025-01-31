package awele.javafx;

import awele.javafx.controller.GameBoardPane;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AweleApplication extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/awele/view/RootStack.fxml"));
        Parent root = loader.load();
        root.getStylesheets().add("/awele/view/styles.css");
        stage.setTitle("AWELE");
        stage.setScene(new Scene(root));
        stage.setOnCloseRequest(event -> GameBoardPane.shutdown()); // stop the application on click on the little cross
        stage.show();
    }
}
