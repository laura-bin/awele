package awele.controller;

import awele.controller.logic.Game;
import awele.controller.logic.GameStatus;
import awele.view.GameMessage;

import java.util.List;

/**
 * Management of the game navigation (menu displays and game's actions called)
 * and player's inputs
 */
public class GameController {

    private final UIController ui;

    /**
     * Constructor
     *
     * @param ui user interface managing the displays and player's inputs
     */
    public GameController(UIController ui) {
        this.ui = ui;
    }

    /**
     * Starts the application and manages the navigation
     */
    public void start() {
        MenuChoice playerChoice = MenuChoice.BACK;

        ui.displayMessage(GameMessage.WELCOME);
        while (playerChoice != MenuChoice.QUIT) {
            switch (playerChoice) {
                case PLAY:
                    playerChoice = ui.menu(Menu.DIFFICULTY);

                    switch (playerChoice) {
                        case NORMAL_MODE:
                            startGame(playerChoice);
                            playerChoice = ui.menu(Menu.GO_BACK);
                            break;
                        case HARD_MODE:
                            playerChoice = ui.menu(Menu.NOT_YET_IMPLEMENTED);
                            break;
                    }
                    break;
                case SCORES:
                    playerChoice = ui.menu(Menu.NOT_YET_IMPLEMENTED);
                    break;
                case BACK:
                    playerChoice = ui.menu(Menu.MAIN);
                    break;
            }
        }
    }

    private void startGame(MenuChoice difficulty) {
        boolean wantToStart = ui.menu(Menu.START_GAME) == MenuChoice.YES;

        Game game = new Game(difficulty, wantToStart);
        ui.displayBoard(game.getGameBoard());

        while (game.getStatus() == GameStatus.IN_PROGRESS) {
            List<Integer> eligibleHouses = game.getActivePlayerEligibleHouseNumbers();
            if (eligibleHouses.isEmpty()) {
                ui.displayMessage(GameMessage.IMPOSSIBLE_MOVE);
                game.collectRemainingSeeds();
            } else {
                int pickedHouseNumber = game.getActivePlayer().pickHouseForSowing(eligibleHouses, ui);
                game.sowSeeds(pickedHouseNumber);
            }
            ui.displayBoard(game.getGameBoard());
        }

        ui.displayMessage(game.getStatus().getMessage());
    }
}
