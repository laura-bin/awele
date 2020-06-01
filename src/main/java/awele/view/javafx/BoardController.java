package awele.view.javafx;

import awele.controller.MenuChoice;
import awele.controller.logic.Game;

public class BoardController {

    private Game currentGame;

    public void newGame(MenuChoice gameDifficulty, boolean wantsToStart) {
        currentGame = new Game(gameDifficulty, wantsToStart);
    }
}
