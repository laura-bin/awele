package awele.javafx.controller;

import awele.gamelogic.DifficultyLevel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;

import java.net.URL;
import java.util.ResourceBundle;

public class GameSettingsMenuPane implements Initializable {

    @FXML
    private Label settingsTitle;
    @FXML
    private RadioButton normalMode;
    @FXML
    private RadioButton hardMode;
    @FXML
    private CheckBox wantsToStart;
    @FXML
    private Button playButton;
    @FXML
    private Button backButton;

    private RootStack root;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Utils.setText(settingsTitle);
        Utils.setText(normalMode);
        Utils.setText(hardMode);
        Utils.setText(wantsToStart);
        Utils.setText(playButton);
        Utils.setText(backButton);
    }

    /**
     * Sets the root stack that manages the navigation in the application
     * @param root root stack
     */
    public void setRoot(RootStack root) {
        this.root = root;
    }

    /**
     * Starts the game
     */
    public void play() {
        DifficultyLevel difficulty;
        if (normalMode.isSelected()) {
            difficulty = DifficultyLevel.NORMAL;
        } else if (hardMode.isSelected()){
            difficulty = DifficultyLevel.HARD;
        } else {
            throw new IllegalStateException("invalid state, neither normal nor hard difficulty selected");
        }

        boolean wantsToStart = this.wantsToStart.isSelected();

        root.setGameBoardVisible(difficulty, wantsToStart);
    }

    /**
     * Go back to main menu
     */
    public void goBack() {
        root.setMainMenuVisible();
    }
}
