package awele.ui.javafx;

import javafx.event.ActionEvent;

public class Scores {

    private RootStack root;

    public void setRoot(RootStack root) {
        this.root = root;
    }

    public void goBack(ActionEvent actionEvent) {
        this.root.setMenuVisible();
    }
}
