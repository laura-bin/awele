package view;

public enum GameErrorMessage {
    INVALID_HOUSE("Invalid house, pick another one."),
    HOUSE_NOT_INT("You must pick an integer corresponding to the house position.");

    /**
     * Message to display in the user interface
     */
    private final String text;

    /**
     * Private constructor
     *
     * @param text
     */
    GameErrorMessage(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
