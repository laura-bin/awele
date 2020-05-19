package view;

import controller.Menu;
import controller.MenuChoice;
import model.Board;

/**
 * User interface management in a terminal :
 * - displays the menus, boards, scores
 */
public class ConsoleUI {

    /**
     * Displays a welcome message at the start of the app
     */
    public void displayWelcomeMessage() {
        System.out.println("= = = = = = = = = = = = = = = = = = = =");
        System.out.println("= = = =  WELCOME TO AWELE GAME  = = = =");
        System.out.println("= = = = = = = = = = = = = = = = = = = =");
    }

    /* ************************************
     *           MENU DISPLAYS            *
     **************************************/
    /**
     * Displays main menu on the std output =
     * - show high scores
     * - start new game
     * - quit
     */
    public void displayMainMenu() {
        System.out.println();
        System.out.println("= = = = = = =  MAIN MENU  = = = = = = =");
        displayMenu(Menu.MAIN);
    }

    /**
     * Displays the menu to go back to the main menu or quit
     */
    public void displayGoBackMenu() {
        System.out.println();
        System.out.println("= = = = = = = = = = = = = = = = = = = =");
        displayMenu(Menu.GO_BACK);
    }

    /**
     * Displays the new game menu (selection of the game difficulty) :
     * - normal mode
     * - hard mode
     */
    public void displayNewGameMenu() {
        System.out.println();
        System.out.println("= = = = = = =  NEW  GAME  = = = = = = =");
        displayMenu(Menu.DIFFICULTY);
    }

    /**
     * Displays the menu choices for a given menu
     * @param menuDisplayed
     */
    private void displayMenu (Menu menuDisplayed) {
        for (MenuChoice choice : menuDisplayed.getChoices()) {
            System.out.println(choice.getText());
        }
    }

    /* ************************************
     *            HIGH SCORES             *
     **************************************/
    /**
     * Displays the high scores table
     */
    public void displayScores() {
        System.out.println();
        System.out.println("= = = = = = = HIGH SCORES = = = = = = =");
        System.out.println();
        System.out.println("This feature will be developed\nin the third version of AWELE"); // TODO
    }

}
