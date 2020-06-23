package awele.javafx.controller;

import awele.model.Score;
import awele.services.Database;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.Timestamp;
import java.util.ResourceBundle;

public class ScoreTablePane implements Initializable {

    @FXML
    private TableView<Score> scoreTable;
    @FXML
    private TableColumn<Score, Timestamp> dateColumn;
    @FXML
    private TableColumn<Score, Integer> durationColumn;
    @FXML
    private TableColumn<Score, String> winnerColumn;
    @FXML
    private TableColumn<Score, Integer> humanScoreColumn;
    @FXML
    private TableColumn<Score, Integer> machineScoreColumn;

    @FXML
    private Button goBackButton;

    private RootStack root;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Utils.setText(goBackButton);
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        durationColumn.setCellValueFactory(new PropertyValueFactory<>("duration"));
        winnerColumn.setCellValueFactory(new PropertyValueFactory<>("winnerName"));
        humanScoreColumn.setCellValueFactory(new PropertyValueFactory<>("humanPlayerScore"));
        machineScoreColumn.setCellValueFactory(new PropertyValueFactory<>("virtualPlayerScore"));
        Database.getInstance().getScores(scoreTable.getItems());
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
     * Go back to main menu
     */
    public void goBack() {
        root.setMainMenuVisible();
    }
}
