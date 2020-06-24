package awele.gamelogic;

import awele.ui.GameMessage;

public enum GameStatus {
    IN_PROGRESS(GameMessage.EMPTY),
    END_WIN(GameMessage.WIN),
    END_DRAW(GameMessage.DRAW),
    END_LOSE(GameMessage.LOSE);

    /**
     * Message to display in the user interface
     */
    private final GameMessage message;

    /**
     * Private constructor
     *
     * @param message GameMessage
     */
    GameStatus(GameMessage message) {
        this.message = message;
    }

    public GameMessage getMessage() {
        return message;
    }

}
