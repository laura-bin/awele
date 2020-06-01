package controller.logic;

import controller.UIController;
import view.GameErrorMessage;
import view.GameMessage;

import java.util.List;
import java.util.function.IntConsumer;

public class HumanPlayer implements Player {

    @Override
    public void pickHouseForSowing(List<Integer> eligibleHouseNumbers, UIController ui, IntConsumer callback) {
        ui.displayMessage(GameMessage.PICK_HOUSE);

        ui.waitNumber(eligibleHouseNumbers, userSelection -> handleHousePicked(eligibleHouseNumbers, ui, callback, userSelection));
    }

    public void handleHousePicked(List<Integer> eligibleHouseNumbers, UIController ui, IntConsumer callback, int pickedHouse) {
        if (eligibleHouseNumbers.contains(pickedHouse)) {
            callback.accept(pickedHouse);
        }
        if (pickedHouse < 0) {
            ui.displayMessage(GameErrorMessage.HOUSE_NOT_INT);
        } else {
            ui.displayMessage(GameErrorMessage.INVALID_HOUSE);
        }
        ui.waitNumber(eligibleHouseNumbers, userSelection -> handleHousePicked(eligibleHouseNumbers, ui, callback, userSelection));
    }

}
