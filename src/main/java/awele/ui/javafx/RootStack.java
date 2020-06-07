package awele.ui.javafx;

import awele.controller.MenuChoice;
import awele.controller.logic.Game;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.util.ResourceBundle;

public class RootStack implements Initializable {

    public StackPane root;

    public Pane newGame;
    public Pane menu;
    public Pane board;
    public Pane scores;
    private Menu menuController;
    private Board boardController;
    private NewGame newGameController;
    private Scores scoreController;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ObservableList<Node> children = root.getChildren();
        for (Node child : children) {
            child.setVisible(child == menu);
        }

        menuController = (Menu) menu.getUserData();
        boardController = (Board) board.getUserData();
        newGameController = (NewGame) newGame.getUserData();
        scoreController = (Scores) scores.getUserData();

        boardController.setRoot(this);
        menuController.setRoot(this);
        newGameController.setRoot(this);
        scoreController.setRoot(this);
    }

    public void setNewGameVisible() {
        setPaneVisible(newGame);
    }

    public void setBoardVisible(MenuChoice difficulty, boolean wantsToStart) {
        boardController.startGame(new Game(difficulty, wantsToStart));
        setPaneVisible(board);
    }

    public void setMenuVisible() {
        setPaneVisible(menu);
    }

    public void setScoresVisible() {
        setPaneVisible(scores);
    }

    private void setPaneVisible(Pane pane) {
        ObservableList<Node> children = root.getChildren();
        for (Node child : children) {
            child.setVisible(child == pane);
        }

        children.remove(pane);
        children.add(pane);
    }

    public void quit() {
        // TODO
        System.exit(0);
    }


}
