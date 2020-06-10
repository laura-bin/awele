package awele.javafx.controller;

import awele.gamelogic.DifficultyLevel;
import awele.gamelogic.Game;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Root stack containing the javafx panes
 * Navigation management in the game
 */
public class RootStack implements Initializable {

    public StackPane root;      // root stack
    public Pane mainMenu;       // main menu display (play / view scores / quit)
    public Pane newGameMenu;    // new game menu display (game settings)
    public Pane gameBoard;      // game board display
    public Pane scoreTable;    // scores table

    private MainMenuPane mainMenuPaneController;
    private GameSettingsMenuPane gameSettingsMenuPaneController;
    private GameBoardPane gameBoardPaneController;
    private ScoreTablePane scoreTablePaneController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setMainMenuVisible();

        mainMenuPaneController = (MainMenuPane) mainMenu.getUserData();
        gameSettingsMenuPaneController = (GameSettingsMenuPane) newGameMenu.getUserData();
        gameBoardPaneController = (GameBoardPane) gameBoard.getUserData();
        scoreTablePaneController = (ScoreTablePane) scoreTable.getUserData();

        mainMenuPaneController.setRoot(this);
        gameSettingsMenuPaneController.setRoot(this);
        gameBoardPaneController.setRoot(this);
        scoreTablePaneController.setRoot(this);
    }

    /**
     * Sets the main menu pane visible
     */
    public void setMainMenuVisible() {
        setPaneVisible(mainMenu);
    }

    /**
     * Sets the new game menu visible
     */
    public void setGameSettingsMenuVisible() {
        setPaneVisible(newGameMenu);
    }

    /**
     * Create a new game and sets the game board visible
     * @param difficulty game difficulty chosen by the human player
     * @param wantsToStart does the human player want to be the first to play
     */
    public void setGameBoardVisible(DifficultyLevel difficulty, boolean wantsToStart) {
        gameBoardPaneController.startGame(new Game(difficulty, wantsToStart));
        setPaneVisible(gameBoard);
    }

    /**
     * Sets the score table visible
     */
    public void setScoreTableVisible() {
        setPaneVisible(scoreTable);
    }

    /**
     * Sets a pane in the foreground by making it visible and the others invisible
     *
     * @param pane pane to show
     */
    private void setPaneVisible(Pane pane) {
        ObservableList<Node> children = root.getChildren();
        for (Node child : children) {
            child.setVisible(child == pane);
        }
    }

    public void quit() {
        // TODO
        System.exit(0);
    }

}
