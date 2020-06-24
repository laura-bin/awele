package awele.gamelogic;

import java.util.List;
import java.util.Random;

public class EasyPlayer implements VirtualPlayer {

    /**
     * @param game from which choose a house number (between 1 & the number of houses per player)
     * @return the chosen number (between 1&6)
     */
    public int pickHouseForSowing(Game game) {
        Random r = new Random();
        List<Integer> eligibleHouses = game.getEligibleHouseNumbers(PlayerType.VIRTUAL.ordinal());
        return eligibleHouses.get(r.nextInt(eligibleHouses.size()));
    }

}
