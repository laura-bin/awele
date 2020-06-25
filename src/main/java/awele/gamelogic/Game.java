package awele.gamelogic;

import awele.model.GameBoard;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Game {

    private final GameBoard board;          // board of 2 rows of 6 houses + 2 stock houses
    private VirtualPlayer virtualPlayer;    // virtual player (easy or hard)
    private int activePlayer;               // active player number : 0 for human player and 1 for virtual player
    private GameStatus status;              // game status in progress / end
    private final LocalDateTime start;      // time at which the game started
    private Duration duration;              // game duration

    /**
     * Constructor
     *
     * @param difficulty  DifficultyType NORMAL or HARD that determines the virtual player added to the game
     * @param humanStarts does the human player wants to start (boolean)
     */
    public Game(DifficultyLevel difficulty, boolean humanStarts) {
        this.start = LocalDateTime.now();
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
     * Constructor
     *
     * @param board GameBoard
     */
    public Game(GameBoard board) {
        this.board = board.copy();
        this.status = GameStatus.IN_PROGRESS;
        this.start = LocalDateTime.now();
    }

    /**
     * @return the GameBoard board associated to the game
     */
    public GameBoard getGameBoard() {
        return board.copy();
    }

    /**
     * @return GameStatus status : in progress or finished and how (win / lose / draw)
     */
    public GameStatus getStatus() {
        return status;
    }

    public int getActivePlayerNumber() {
        return activePlayer;
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
        if (duration == null) return Duration.between(start, LocalDateTime.now());
        else return duration;
    }

    public LocalDateTime getStart() {
        return start;
    }

    /**
     * @return the virtual player
     */
    public VirtualPlayer getVirtualPlayer() {
        return virtualPlayer;
    }

    public int getVirtualPlayerPickedHouse() {
        return virtualPlayer.pickHouseForSowing(this);
    }

    /**
     * @return List of Integer (between 1&6) that can be picked by the active player for sowing
     */
    public List<Integer> getActivePlayerEligibleHouseNumbers() {
        return getEligibleHouseNumbers(activePlayer);

    }

    /**
     * @return List of Integer (between 1&6) that can be picked by the given player for sowing
     */
    public List<Integer> getEligibleHouseNumbers(int player) {
        List<Integer> eligibleHouseNumbers = new ArrayList<>();

        // if all the opponent houses are empty, the player must feed the opponent
        if (isStarved(getOpponent(player), board.getHouses())) {
            for (int houseNumber = 1; houseNumber <= GameBoard.N_HOUSES_PER_PLAYER; houseNumber++) {
                if (getHouseValue(houseNumber, player) > GameBoard.N_HOUSES_PER_PLAYER - houseNumber) {
                    eligibleHouseNumbers.add(houseNumber);
                }
            }
        } else {
            for (int houseNumber = 1; houseNumber <= GameBoard.N_HOUSES_PER_PLAYER; houseNumber++) {
                if (getHouseValue(houseNumber, player) > 0) {
                    eligibleHouseNumbers.add(houseNumber);
                }
            }
        }

        return eligibleHouseNumbers;
    }

    /**
     * Sows seeds
     *
     * @param fromHouseNumber house number picked by the player from which to start sowing
     * @return the last house index updated
     */
    public int sowSeeds(int fromHouseNumber, int player) {
        List<Integer> houses = board.getHouses();

        int startIndex = convertHouseNumberToHouseIndex(fromHouseNumber, player);
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
     * @return the number of seeds captured
     */
    public int captureSeedsFromHouseIndex(int fromHouseIndex, int player) {
        int opponent = getOpponent(player);
        int seedsCaptured = 0;

        // does the last house belong to the opponent ?
        if (isOpposingHouse(fromHouseIndex)) {
            List<Integer> newHouses = board.getHouses();

            // capture seeds max to the first index of the opponent
            while (fromHouseIndex >= board.getStartIndexForPlayer(opponent)) {
                int houseValue = newHouses.get(fromHouseIndex);

                // stop if the house doesn't contain 2 or 3 seeds
                if (houseValue > 3 || houseValue < 2) break;
                else {
                    // capture seeds
                    seedsCaptured += houseValue;
                    newHouses.set(fromHouseIndex, 0);
                }

                fromHouseIndex--;
            }

            // if the opponent isn't starved by the capture, the capture is allowed
            if (!isStarved(opponent, newHouses)) {
                board.updateHouses(newHouses);
                board.addToStock(player, seedsCaptured);

                // check if there is a winner
                if (board.getStockByPlayer(PlayerType.HUMAN) > 24) end(GameStatus.END_WIN);
                if (board.getStockByPlayer(PlayerType.VIRTUAL) > 24) end(GameStatus.END_LOSE);
                if (board.getStockByPlayer(PlayerType.HUMAN) == 24 & board.getStockByPlayer(PlayerType.VIRTUAL) == 24)
                    end(GameStatus.END_DRAW);
            }
        }
        return seedsCaptured;
    }

    /**
     * Capture seeds in the opponent's houses
     *
     * @param lastHouse  last house updated
     * @param lastPlayer player who owns the last house update
     */
    public int captureSeedsFromHouseNumber(int lastHouse, int lastPlayer) {
        return captureSeedsFromHouseIndex(convertHouseNumberToHouseIndex(lastHouse, lastPlayer), activePlayer);
    }

    /**
     * Switches the active player number
     */
    public void switchActivePlayer() {
        activePlayer = getOpponent(activePlayer);
    }

    /**
     * When the opponent is starved and the active player can't feed him,
     * collects the remaining seeds in the houses of the active player
     * and add them to his stock house.
     * At last, sets the end status of the game (sets the winner).
     */
    public int collectRemainingSeeds(int player) {
        List<Integer> houses = board.getHousesValuesByPlayer(player);
        int seedsToAdd = 0;

        // collect remaining seeds
        for (int house : houses) seedsToAdd += house;
        board.addToStock(player, seedsToAdd);
        board.emptyHouses(player);

        // set the winner
        if (board.getStockByPlayer(PlayerType.HUMAN) > board.getStockByPlayer(PlayerType.HUMAN)) {
            end(GameStatus.END_WIN);
        } else if (board.getStockByPlayer(PlayerType.VIRTUAL) > board.getStockByPlayer(PlayerType.VIRTUAL)) {
            end(GameStatus.END_LOSE);
        } else {
            end(GameStatus.END_DRAW);
        }

        return seedsToAdd;
    }

    /**
     * Ends game by setting a GameStatus and stops the game chronometer
     *
     * @param status GameStatus to set
     */
    public void end(GameStatus status) {
        this.status = status;
        this.duration = Duration.between(start, LocalDateTime.now());
    }

    /**
     * Gets the number of the opponent for a given player
     *
     * @return the opponent number (position in the players list)
     */
    private int getOpponent(int player) {
        return player == 0 ? 1 : 0;
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
        int opponent = getOpponent(activePlayer);
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
     * @param player      player number (0 for virtual player & 1 for human)
     * @return index of the house in the list of houses of the board
     */
    private int convertHouseNumberToHouseIndex(int houseNumber, int player) {
        return houseNumber + player * GameBoard.N_HOUSES_PER_PLAYER - 1;
    }
}
