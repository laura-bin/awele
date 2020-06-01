package awele.view;

public enum GameMessage {
    WELCOME("= = = = = = = = = = = = = = = = = = = = = = = = = =\n" +
            "= = = = = = =  WELCOME TO AWELE GAME  = = = = = = =\n" +
            "= = = = = = = = = = = = = = = = = = = = = = = = = ="),
    PICK_HOUSE("Pick a house for sowing."),
    VIRTUAL_PLAYER_CHOICE("Virtual player picked house %d."),
    IMPOSSIBLE_MOVE("Impossible to play."),
    WIN("You win !"),
    DRAW("Draw : both players have collected the same amount of seeds."),
    LOSE("Virtual player wins !"),
    EMPTY("");

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

    public String getText(Object... params) {
        return String.format(text, params);
    }
}
