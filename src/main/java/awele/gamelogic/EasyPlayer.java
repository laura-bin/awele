package awele.gamelogic;

import java.util.List;
import java.util.Random;

public class EasyPlayer implements VirtualPlayer {

    public int pickHouseForSowing(List<Integer> eligibleHouses) {
        Random r = new Random();
        int pickedHouse = eligibleHouses.get(r.nextInt(eligibleHouses.size()));
        return pickedHouse;
    }

}
