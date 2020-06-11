package awele.ui;

public enum GameMessage {
    WELCOME("Welcome to AWELE game"),
    MAIN_MENU("Main menu"),
    NEW_GAME_SETTINGS("Settings"),
    PLAY_FIRST("Play first ?"),
    VIEW_SCORES("High scores"),
    NEW_GAME("Play new game"),
    NORMAL_MODE("Normal mode"),
    HARD_MODE("Hard mode"),
    YES("Yes"),
    NO("No"),
    GO_BACK("Back to main menu"),
    QUIT("Quit"),
    PICK_HOUSE("Pick a house for sowing."),
    VIRTUAL_PLAYER_CHOICE("Virtual player picked house %d."),
    IMPOSSIBLE_MOVE("Impossible to play."),
    CAPTURE_NOT_ALLOWED("The capture is forbidden : it will starve the opponent."),
    WIN("You win !"),
    DRAW("Draw : both players have collected the same amount of seeds."),
    LOSE("Virtual player wins !"),
    HUMAN_PLAYER_TURN("It's your turn to play."),
    VIRTUAL_PLAYER_TURN("Virtual player is picking a house."),
    PLAY("Play"),
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

    /**
     * @param params params for formatted strings
     * @return the formatted GameMessage text
     */
    public String getText(Object... params) {
        return String.format(text, params);
    }
}
