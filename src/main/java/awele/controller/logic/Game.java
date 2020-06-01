package awele.controller.logic;

import awele.controller.MenuChoice;
import awele.model.GameBoard;

import java.util.ArrayList;
import java.util.List;

public class Game {

    private final GameBoard board;        // board of 2 rows of 6 houses + 2 stock houses
    private final List<Player> players;   // players controllers (number pickers)
    private int activePlayer;       // active player number : 0 for human player and 1 for virtual player
    private GameStatus status;      // game status in progress / end

    /**
     * Constructor
     *
     * @param humanStarts does the human player wants to start (boolean)
     */
    public Game(MenuChoice gameDifficulty, boolean humanStarts) {
        this.board = new GameBoard();

        this.players = new ArrayList<>();
        players.add(new HumanPlayer());
        switch (gameDifficulty) {
            case HARD_MODE:
                // TODO
                break;
            case NORMAL_MODE:
                players.add(new VirtualEasyPlayer());
                break;
        }

        this.activePlayer = humanStarts ? 0 : 1;

        this.status = GameStatus.IN_PROGRESS;
    }

    /**
     * @return the GameBoard board associated to the game
     */
    public GameBoard getGameBoard() {
        return this.board;
    }

    /**
     * Get the game status : in progress or finished and how (win / lose / draw)
     *
     * @return a GameStatus
     */
    public GameStatus getStatus() {
        return status;
    }

    /**
     * Get the active player
     *
     * @return Player that is active
     */
    public Player getActivePlayer() {
        return this.players.get(activePlayer);
    }

    /**
     * Get the house numbers (between 1&6) that are a valid choice for the player
     *
     * @return List of Integer (between 1&6) that can be picked by the player for sowing
     */
    public List<Integer> getActivePlayerEligibleHouseNumbers() {
        List<Integer> houseIndexes = board.getHouseIndexes(activePlayer);
        List<Integer> eligibleHouseNumbers = new ArrayList<>();

        // if all the opponent houses are empty, the player must feed the opponent
        if (isStarved(getOpponentNumber())) {
            for (Integer index : houseIndexes) {
                if (board.getHouseValue(index) >= GameBoard.N_HOUSES_PER_PLAYER - index) {
                    eligibleHouseNumbers.add(convertHouseIndexToHouseNumber(index));
                }
            }
        } else {
            for (Integer index : houseIndexes) {
                if (board.getHouseValue(index) > 0) {
                    eligibleHouseNumbers.add(convertHouseIndexToHouseNumber(index));
                }
            }
        }
        return eligibleHouseNumbers;
    }

    /**
     * Sows seeds and capture opponent seeds if possible
     *
     * @param fromHouseNumber house number picked by the player from which to start sowing
     */
    public void sowSeeds(int fromHouseNumber) {
        List<Integer> houses = board.getHouses();
        int startIndex = convertPickedHouseToHouseIndex(fromHouseNumber);
        int nSowing = houses.get(startIndex);
        int houseToUpdateIndex = startIndex;
        int opponent = getOpponentNumber();

        houses.set(houseToUpdateIndex, 0);

        for (int i = 0; i < nSowing; i++) {
            if (houseToUpdateIndex == startIndex) houseToUpdateIndex = getNextHouseIndex(houseToUpdateIndex);
            houses.set(houseToUpdateIndex, houses.get(houseToUpdateIndex) + 1);
            houseToUpdateIndex = getNextHouseIndex(houseToUpdateIndex);
        }

        board.updateHouses(houses);


        houseToUpdateIndex = houseToUpdateIndex == 0 ? GameBoard.N_HOUSES_PER_PLAYER * GameBoard.N_PLAYERS - 1 : houseToUpdateIndex - 1;

        // does the last house belong to the opponent ?
        if (board.getHouseIndexes(opponent).contains(houseToUpdateIndex)) {
            captureSeeds(houseToUpdateIndex, opponent);
        }

        switchActivePlayer();
    }

    /**
     * Capture seeds in the opponent's houses
     * @param fromHouseIndex last house index where a seed was sowed
     * @param opponent opponent number
     */
    private void captureSeeds(int fromHouseIndex, int opponent) {
        List<Integer> newHouses = board.getHouses();
        int seedsToAdd = 0;

        while (fromHouseIndex >= board.getStartIndex(opponent)) {
            int houseValue = newHouses.get(fromHouseIndex);

            // stop if the house doesn't contain 2 or 3 seeds
            if (houseValue > 3 || houseValue < 2) break;
            else {
                seedsToAdd += houseValue;
                newHouses.set(fromHouseIndex, 0);
            }

            fromHouseIndex--;
        }

        // if the opponent isn't starved by the capture, the capture is allowed
        if (!isStarved(opponent, newHouses)) {
            board.updateHouses(newHouses);
            board.addToStock(activePlayer, seedsToAdd);

            // check if there is a winner
            if (board.getStock(0) > 24) status = GameStatus.END_WIN;
            if (board.getStock(1) > 24) status = GameStatus.END_LOSE;
            if (board.getStock(0) == 24 & board.getStock(1) == 24) status = GameStatus.END_DRAW;
        }
    }

    /**
     * Switches the active player number
     */
    private void switchActivePlayer() {
        activePlayer = getOpponentNumber();
    }

    // TODO 2 types of end when it's impossible to play
    public void collectRemainingSeeds() {
        List<Integer> houses = board.getHousesValues(activePlayer);
        int seedsToAdd = 0;

        // collect remaining seeds
        for (int house : houses) seedsToAdd += house;
        board.addToStock(activePlayer, seedsToAdd);
        board.emptyHouses(activePlayer);

        // set the winner
        if (board.getStock(0) > board.getStock(1)) {
            status = GameStatus.END_WIN;
        } else if (board.getStock(1) > board.getStock(0)) {
            status = GameStatus.END_LOSE;
        } else {
            status = GameStatus.END_DRAW;
        }
    }

    /**
     * Get the number of the opponent (the player that is not active)
     *
     * @return the opponent number (position in the players list)
     */
    private int getOpponentNumber() {
        return activePlayer == 0 ? 1 : 0;
    }

    /**
     * Determines if all the opponent houses are empty
     *
     * @return boolean true if all the opponent houses are empty
     */
    private boolean isStarved(int player) {
        List<Integer> houses = board.getHouses();
        return isStarved(player, houses);
    }

    /**
     * Determines if all the opponent houses are empty
     *
     * @return boolean true if all the opponent houses are empty
     */
    private boolean isStarved(int player, List<Integer> houses) {
        int start = board.getStartIndex(player);
        for (int i = 0; i < GameBoard.N_HOUSES_PER_PLAYER; i++) {
            if (houses.get(start + i) > 0) return false;
        }
        return true;
    }

    /**
     * @param index
     * @return return the next index int the houses array
     */
    private int getNextHouseIndex(int index) {
        if (index < GameBoard.N_PLAYERS * GameBoard.N_HOUSES_PER_PLAYER - 1) return index + 1;
        else return 0;
    }

    /**
     * +-----+-----+-----+-----+       +-----+-----+-----+-----+
     * |  3  |  2  |  1  |  0  |       |  4  |  3  |  2  |  1  |
     * +-----+-----+-----+-----+  ==>  +-----+-----+-----+-----+
     * |  4  |  5  |  6  |  7  |       |  1  |  2  |  3  |  4  |
     * +-----+-----+-----+-----+       +-----+-----+-----+-----+
     *
     * @param houseIndex index of the house in the board array houses
     * @return house number that can be picked by a player between 1 and nHouses
     */
    private int convertHouseIndexToHouseNumber(int houseIndex) {
        return houseIndex - GameBoard.N_HOUSES_PER_PLAYER * activePlayer + 1;
    }

    /**
     * +-----+-----+-----+-----+       +-----+-----+-----+-----+
     * |  4  |  3  |  2  |  1  |       |  3  |  2  |  1  |  0  |
     * +-----+-----+-----+-----+  ==>  +-----+-----+-----+-----+
     * |  1  |  2  |  3  |  4  |       |  4  |  5  |  6  |  7  |
     * +-----+-----+-----+-----+       +-----+-----+-----+-----+
     *
     * @param pickedHouseNumber house number picked by a player between 1 and nHouses
     * @return index of the house in the board array houses
     */
    private int convertPickedHouseToHouseIndex(int pickedHouseNumber) {
        return pickedHouseNumber + GameBoard.N_HOUSES_PER_PLAYER * activePlayer - 1;
    }
}
