package controller.console;

import controller.game.GameStatus;
import model.GameBoard;
import view.GameErrorMessage;
import view.GameMessage;
import view.ConsoleDisplay;

import java.util.InputMismatchException;
import java.util.Scanner;

public class ConsoleController {

    private final ConsoleDisplay console = new ConsoleDisplay();

    /**
     * Displays the right menu, waits the player's input and checks if it's a valid choice for the current menu
     *
     * @param menu for which the choice is needed
     * @return a MenuChoice corresponding to the player's input
     */
    public MenuChoice menu(Menu menu) {
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
            System.err.println(String.format("%s is not a valid choice", playerInput));
        }
        return MenuChoice.QUIT;
    }

    /**
     * Wait an integer on the standard input
     * @return the house number picked by the human player
     */
    public int waitNumber() {
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

    public void displayWelcomeMessage() {
        console.displayWelcomeMessage();
    }

    public void displayMessage(String message) {
        console.displayMessage(message);
    }

    public void displayMessage(GameMessage message) {
        console.displayMessage(message);
    }

    public void displayMessage(GameStatus status) {
        console.displayMessage(status);
    }

    public void displayMessage(GameErrorMessage errorMessage) {
        console.displayMessage(errorMessage);
    }

    public void displayBoard(GameBoard board) {
        console.displayBoard(board);
    }

    public void displayVirtualPlayerPlaying() {
        console.displayVirtualPlayerPlaying();
    }

}
