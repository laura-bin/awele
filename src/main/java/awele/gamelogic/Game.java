package awele.gamelogic;

import awele.model.GameBoard;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class Game {

    private final GameBoard board;          // board of 2 rows of 6 houses + 2 stock houses
    private VirtualPlayer virtualPlayer;    // virtual player (easy or hard)
    private int activePlayer;               // active player number : 0 for human player and 1 for virtual player
    private GameStatus status;              // game status in progress / end
    private final long start;               // time at which the game started
    private Duration duration;              // game duration

    /**
     * Constructor
     *
     * @param difficulty DifficultyType NORMAL or HARD that determines the virtual player added to the game
     * @param humanStarts does the human player wants to start (boolean)
     */
    public Game(DifficultyLevel difficulty, boolean humanStarts) {
        this.start = System.nanoTime();
        this.board = new GameBoard();

        switch (difficulty) {
            case HARD:
                this.virtualPlayer = new HardPlayer();
                break;
            case NORMAL:
                this.virtualPlayer = new EasyPlayer();
                break;
        }

        this.activePlayer = humanStarts ? PlayerType.HUMAN.ordinal() : PlayerType.VIRTUAL.ordinal();

        this.status = GameStatus.IN_PROGRESS;
    }

    /**
     * @return the GameBoard board associated to the game
     */
    public GameBoard getGameBoard() {
        return this.board;
    }

    /**
     * @return GameStatus status : in progress or finished and how (win / lose / draw)
     */
    public GameStatus getStatus() {
        return status;
    }

    public int getActivePlayerNumber() {
        return  activePlayer;
    }

    /**
     * @return true if the active player is human
     */
    public boolean activePlayerIsHuman() {
        return activePlayer == PlayerType.HUMAN.ordinal();
    }

    /**
     * @return the game duration
     */
    public Duration getDuration() {
        if (duration == null) return Duration.ofNanos(System.nanoTime() - start);
        else return duration;
    }

    public int getVirtualPlayerPickedHouse() {
        return virtualPlayer.pickHouseForSowing(getActivePlayerEligibleHouseNumbers());
    }

    /**
     * @return List of Integer (between 1&6) that can be picked by the player for sowing
     */
    public List<Integer> getActivePlayerEligibleHouseNumbers() {
        List<Integer> eligibleHouseNumbers = new ArrayList<>();

        // if all the opponent houses are empty, the player must feed the opponent
        if (isStarved(getOpponentNumber(), board.getHouses())) {
            for (int houseNumber = 1; houseNumber <= GameBoard.N_HOUSES_PER_PLAYER; houseNumber++) {
                if (getHouseValue(houseNumber, activePlayer) > GameBoard.N_HOUSES_PER_PLAYER - houseNumber) {
                    eligibleHouseNumbers.add(houseNumber);
                }
            }
        } else {
            for (int houseNumber = 1; houseNumber <= GameBoard.N_HOUSES_PER_PLAYER; houseNumber++) {
                if (getHouseValue(houseNumber, activePlayer) > 0) {
                    eligibleHouseNumbers.add(houseNumber);
                }
            }
        }
        return eligibleHouseNumbers;
    }

    /**
     * Sows seeds and capture opponent seeds if possible
     *
     * @param fromHouseNumber house number picked by the player from which to start sowing
     * @return the last house index updated
     */
    public int sowSeeds(int fromHouseNumber) {
        List<Integer> houses = board.getHouses();
        int startIndex = convertHouseNumberToHouseIndex(fromHouseNumber, activePlayer);
        int nSowing = houses.get(startIndex);
        int houseToUpdateIndex = startIndex;

        houses.set(houseToUpdateIndex, 0);

        for (int i = 0; i < nSowing; i++) {
            if (houseToUpdateIndex == startIndex) houseToUpdateIndex = getNextHouseIndex(houseToUpdateIndex);
            houses.set(houseToUpdateIndex, houses.get(houseToUpdateIndex) + 1);
            houseToUpdateIndex = getNextHouseIndex(houseToUpdateIndex);
        }

        board.updateHouses(houses);

        return getPreviousIndex(houseToUpdateIndex);
    }

    /**
     * Capture seeds in the opponent's houses
     *
     * @param fromHouseIndex last house index where a seed was sowed
     * @return true if seeds if seeds could be captured
     */
    public boolean captureSeeds(int fromHouseIndex) {
        int opponent = getOpponentNumber();

        // does the last house belong to the opponent ?
        if (isOpposingHouse(fromHouseIndex)) {
            List<Integer> newHouses = board.getHouses();
            int seedsToAdd = 0;

            // capture seeds max to the first index of the opponent
            while (fromHouseIndex >= board.getStartIndexForPlayer(opponent)) {
                int houseValue = newHouses.get(fromHouseIndex);

                // stop if the house doesn't contain 2 or 3 seeds
                if (houseValue > 3 || houseValue < 2) break;
                else {
                    // capture seeds
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
                if (board.getStockByPlayer(PlayerType.HUMAN) > 24) end(GameStatus.END_WIN);
                if (board.getStockByPlayer(PlayerType.VIRTUAL) > 24) end(GameStatus.END_LOSE);
                if (board.getStockByPlayer(PlayerType.HUMAN) == 24 & board.getStockByPlayer(PlayerType.VIRTUAL) == 24)
                    end(GameStatus.END_DRAW);
                return true;
            }
        }
        return false;
    }

    /**
     * Capture seeds in the opponent's houses
     *
     * @param lastHouse last house updated
     * @param lastPlayer player who owns the last house update
     */
    public boolean captureSeeds(int lastHouse, int lastPlayer) {
        return captureSeeds(convertHouseNumberToHouseIndex(lastHouse, lastPlayer));
    }

    /**
     * Switches the active player number
     */
    public void switchActivePlayer() {
        activePlayer = getOpponentNumber();
    }

    /**
     * When the opponent is starved and the active player can't feed him,
     * collects the remaining seeds in the houses of the active player
     * and add them to his stock house.
     * At last, sets the end status of the game (sets the winner).
     */
    public void collectRemainingSeeds() {
        List<Integer> houses = board.getHousesValuesByPlayer(activePlayer);
        int seedsToAdd = 0;

        // collect remaining seeds
        for (int house : houses) seedsToAdd += house;
        board.addToStock(activePlayer, seedsToAdd);
        board.emptyHouses(activePlayer);

        // set the winner
        if (board.getStockByPlayer(PlayerType.HUMAN) > board.getStockByPlayer(PlayerType.HUMAN)) {
            end(GameStatus.END_WIN);
        } else if (board.getStockByPlayer(PlayerType.VIRTUAL) > board.getStockByPlayer(PlayerType.VIRTUAL)) {
            end(GameStatus.END_LOSE);
        } else {
            end(GameStatus.END_DRAW);
        }
    }

    /**
     * Ends game by setting a GameStatus and stops the game chronometer
     * @param status GameStatus to set
     */
    public void end(GameStatus status) {
        this.status = status;
        this.duration = Duration.ofNanos(System.nanoTime() - start);
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
     * Determines if the houses of a player are all empty
     *
     * @param player player number
     * @param houses complete list of the houses
     * @return boolean true if all the opponent houses are empty
     */
    private boolean isStarved(int player, List<Integer> houses) {
        int start = board.getStartIndexForPlayer(player);
        for (int i = 0; i < GameBoard.N_HOUSES_PER_PLAYER; i++) {
            if (houses.get(start + i) > 0) return false;
        }
        return true;
    }

    /**
     * Does the house index belong to the player's opponent ?
     *
     * @param houseIndex house index
     * @return true if the house index belongs to the player's opponent
     */
    private boolean isOpposingHouse(int houseIndex) {
        int opponent = getOpponentNumber();
        for (int i = 0; i < GameBoard.N_HOUSES_PER_PLAYER; i++) {
            if (houseIndex == opponent * GameBoard.N_HOUSES_PER_PLAYER + i) return true;
        }
        return false;
    }

    /**
     * Gets the previous house index (loop iteration)
     *
     * @param index house index
     * @return the previous house index in list of houses of the board
     */
    private int getPreviousIndex(int index) {
        return index == 0 ? GameBoard.N_HOUSES_PER_PLAYER * GameBoard.N_PLAYERS - 1 : index - 1;
    }

    /**
     * Gets the next house index (loop iteration)
     *
     * @param index
     * @return return the next index in list of houses of the board
     */
    private int getNextHouseIndex(int index) {
        if (index < GameBoard.N_PLAYERS * GameBoard.N_HOUSES_PER_PLAYER - 1) return index + 1;
        else return 0;
    }

    /**
     * Gets house value by player number and house number
     */
    private int getHouseValue(int house, int player) {
        return board.getHouseValueByIndex(convertHouseNumberToHouseIndex(house, player));
    }

    /**
     * +-----+-----+-----+-----+-----+-----+       +-----+-----+-----+-----+-----+-----+
     * |  6  |  5  |  4  |  3  |  2  |  1  |       |  5  |  4  |  3  |  2  |  1  |  0  |
     * +-----+-----+-----+-----+-----+-----+  ==>  +-----+-----+-----+-----+-----+-----+
     * |  1  |  2  |  3  |  4  |  5  |  6  |       |  6  |  7  |  8  |  9  | 10  | 11  |
     * +-----+-----+-----+-----+-----+-----+       +-----+-----+-----+-----+-----+-----+
     *
     * @param houseNumber house number to convert (between 1 & GameBoard.N_HOUSES_PER_PLAYER
     * @param player player number (0 for virtual player & 1 for human)
     * @return index of the house in the list of houses of the board
     */
    private int convertHouseNumberToHouseIndex(int houseNumber, int player) {
        return houseNumber + player * GameBoard.N_HOUSES_PER_PLAYER - 1;
    }

}
