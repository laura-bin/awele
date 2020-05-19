package controller;

import java.util.Set;

/**
 * Possible choices included in the menus of the application
 * an list of valid input to select them
 */
public enum MenuChoice {

    SCORES ("High scores",
            "h", "high", "score", "scores", "high scores"),
    PLAY ("Play new game",
            "p", "play", "new", "game", "play new game"),
    NORMAL_MODE("Normal mode",
            "n", "normal", "normal mode"),
    HARD_MODE("Hard mode",
            "h", "hard", "hard mode"),
    BACK ("Back to main menu",
            "b", "back", "back to main menu", "main", "main menu"),
    QUIT ("Quit",
            "q", "quit");

    /**
     * Base text to display in the user interface
     */
    private String text;

    /**
     * Valid inputs to select the menu choice
     */
    private Set<String> validInputs;

    /**
     * Private constructor
     * @param text
     * @param validChoices
     */
    MenuChoice(String text, String... validChoices) {
        this.text = text;
        this.validInputs = Set.of(validChoices);
    }

    /**
     * @return the text to display in the UI
     */
    public String getText() {
        return this.text;
    }

    /**
     * Checks if the player input correspond to the valid choices for a menu choice
     * @param playerInput
     * @return boolean
     */
    public boolean isInputValid(String playerInput) {
        for (String expected : validInputs) {
            if (playerInput.equalsIgnoreCase(expected)) return true;
        }
        return false;
    }

}
