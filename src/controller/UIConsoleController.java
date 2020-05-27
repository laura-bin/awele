package controller;

import model.GameBoard;
import view.GameErrorMessage;
import view.GameMessage;
import view.ConsoleDisplay;

import java.util.InputMismatchException;
import java.util.Scanner;

public class UIConsoleController implements UIController {

    private final ConsoleDisplay console = new ConsoleDisplay();

    /**
     * Displays the right menu, waits the player's input and checks if it's a valid choice for the current menu
     *
     * @param menu for which the choice is needed
     * @return a MenuChoice corresponding to the player's input
     */
    @Override
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
            console.displayMessage(GameErrorMessage.INVALID_INPUT, playerInput);
        }
        return MenuChoice.QUIT;
    }

    /**
     * Wait an integer on the standard input
     * @return the house number picked by the human player
     */
    @Override
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

    @Override
    public void displayMessage(GameMessage message, Object... params) {
        console.displayMessage(message, params);
    }

    @Override
    public void displayMessage(GameErrorMessage message, Object... params) {
        console.displayMessage(message, params);
    }

    @Override
    public void displayBoard(GameBoard board) {
        console.displayBoard(board);
    }

    @Override
    public void displayVirtualPlayerPlayingAnimation() {
        console.displayVirtualPlayerPlaying();
    }
}
