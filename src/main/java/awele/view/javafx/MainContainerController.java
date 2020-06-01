package awele.view.javafx;

import awele.controller.GameController;
import awele.controller.MenuChoice;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class MainContainerController implements Initializable {

    @FXML
    private VBox board;

    @FXML
    private CheckBox wantsToStart;

    @FXML
    private RadioButton difficultyHard;

    @FXML
    private RadioButton difficultyNormal;

    private BoardController boardController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        boardController = (BoardController) board.getUserData();
    }

    @FXML
    public void newGame() {
        MenuChoice choice;
        if (difficultyNormal.isSelected()) {
            choice = MenuChoice.NORMAL_MODE;
        } else if (difficultyHard.isSelected()){
            choice = MenuChoice.HARD_MODE;
        } else {
            throw new IllegalStateException("invalid state, neither normal nor hard difficulty selected");
        }

        boolean wantsToStart = this.wantsToStart.isSelected();

        boardController.newGame(choice, wantsToStart);
    }
}
