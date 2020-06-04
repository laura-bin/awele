package awele.view.javafx;

import javafx.event.ActionEvent;

public class Menu {

    private RootStack root;

    public void setRoot(RootStack root) {
        this.root = root;
    }

    public void newGame(ActionEvent actionEvent) {
        this.root.setNewGameVisible();
    }
}
