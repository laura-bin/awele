package view;

import controller.Menu;
import controller.MenuChoice;

/**
 * User interface management in a terminal :
 * - displays the menus, boards, scores
 */
public class Console {

    /**
     * Displays a welcome message at the start of the app
     */
    public void displayWelcomeMessage() {
        System.out.println("= = = = = = = = = = = = = = = = = = = =");
        System.out.println("= = = =  WELCOME TO AWELE GAME  = = = =");
        System.out.println("= = = = = = = = = = = = = = = = = = = =");
    }

    /**
     * Displays the menu choices for a given menu
     * @param menuDisplayed
     */
    public void displayMenu (Menu menuDisplayed) {
        System.out.println();
        System.out.println(menuDisplayed.getTitle());
        for (MenuChoice choice : menuDisplayed.getChoices()) {
            System.out.println(choice.getText());
        }
    }

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
