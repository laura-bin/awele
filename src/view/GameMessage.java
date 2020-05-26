package view;

public enum GameMessage {
    PICK_HOUSE("Pick a house for sowing."),
    IMPOSSIBLE("Impossible to play."); // end because no more move is possible

    /**
     * Message to display in the user interface
     */
    private final String text;

    /**
     * Private constructor
     *
     * @param text
     */
    GameMessage(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
