package awele.console.controller;

import awele.console.view.ConsoleDisplay;
import awele.gamelogic.DifficultyLevel;
import awele.gamelogic.Game;
import awele.gamelogic.GameStatus;
import awele.model.GameBoard;
import awele.ui.GameErrorMessage;
import awele.ui.GameMessage;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

/**
 * Management of the game navigation (menu displays and game's actions called)
 * and player's inputs
 */
public class ConsoleGameController {

    private final ConsoleDisplay console = new ConsoleDisplay();

    /**
     * Starts the application and manages the navigation
     */
    public void start() {
        MenuChoice playerChoice = MenuChoice.BACK;

        displayMessage(GameMessage.WELCOME);
        while (playerChoice != MenuChoice.QUIT) {
            switch (playerChoice) {
                case PLAY:
                    playerChoice = menu(Menu.DIFFICULTY);
                    boolean wantToStart = menu(Menu.START_GAME) == MenuChoice.YES;

                    switch (playerChoice) {
                        case NORMAL_MODE:
                            startGame(new Game(DifficultyLevel.NORMAL, wantToStart));
                            playerChoice = menu(Menu.GO_BACK);
                            break;
                        case HARD_MODE:
                            displayMessage(GameErrorMessage.NOT_YET_IMPLEMENTED);
                            playerChoice = menu(Menu.NOT_YET_IMPLEMENTED);
                            break;
                    }
                    break;
                case SCORES:
                    displayMessage(GameErrorMessage.NOT_YET_IMPLEMENTED);
                    playerChoice = menu(Menu.NOT_YET_IMPLEMENTED);
                    break;
                case BACK:
                    playerChoice = menu(Menu.MAIN);
                    break;
            }
        }
    }

    /**
     * Main loop (avoids the recursive call of turn())
     *
     * @param game game to start
     */
    protected void startGame(Game game) {
        displayBoard(game.getGameBoard());

        // launch a new game turn while the game status is in progress
        while (game.getStatus() == GameStatus.IN_PROGRESS) {
            List<Integer> eligibleHouses = game.getActivePlayerEligibleHouseNumbers();

            // if no move is possible, display the right message, collect remaining seeds adn end game
            if (eligibleHouses.isEmpty()) {
                displayMessage(GameMessage.IMPOSSIBLE_MOVE);
                game.collectRemainingSeeds();
            } else {
                int pickedHouse;
                int lastHouseIndex;

                // the house is picked by the right player
                if (game.activePlayerIsHuman()) {
                    displayMessage(GameMessage.PICK_HOUSE);
                    pickedHouse = waitNumber();

                    while (!eligibleHouses.contains(pickedHouse)) {
                        if (pickedHouse < 0) {
                            displayMessage(GameErrorMessage.HOUSE_NOT_INT);
                        } else {
                            displayMessage(GameErrorMessage.INVALID_HOUSE);
                        }
                        pickedHouse = waitNumber();
                    }
                } else {
                    pickedHouse = game.getVirtualPlayerPickedHouse();
                    displayVirtualPlayerPlayingAnimation();
                    displayMessage(GameMessage.VIRTUAL_PLAYER_CHOICE, pickedHouse);
                }

                lastHouseIndex = game.sowSeeds(pickedHouse);
                game.captureSeeds(lastHouseIndex);
                game.switchActivePlayer();
                displayBoard(game.getGameBoard());
            }
        }
        displayMessage(game.getStatus().getMessage());
    }

    /**
     * Displays the right menu, waits the player's input and checks if it's a valid choice for the current menu
     *
     * @param menu for which the choice is needed
     * @return a MenuChoice corresponding to the player's input
     */
    private MenuChoice menu(Menu menu) {
        Scanner sc = new Scanner(System.in);
        String playerInput;

        console.displayMenu(menu);

        while (sc.hasNext()) {
            playerInput = sc.nextLine();

            for (MenuChoice choice : menu.getChoices()) {
                if (choice.isInputValid(playerInput)) {
                    return choice;
                }
            }
            console.displayMessage(GameErrorMessage.INVALID_INPUT, playerInput);
        }
        return MenuChoice.QUIT;
    }

    /**
     * Wait an integer on the standard input
     *
     * @return the house number picked by the human player
     */
    private int waitNumber() {
        Scanner sc = new Scanner(System.in);
        if (sc.hasNext()) {
            try {
                return sc.nextInt();
            } catch (InputMismatchException e) {
                return -1;
            }
        } else {
            System.exit(0);
            return -1;
        }
    }

    private void displayMessage(GameMessage message, Object... params) {
        console.displayMessage(message, params);
    }

    private void displayMessage(GameErrorMessage message, Object... params) {
        console.displayMessage(message, params);
    }

    private void displayBoard(GameBoard board) {
        console.displayBoard(board);
    }

    private void displayVirtualPlayerPlayingAnimation() {
        console.displayVirtualPlayerPlaying();
    }

}
