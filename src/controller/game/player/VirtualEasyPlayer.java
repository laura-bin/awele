package controller.game.player;

import controller.console.ConsoleController;

import java.util.List;
import java.util.Random;

public class VirtualEasyPlayer implements Player {

    @Override
    public int pickHouseForSowing(List<Integer> eligibleHouses, ConsoleController console) {
        Random r = new Random();
        int pickedHouse = eligibleHouses.get(r.nextInt(eligibleHouses.size()));
        console.displayVirtualPlayerPlaying();
        console.displayMessage(String.format("Virtual player picked house %d.", pickedHouse));
        return pickedHouse;
    }

}
