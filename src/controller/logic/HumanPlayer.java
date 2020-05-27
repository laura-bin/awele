package controller.logic;

import controller.UIController;
import controller.logic.Player;
import view.GameErrorMessage;
import view.GameMessage;

import java.util.List;

public class HumanPlayer implements Player {

    @Override
    public int pickHouseForSowing(List<Integer> eligibleHouseNumbers, UIController ui) {
        int pickedHouse;
        ui.displayMessage(GameMessage.PICK_HOUSE);

        pickedHouse = ui.waitNumber();
        while (!eligibleHouseNumbers.contains(pickedHouse)) {
            if (pickedHouse < 0) {
                ui.displayMessage(GameErrorMessage.HOUSE_NOT_INT);
            } else {
                ui.displayMessage(GameErrorMessage.INVALID_HOUSE);
            }
            pickedHouse = ui.waitNumber();
        }
        return pickedHouse;
    }

}
