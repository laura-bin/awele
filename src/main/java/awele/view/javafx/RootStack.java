package awele.view.javafx;

import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class RootStack implements Initializable {


    public StackPane root;
    public AnchorPane newGame;
    public VBox menu;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ObservableList<Node> children = root.getChildren();
        for (Node child : children) {
            child.setVisible(child == menu);
        }

        Menu menuController = (Menu) menu.getUserData();
        menuController.setRoot(this);
    }

    public void setNewGameVisible() {
        ObservableList<Node> children = root.getChildren();
        for (Node child : children) {
            child.setVisible(child == newGame);
        }

        children.remove(newGame);
        children.add(newGame);
    }
}
