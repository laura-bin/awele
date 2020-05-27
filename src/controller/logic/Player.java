package controller.logic;

import controller.UIController;

import java.util.List;

public interface Player {

    /**
     * Pick a house from where to start sowing
     *
     * @param eligibleHouseNumbers valid house numbers (between 1 & the number of houses per player)
     * @param ui controller to ask to the player to choose a house
     * @return the house number picked by the player
     */
    int pickHouseForSowing(List<Integer> eligibleHouseNumbers, UIController ui);

}
