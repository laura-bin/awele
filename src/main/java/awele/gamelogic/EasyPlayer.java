package awele.gamelogic;

import java.util.List;
import java.util.Random;

public class EasyPlayer implements VirtualPlayer {

    public int pickHouseForSowing(Game game) {
        Random r = new Random();
        List<Integer> eligibleHouses = game.getEligibleHouseNumbers(PlayerType.VIRTUAL.ordinal());
        int pickedHouse = eligibleHouses.get(r.nextInt(eligibleHouses.size()));
        return pickedHouse;
    }

}
