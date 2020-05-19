package controller;

import view.Console;

import java.util.Scanner;

/**
 * Management of the game navigation (menu displays and game's actions called)
 * and player's inputs
 */
public class ConsoleController {

    Console console = new Console();

    /**
     * Starts the application and manages teh navigation
     */
    public void start() {
        MenuChoice playerChoice = MenuChoice.BACK;

        console.displayWelcomeMessage();
        while (playerChoice != MenuChoice.QUIT) {
            switch(playerChoice) {
                case PLAY:
                    playerChoice = menu(Menu.DIFFICULTY);
                    // TODO
                    break;
                case SCORES:
                    console.displayScores();
                    playerChoice = menu(Menu.GO_BACK);
                    break;
                case BACK:
                    playerChoice = menu(Menu.MAIN);
            }
        }
        System.exit(0);
    }

    /**
     * Displays the right menu and sends back the player's menu choice
     * @param menu
     * @return a MenuChoice corresponding to the player's input
     */
    private MenuChoice menu(Menu menu) {
        console.displayMenu(menu);
        return waitPlayerChoice(menu);
    }

    /**
     * Waits the player's input and check if it's a valid choice for the current menu
     * @param menu for which the choice is needed
     * @return a MenuChoice corresponding to the player's input
     */
    private MenuChoice waitPlayerChoice(Menu menu) {
        Scanner sc = new Scanner(System.in);
        String playerInput;

        while (sc.hasNext()) {
            playerInput = sc.nextLine();

            for (MenuChoice choice : menu.getChoices()) {
                if(choice.isInputValid(playerInput)) {
                    return choice;
                }
            }
            System.err.println(String.format("%s is not a valid choice", playerInput));
        }
        return MenuChoice.QUIT;
    }

}
