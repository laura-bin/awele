package controller.game;

public enum GameStatus {
    IN_PROGRESS(""),
    END_WIN("You win !"),
    END_DRAW("Draw : both players have collected the same amount of seeds."),
    END_LOSE("Virtual player wins !");

    /**
     * Message to display in the user interface
     */
    private final String text;

    /**
     * Private constructor
     *
     * @param text
     */
    GameStatus(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
