package controller.logic;

import controller.UIController;
import view.GameMessage;

import java.util.List;
import java.util.Random;
import java.util.function.IntConsumer;

public class VirtualEasyPlayer implements Player {

    private static final Random RANDOM = new Random();

    @Override
    public void pickHouseForSowing(List<Integer> eligibleHouses, UIController ui, IntConsumer callback) {
        int pickedHouse = eligibleHouses.get(RANDOM.nextInt(eligibleHouses.size()));
        ui.displayVirtualPlayerPlayingAnimation(() -> {
            ui.displayMessage(GameMessage.VIRTUAL_PLAYER_CHOICE, pickedHouse);
            callback.accept(pickedHouse);
        });
    }

}
