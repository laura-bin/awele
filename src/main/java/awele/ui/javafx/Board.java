package awele.ui.javafx;

import awele.controller.logic.Game;
import javafx.event.ActionEvent;

public class Board {

    private RootStack root;
    private Game game;

    public void setRoot(RootStack rootStack) {
        root = rootStack;
    }

    public void setGame(Game game) {
        this.game = game;

    }

    public void goBack(ActionEvent actionEvent) {
        this.root.setMenuVisible();
    }

    public void quit(ActionEvent actionEvent) {
        this.root.quit();
    }
}
