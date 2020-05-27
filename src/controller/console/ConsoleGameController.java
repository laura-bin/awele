package controller.console;

import controller.game.GameController;
import controller.game.GameStatus;
import view.GameMessage;

import java.util.List;

/**
 * Management of the game navigation (menu displays and game's actions called)
 * and player's inputs
 */
public class ConsoleGameController {

    private final ConsoleController console = new ConsoleController();

    /**
     * Starts the application and manages teh navigation
     */
    public void start() {
        MenuChoice playerChoice = MenuChoice.BACK;

        console.displayWelcomeMessage();
        while (playerChoice != MenuChoice.QUIT) {
            switch (playerChoice) {
                case PLAY:
                    playerChoice = console.menu(Menu.DIFFICULTY);

                    switch (playerChoice) {
                        case NORMAL_MODE:
                            startGame(playerChoice);
                            playerChoice = console.menu(Menu.GO_BACK);
                            break;
                        case HARD_MODE:
                            playerChoice = console.menu(Menu.NOT_YET_IMPLEMENTED);
                            break;
                    }
                    break;
                case SCORES:
                    playerChoice = console.menu(Menu.NOT_YET_IMPLEMENTED);
                    break;
                case BACK:
                    playerChoice = console.menu(Menu.MAIN);
                    break;
            }
        }
    }

    private void startGame(MenuChoice difficulty) {
        boolean wantToStart = console.menu(Menu.START_GAME) == MenuChoice.YES;

        GameController game = new GameController(difficulty, wantToStart);
        console.displayBoard(game.getGameBoard());

        while (game.getStatus() == GameStatus.IN_PROGRESS) {
            List<Integer> eligibleHouses = game.getActivePlayerEligibleHouseNumbers();
            if (eligibleHouses.isEmpty()) {
                console.displayMessage(GameMessage.IMPOSSIBLE);
                game.collectRemainingSeeds();
            } else {
                int pickedHouseNumber = game.getActivePlayer().pickHouseForSowing(eligibleHouses, console);
                game.sowSeeds(pickedHouseNumber);
            }
            console.displayBoard(game.getGameBoard());
        }

        console.displayMessage(game.getStatus());
    }

}
