package controller;

import model.GameBoard;
import view.GameErrorMessage;
import view.GameMessage;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.IntConsumer;

public interface UIController {

    /**
     * Displays a menu and waits the player's choice
     * @param menu Menu to display
     * @return MenuChoice corresponding to the player's choice
     */
    void menu(Menu menu, Consumer<MenuChoice> callback);

    /**
     * Waits a number picked by the player
     * @return the integer chosen by the player
     */
    void waitNumber(List<Integer> eligibleHouseNumbers, IntConsumer callback);

    /**
     * Displays a game board
     * @param board GameBoard to display
     */
    void displayBoard(GameBoard board);

    /**
     * Displays a message to the player
     *
     * @param message text to display
     */
    void displayMessage(GameMessage message, Object... params);

    /**
     * Displays an error message to the player
     *
     * @param message text to display
     */
    void displayMessage(GameErrorMessage message, Object... params);

    /**
     * Displays an animation "while" the virtual player picks a number
     */
    void displayVirtualPlayerPlayingAnimation(Runnable callback);

}
