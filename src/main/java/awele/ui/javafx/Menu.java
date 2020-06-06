package awele.ui.javafx;

import awele.view.javafx.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class Menu implements Initializable {

    @FXML
    private Label menuTitle;

    @FXML
    private Button newGameButton;

    private RootStack root;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Utils.setText(menuTitle);
        Utils.setText(newGameButton);
    }

    public void setRoot(RootStack root) {
        this.root = root;
    }

    public void newGame(ActionEvent actionEvent) {
        this.root.setNewGameVisible();
    }

    public void viewScores(ActionEvent actionEvent) {
        this.root.setScoresVisible();
    }

    public void quit(ActionEvent actionEvent) {
        this.root.quit();
    }
}
