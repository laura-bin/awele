package awele.ui.javafx;

import awele.controller.MenuChoice;
import awele.view.javafx.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;

import java.net.URL;
import java.util.ResourceBundle;

public class NewGame implements Initializable {

    @FXML
    private RadioButton difficultyNormal;

    @FXML
    private RadioButton difficultyHard;

    @FXML
    private CheckBox wantsToStart;

    @FXML
    private Button playButton;

    @FXML
    private Button backButton;

    @FXML
    private Label settingsTitle;

    private RootStack root;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Utils.setText(settingsTitle);
    }

    public void setRoot(RootStack rootStack) {
        root = rootStack;
    }

    public void play(ActionEvent actionEvent) {

        MenuChoice choice;
        if (difficultyNormal.isSelected()) {
            choice = MenuChoice.NORMAL_MODE;
        } else if (difficultyHard.isSelected()){
            choice = MenuChoice.HARD_MODE;
        } else {
            throw new IllegalStateException("invalid state, neither normal nor hard difficulty selected");
        }

        boolean wantsToStart = this.wantsToStart.isSelected();

        root.setBoardVisible(choice, wantsToStart);
    }

    public void back(ActionEvent actionEvent) {
        root.setMenuVisible();
    }
}
