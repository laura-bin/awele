package awele.ui.javafx;

import awele.controller.Menu;
import awele.controller.MenuChoice;
import awele.controller.logic.Game;
import awele.controller.logic.GameStatus;
import awele.controller.logic.HumanPlayer;
import awele.controller.logic.VirtualEasyPlayer;
import awele.model.GameBoard;
import awele.view.GameMessage;
import awele.view.javafx.Utils;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.time.Duration;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Board implements Initializable {

    private static final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

    @FXML
    private Label chronometer;

    @FXML
    private Label playerMessage;

    @FXML
    private GridPane gridBoard;

    // private void startGame(MenuChoice difficulty) {

    //     while (game.getStatus() == GameStatus.IN_PROGRESS) {
    //         List<Integer> eligibleHouses = game.getActivePlayerEligibleHouseNumbers();
    //         if (eligibleHouses.isEmpty()) {
    //             ui.displayMessage(GameMessage.IMPOSSIBLE_MOVE);
    //             game.collectRemainingSeeds();
    //         } else {
    //             int pickedHouseNumber = game.getActivePlayer().pickHouseForSowing(eligibleHouses, ui);
    //             game.sowSeeds(pickedHouseNumber);
    //         }
    //         ui.displayBoard(game.getGameBoard());
    //     }
    //
    //     ui.displayMessage(game.getStatus().getMessage());
    // }

    private RootStack root;
    private Game game;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        for (Node node : gridBoard.getChildren()) {
            if (GridPane.getRowIndex(node) == 1 && GridPane.getColumnIndex(node) < GameBoard.N_HOUSES_PER_PLAYER) {
                ((Button) node).setOnAction(e -> {
                    playerPickedHouse(GridPane.getColumnIndex(node) + 1);
                });
            }
        }

        executor.scheduleAtFixedRate(() -> {
            // insert the task in the javafx thread's task queue
            Platform.runLater(() -> {
                if (game == null) {
                    return;
                }
                Duration currentTimeDuration = game.getCurrentTimeDuration();
                chronometer.setText(String.format("%s:%s:%s", currentTimeDuration.toHours(),
                    currentTimeDuration.toMinutesPart(), currentTimeDuration.toSecondsPart()));
            });
        }, 0, 1, TimeUnit.SECONDS);
    }

    public void setRoot(RootStack rootStack) {
        root = rootStack;
    }

    public void startGame(Game game) {
        this.game = game;
        displayBoard(game.getGameBoard());

        turn();
    }

    public void turn() {
        List<Integer> eligibleHouses = game.getActivePlayerEligibleHouseNumbers();
        if (eligibleHouses.isEmpty()) {
            Utils.setText(playerMessage, GameMessage.IMPOSSIBLE_MOVE);
            game.collectRemainingSeeds();
        } else {
            if (game.getActivePlayer() instanceof HumanPlayer) {
                for (Node node : gridBoard.getChildren()) {
                    Integer columnIndex = GridPane.getColumnIndex(node);
                    if (GridPane.getRowIndex(node) == 1 && columnIndex < GameBoard.N_HOUSES_PER_PLAYER) {
                        node.setDisable(!eligibleHouses.contains(columnIndex + 1));
                    }
                }
            } else {
                int pickedHouse = ((VirtualEasyPlayer)game.getActivePlayer()).pickHouseForSowing(eligibleHouses);
                playerPickedHouse(pickedHouse);
            }
        }
    }

    public void playerPickedHouse(int pickedHouseNumber) {
        game.sowSeeds(pickedHouseNumber);
        displayBoard(game.getGameBoard());
        if (game.getStatus() != GameStatus.IN_PROGRESS) {
            Utils.setText(playerMessage, game.getStatus().getMessage());
            return;
        }
        turn();
    }

    public void goBack(ActionEvent actionEvent) {
        this.root.setMenuVisible();
    }

    public void quit(ActionEvent actionEvent) {
        this.root.quit();
    }

    private void displayBoard(GameBoard board) {

        for (Node node : gridBoard.getChildren()) {
            ((Labeled) node).setText(getHouseValue(GridPane.getRowIndex(node), GridPane.getColumnIndex(node), board));
        }


    }

    private String getHouseValue(int row, int column, GameBoard board) {
        int player = (row + 1) % 2;
        if (column < GameBoard.N_HOUSES_PER_PLAYER) {
            if (player == 0) {
                return String.valueOf(board.getHouseValue(column));
            } else {
                return String.valueOf(board.getHouseValue(GameBoard.N_PLAYERS*GameBoard.N_HOUSES_PER_PLAYER - 1 - column));
            }
        } else {
            return String.valueOf(board.getStock(player));
        }
    }
}
