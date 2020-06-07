package awele.ui.javafx;

import awele.controller.Menu;
import awele.controller.MenuChoice;
import awele.controller.logic.Game;
import awele.controller.logic.GameStatus;
import awele.model.GameBoard;
import awele.view.GameMessage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Labeled;
import javafx.scene.layout.GridPane;

import java.util.List;

public class Board {

    @FXML
    private GridPane gridBoard;

    // private void startGame(MenuChoice difficulty) {
    //     boolean wantToStart = ui.menu(Menu.START_GAME) == MenuChoice.YES;
    //
    //     ui.displayBoard(game.getGameBoard());
    //
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

    public void setRoot(RootStack rootStack) {
        root = rootStack;
    }

    public void startGame(Game game) {
        this.game = game;
        displayBoard(game.getGameBoard());

    }

    public void goBack(ActionEvent actionEvent) {
        this.root.setMenuVisible();
    }

    public void quit(ActionEvent actionEvent) {
        this.root.quit();
    }

    private void displayBoard(GameBoard board) {
        for (Node node : gridBoard.getChildren()) {
            ((Labeled) node).setText(String.valueOf(board.getHouseValue(0)));
        }
    }

}
