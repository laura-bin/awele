package controller;

import view.ConsoleUI;

import java.util.Scanner;

/**
 * Management of the game navigation (menu displays and game's actions called)
 * and player's inputs
 */
public class ConsoleController {

    ConsoleUI console = new ConsoleUI();

    /**
     * Starts the application :
     * - displays a welcome message
     * - calls the main menu
     */
    public void start() {
        console.displayWelcomeMessage();
        mainMenu();
    }

    /**
     * Displays main menu choices to call the next game action :
     * - view scores
     * - play new game
     * - quit
     * and call the next action following the player's choice
     */
    private void mainMenu() {
        MenuChoice playerChoice;

        console.displayMainMenu();

        switch (waitPlayerChoice(Menu.MAIN)) {
            case SCORES:
                console.displayScores();
                goBackMenu();
                break;
            case PLAY:
                console.displayNewGameMenu();
                switch (waitPlayerChoice(Menu.DIFFICULTY)) {
                    case NORMAL_MODE:
                        // TODO NORMAL MODE GAME
                        System.out.println("normal mode game");
                        break;
                    case HARD_MODE:
                        // TODO HARD MODE GAME
                        System.out.println("Feature in development");
                        goBackMenu();
                }
                break;
            case QUIT:
                System.exit(0);
        }
    }

    /**
     * Displays the go back menu :
     * - back to main menu
     * - quit
     * calls the next action following the player's choice
     */
    private void goBackMenu() {
        console.displayGoBackMenu();
        switch (waitPlayerChoice(Menu.GO_BACK)) {
            case BACK:
                mainMenu();
                break;
            case QUIT:
                System.exit(0);
        }
    }

    /**
     * Waits the player's input and check if it's a valid choice for the current menu
     * @param menu for which the choice is needed
     * @return a MenuChoice corresponding to the player's input
     */
    private MenuChoice waitPlayerChoice(Menu menu) {
        Scanner sc = new Scanner(System.in);
        String playerInput;

        while (true) {
            playerInput = sc.nextLine();

            for (MenuChoice choice : menu.getChoices()) {
                if(choice.isInputValid(playerInput)) {
                    return choice;
                }
            }
            System.err.println(String.format("%s is not a valid choice", playerInput));
        }
    }

}
