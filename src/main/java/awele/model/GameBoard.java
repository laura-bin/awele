package awele.model;

import awele.gamelogic.PlayerType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameBoard {

    /* GAME BOARD VIEW WITH 2 PLAYERS AND 6 HOUSES PER PLAYER

        houses[]                              stock[]
        +-----+-----+-----+-----+-----+-----+ +-----+
        |  5  |  4  |  3  |  2  |  1  |  0  | |  0  |  PLAYER 0 (virtual player)
        +-----+-----+-----+-----+-----+-----+ +-----+
        |  6  |  7  |  8  |  9  | 10  | 11  | |  1  |  PLAYER 1 (human player)
        +-----+-----+-----+-----+-----+-----+ +-----+
    */

    public static final int N_PLAYERS = 2;              // number of players
    public static final int N_HOUSES_PER_PLAYER = 6;    // number of houses belonging to each player

    private final List<Integer> houses; // houses (firsts belongs to player 0 and lasts to player 1)
    private final List<Integer> stocks; // seed stocks (0 belongs to player 0 & 1 belongs to player 1)

    /**
     * Constructor
     */
    public GameBoard() {
        this.houses = new ArrayList<>();
        for (int i = 0; i < N_PLAYERS * N_HOUSES_PER_PLAYER; i++) houses.add(4);

        this.stocks = new ArrayList<>();
        for (int i = 0; i < N_PLAYERS; i++) this.stocks.add(0);
    }

    /**
     * @return a copy of the houses list
     */
    public List<Integer> getHouses() {
        return new ArrayList<>(this.houses);
    }

    /**
     * Get houses sublist for a player
     *
     * @param player player number (0 for virtual player & 1 for human player)
     * @return the sublist of houses belonging to the player
     */
    public List<Integer> getHousesValuesByPlayer(int player) {
        int start = getStartIndexForPlayer(player);
        return Collections.unmodifiableList(houses.subList(start, start + N_HOUSES_PER_PLAYER));
    }

    /**
     * Get houses sublist for a player
     *
     * @param playerType PlayerType enum (VIRTUAL or HUMAN)
     * @return the sublist of houses belonging to the player
     */
    public List<Integer> getHousesValuesByPlayer(PlayerType playerType) {
        return getHousesValuesByPlayer(playerType.ordinal());
    }

    /**
     * Gets a house value by its index
     *
     * @param index index of the house
     * @return value of the house
     */
    public int getHouseValueByIndex(int index) {
        return houses.get(index);
    }

    /**
     * Get the first house index belonging to a player
     *
     * @param player player number corresponding to his position in the players list
     * @return the first index belonging to th player in the houses array
     */
    public int getStartIndexForPlayer(int player) {
        return player * N_HOUSES_PER_PLAYER;
    }

    /**
     * @param player player'snumber
     * @return the stock of seeds of the player
     */
    public int getStockByPlayer(int player) {
        return stocks.get(player);
    }

    /**
     * @param player player'snumber
     * @return the stock of seeds of the player
     */
    public int getStockByPlayer(PlayerType player) {
        return getStockByPlayer(player.ordinal());
    }

    /**
     * Saves new houses values in the houses list
     *
     * @param newHouses newHouses list to save
     */
    public void updateHouses(List<Integer> newHouses) {
        Collections.copy(houses, newHouses);
    }

    /**
     * Add seeds to a player's stock
     *
     * @param player     player number
     * @param seedsToAdd seeds to add to the player's stock
     */
    public void addToStock(int player, int seedsToAdd) {
        stocks.set(player, stocks.get(player) + seedsToAdd);
    }

    public void emptyHouses(int player) {
        int start = getStartIndexForPlayer(player);
        for (int i = 0; i < N_HOUSES_PER_PLAYER; i++) houses.set(start + i, 0);
    }

}
