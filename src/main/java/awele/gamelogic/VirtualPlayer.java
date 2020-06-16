package awele.gamelogic;

import java.util.List;

public interface VirtualPlayer {

    /**
     * Pick a house from where to start sowing
     *
     * @param eligibleHouseNumbers valid house numbers (between 1 & the number of houses per player)
     * @return the house number picked by the player
     */
    int pickHouseForSowing(List<Integer> eligibleHouseNumbers);

}
