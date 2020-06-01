package awele.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameBoard {
    /* GAME BOARD VIEW WITH 2 PLAYERS AND 6 HOUSES PER PLAYER

            houses[]                  stock[]
        +-----+-----+-----+-----+ +-----+
        |  7  |  6  |  5  |  4  | |  1  |  PLAYER 1 (virtual player)
        +-----+-----+-----+-----+ +-----+
        |  0  |  1  |  2  |  3  | |  0  |  PLAYER 0 (human player)
        +-----+-----+-----+-----+ +-----+
     */

    public static final int N_PLAYERS = 2;              // number of players
    public static final int N_HOUSES_PER_PLAYER = 6;    // number of houses belonging to each player

    private final List<Integer> houses;                       // houses (firsts belongs to player 0 and lasts to player 1)
    private final List<Integer> stocks;                       // seed stocks (0 belongs to player 0 & 1 belongs to player 1)

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

    public List<Integer> getHousesValues(int player) {
        int start = getStartIndex(player);
        return Collections.unmodifiableList(houses.subList(start, start + N_HOUSES_PER_PLAYER));
    }

    /**
     * Gets a house value by its index
     *
     * @param index index of the house
     * @return value of the house
     */
    public int getHouseValue(int index) {
        return houses.get(index);
    }

    /**
     * Gets the indexes of houses that belongs to a player
     *
     * @param player number of the player (0 or 1)
     * @return a list of the player's house indexes
     */
    public List<Integer> getHouseIndexes(int player) {
        List<Integer> indexes = new ArrayList<>();
        int start = getStartIndex(player);
        for (int i = start; i < start + N_HOUSES_PER_PLAYER; i++) indexes.add(i);
        return indexes;
    }

    /**
     * Get the first house index belonging to a player
     *
     * @param player player number corresponding to his position in the players list
     * @return the first index belonging to th player in the houses array
     */
    public int getStartIndex(int player) {
        return player * N_HOUSES_PER_PLAYER;
    }

    /**
     * @param player player'snumber
     * @return the stock of seeds of the player
     */
    public int getStock(int player) {
        return stocks.get(player);
    }

    public List<Integer> getStocks() {
        return stocks;
    }

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
        int start = getStartIndex(player);
        for (int i = 0; i < N_HOUSES_PER_PLAYER; i++) houses.set(start + i, 0);
    }

}
