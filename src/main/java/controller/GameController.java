package controller;

import controller.logic.Game;
import controller.logic.GameStatus;
import view.GameMessage;

import java.util.List;

import static controller.MenuChoice.NORMAL_MODE;
import static controller.MenuChoice.YES;

/**
 * Management of the game navigation (menu displays and game's actions called)
 * and player's inputs
 */
public class GameController {

    private final UIController ui;

    /**
     * Constructor
     *
     * @param ui user interface managing the displays and player's inputs
     */
    public GameController(UIController ui) {
        this.ui = ui;
    }

    /**
     * Starts the application and manages the navigation
     */
    public void start() {
        ui.displayMessage(GameMessage.WELCOME);

        ui.menu(Menu.MAIN, selection -> {
            switch (selection) {
                case PLAY:
                    ui.menu(Menu.DIFFICULTY, difficulty -> {
                        switch (difficulty) {
                            case NORMAL_MODE:
                                startGame(difficulty);
                                break;
                            case HARD_MODE:
                                ui.menu(Menu.NOT_YET_IMPLEMENTED, weDontCare -> {
                                });
                                break;
                        }
                    });
                    break;
                case SCORES:
                    ui.menu(Menu.NOT_YET_IMPLEMENTED, weDontCare -> {
                        start();
                    });
                    break;
                case BACK:
                case QUIT:
                    break;
            }
        });
    }

    private void startGame(MenuChoice difficulty) {
        ui.menu(Menu.START_GAME, selectionToStart -> {
            Game game = new Game(difficulty, selectionToStart == YES);

            turn(game);
        });
    }

    private void turn(Game game) {
        ui.displayBoard(game.getGameBoard());

        List<Integer> eligibleHouses = game.getActivePlayerEligibleHouseNumbers();
        if (eligibleHouses.isEmpty()) {
            ui.displayMessage(GameMessage.IMPOSSIBLE_MOVE);
            game.collectRemainingSeeds();
        } else {
            game.getActivePlayer().pickHouseForSowing(eligibleHouses, ui, pickedHouseNumber -> {
                game.sowSeeds(pickedHouseNumber);

                if (game.getStatus() == GameStatus.IN_PROGRESS) {
                    turn(game);
                } else {
                    ui.displayMessage(game.getStatus().getMessage());
                }
            });
        }
    }
}
