package awele.console.view;

import awele.console.controller.Menu;
import awele.console.controller.MenuChoice;
import awele.gamelogic.PlayerType;
import awele.model.GameBoard;
import awele.ui.GameErrorMessage;
import awele.ui.GameMessage;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * User interface management in a terminal :
 * - displays the menus, boards, scores
 */
public class ConsoleDisplay {

    /**
     * Displays the menu choices for a given menu
     *
     * @param menuDisplayed Menu enum
     */
    public void displayMenu(Menu menuDisplayed) {
        System.out.println();
        System.out.println(menuDisplayed.getTitle());
        for (MenuChoice choice : menuDisplayed.getChoices()) {
            System.out.println(choice.getText());
        }
    }

    /**
     * Displays the game board consisting of 2 rows of nHousesPerPlayer houses and two seed stocks
     *
     * @param board GameBoard to display
     */
    public void displayBoard(GameBoard board) {
        String separatorString = "   " + "+-----".repeat(GameBoard.N_HOUSES_PER_PLAYER) + "+ +-----+";

        System.out.println();
        System.out.println(separatorString);
        System.out.println(fillNorthHouses(board.getHousesValuesByPlayer(PlayerType.VIRTUAL), board.getStockByPlayer(PlayerType.VIRTUAL)));
        System.out.println(separatorString);
        System.out.println(fillSouthHouses(board.getHousesValuesByPlayer(PlayerType.HUMAN), board.getStockByPlayer(PlayerType.HUMAN)));
        System.out.println(separatorString);
    }

    /**
     * Fills the houses (& stock) in the reversed order
     *
     * @param houses houses array to display
     * @param stock  stock int to display
     * @return the formatted string with the houses values in the reversed order
     */
    private String fillNorthHouses(List<Integer> houses, int stock) {
        StringBuilder sb = new StringBuilder("   ");
        for (int house : houses) sb.insert(3, String.format("| %2d  ", house));
        sb.append(String.format("| | %2d  |", stock));
        return sb.toString();
    }

    /**
     * Fills the houses (& stock)
     *
     * @param houses houses array to display
     * @param stock  stock int to display
     * @return the formatted string with the houses values
     */
    private String fillSouthHouses(List<Integer> houses, int stock) {
        StringBuilder sb = new StringBuilder("   ");
        for (int house : houses) sb.append(String.format("| %2d  ", house));
        sb.append(String.format("| | %2d  |", stock));
        return sb.toString();
    }

    /**
     * Displays a message on the standard output
     *
     * @param message GameMessage message
     */
    public void displayMessage(GameMessage message, Object... params) {
        System.out.println(message.getText(params));
    }

    /**
     * Displays an error message on the standard output
     *
     * @param message GameErrorMessage enum
     */
    public void displayMessage(GameErrorMessage message, Object... params) {
        System.err.println(message.getText(params));
    }

    /**
     * Displays a waiting message when the virtual player is picking a house
     */
    public void displayVirtualPlayerPlaying() {
        System.out.print("Virtual player is picking a house");
        for (int i = 0; i < 3; i++) {
            try {
                System.out.print(".");
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println();
    }

}
