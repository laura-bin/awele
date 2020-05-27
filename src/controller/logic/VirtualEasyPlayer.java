package controller.logic;

import controller.UIController;
import controller.logic.Player;
import view.GameMessage;

import java.util.List;
import java.util.Random;

public class VirtualEasyPlayer implements Player {

    @Override
    public int pickHouseForSowing(List<Integer> eligibleHouses, UIController ui) {
        Random r = new Random();
        int pickedHouse = eligibleHouses.get(r.nextInt(eligibleHouses.size()));
        ui.displayVirtualPlayerPlayingAnimation();
        ui.displayMessage(GameMessage.VIRTUAL_PLAYER_CHOICE, pickedHouse);
        return pickedHouse;
    }

}
