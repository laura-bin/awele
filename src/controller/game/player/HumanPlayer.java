package controller.game.player;

import controller.console.ConsoleController;
import view.GameErrorMessage;
import view.GameMessage;

import java.util.List;

public class HumanPlayer implements Player {

    @Override
    public int pickHouseForSowing(List<Integer> eligibleHouseNumbers, ConsoleController console) {
        int pickedHouse;
        console.displayMessage(GameMessage.PICK_HOUSE);

        pickedHouse = console.waitNumber();
        while (!eligibleHouseNumbers.contains(pickedHouse)) {
            if (pickedHouse < 0) {
                console.displayMessage(GameErrorMessage.HOUSE_NOT_INT);
                break;
            } else {
                console.displayMessage(GameErrorMessage.INVALID_HOUSE);
            }
            pickedHouse = console.waitNumber();
        }
        return pickedHouse;
    }

}
