package awele.javafx.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class MainMenuPane implements Initializable {

    @FXML
    private Label mainMenuTitle;
    @FXML
    private Button playButton;
    @FXML
    private Button scoresButton;
    @FXML
    private Button quitButton;

    private RootStack root;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // sets a GameMessage in the view components
        Utils.setText(mainMenuTitle);
        Utils.setText(playButton);
        Utils.setText(scoresButton);
        Utils.setText(quitButton);
    }

    /**
     * Sets the root stack that manages the navigation in the application
     *
     * @param root root stack
     */
    public void setRoot(RootStack root) {
        this.root = root;
    }

    /**
     * Displays the new game menu (game settings)
     */
    public void displayGameSettingsMenu() {
        root.setGameSettingsMenuVisible();
    }

    /**
     * Displays the score table
     */
    public void displaysScoreTable() {
        root.setScoreTableVisible();
    }

    /**
     * Quits the application
     */
    public void quit() {
        root.quit();
    }
}
