package controller;

import model.GameBoard;
import view.GameErrorMessage;
import view.GameMessage;
import view.console.ConsoleDisplay;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.function.Consumer;
import java.util.function.IntConsumer;

public class UIConsoleController implements UIController {

    private final ConsoleDisplay console = new ConsoleDisplay();

    /**
     * Displays the right menu, waits the player's input and checks if it's a valid choice for the current menu
     *
     * @param menu for which the choice is needed
     * @return a MenuChoice corresponding to the player's input
     */
    @Override
    public void menu(Menu menu, Consumer<MenuChoice> callback) {
        Scanner sc = new Scanner(System.in);
        String playerInput;

        console.displayMenu(menu);

        while (sc.hasNext()) {
            playerInput = sc.nextLine();

            for (MenuChoice choice : menu.getChoices()) {
                if (choice.isInputValid(playerInput)) {
                    callback.accept(choice);
                    return;
                }
            }
            console.displayMessage(GameErrorMessage.INVALID_INPUT, playerInput);
        }

        callback.accept(MenuChoice.QUIT);
    }

    /**
     * Wait an integer on the standard input
     * @return the house number picked by the human player
     */
    @Override
    public void waitNumber(List<Integer> eligibleHouseNumbers, IntConsumer callback) {
        Scanner sc = new Scanner(System.in);
        if (!sc.hasNext()) {
            System.exit(0);
            return;
        }

        try {
            callback.accept(sc.nextInt());
        } catch (InputMismatchException e) {
            callback.accept(-1);
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
    public void displayVirtualPlayerPlayingAnimation(Runnable callback) {
        console.displayVirtualPlayerPlaying();
        callback.run();
    }
}
