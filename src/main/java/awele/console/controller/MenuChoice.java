package awele.console.controller;

import awele.ui.GameMessage;

import java.util.Set;

/**
 * Possible choices included in the menus of the application
 * an list of valid input to select them
 */
public enum MenuChoice {

    SCORES(GameMessage.VIEW_SCORES,
            "h", "high", "score", "scores", "high scores"),
    PLAY(GameMessage.NEW_GAME,
            "p", "play", "new", "game", "play new game"),
    NORMAL_MODE(GameMessage.NORMAL_MODE,
            "n", "normal", "normal mode"),
    HARD_MODE(GameMessage.HARD_MODE,
            "h", "hard", "hard mode"),
    YES(GameMessage.YES,
            "y", "yes"),
    NO(GameMessage.NO,
            "n", "no"),
    BACK(GameMessage.GO_BACK,
            "b", "back", "back to main menu", "main", "main menu"),
    QUIT(GameMessage.QUIT,
            "q", "quit");

    /**
     * Base text to display in the user interface
     */
    private final GameMessage message;

    /**
     * Valid inputs to select the menu choice
     */
    private final Set<String> validInputs;

    /**
     * Private constructor
     *
     * @param message
     * @param validChoices
     */
    MenuChoice(GameMessage message, String... validChoices) {
        this.message = message;
        this.validInputs = Set.of(validChoices);
    }

    /**
     * @return the text to display in the UI
     */
    public String getText() {
        return this.message.getText();
    }

    /**
     * Checks if the player input correspond to the valid choices for a menu choice
     *
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
