package awele.javafx.controller;

import awele.gamelogic.*;
import awele.model.GameBoard;
import awele.ui.GameMessage;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/* Grid Pane indexes

    houses[]                              stock[]
    +-----+-----+-----+-----+-----+-----+ +-----+
    | 0.1 | 0.2 | 0.3 | 0.4 | 0.5 | 0.6 | | 0.7 |  PLAYER 0 (virtual player)
    +-----+-----+-----+-----+-----+-----+ +-----+
    | 1.1 | 1.2 | 1.3 | 1.4 | 1.5 | 1.6 | | 1.7 |  PLAYER 1 (human player)
    +-----+-----+-----+-----+-----+-----+ +-----+
*/
public class GameBoardPane implements Initializable  {

    private static final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
    private static final long ANIMATION_DELAY = 1000; // time delay for the game animations in milliseconds

    @FXML
    private Label duration;
    @FXML
    private Label virtualPlayerTurnMarker;
    @FXML
    private Label humanPlayerTurnMarker;
    @FXML
    private GridPane gridBoard;
    @FXML
    private Label playerMessage;

    private RootStack root;

    private Game game;

    private final Deque<ScheduledFuture<?>> animations = new ArrayDeque<>(); // animations in progress

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // set onAction method on each human player's house
        for (Node node : gridBoard.getChildren()) {
            Integer columnIndex = GridPane.getColumnIndex(node);
            if (GridPane.getRowIndex(node) == PlayerType.HUMAN.ordinal()
                    && columnIndex > 0 && columnIndex <= awele.model.GameBoard.N_HOUSES_PER_PLAYER) {
                ((Button) node).setOnAction(e -> sowSeeds(columnIndex));
            }
        }

        // update game duration every second
        executor.scheduleAtFixedRate(() -> {
            // insert the task in the javafx thread's task queue
            Platform.runLater(() -> {
                if (game == null) return;

                Duration currentTimeDuration = game.getDuration();
                duration.setText(String.format("%02d:%02d:%02d", currentTimeDuration.toHours(),
                    currentTimeDuration.toMinutesPart(), currentTimeDuration.toSecondsPart()));
            });
        }, 0, 1, TimeUnit.SECONDS);
    }

    /**
     * Sets the root stack that manages the navigation in the application
     * @param root root stack
     */
    public void setRoot(RootStack root) {
        this.root = root;
    }

    /**
     * Starts the game : sets the game, displays the board and launches the first turn
     * @param game game that is played
     */
    public void startGame(Game game) {
        this.game = game;
        turn();
    }

    /**
     * Goes back to main menu
     * and resets the visual state of the game (animations and styles)
     */
    public void goBack() {
        // cancel pending animations
        for (ScheduledFuture<?> animation : animations) {
            animation.cancel(false);
        }
        animations.clear();

        removeHousesStyles();

        this.root.setMainMenuVisible();
    }

    /**
     * Quits the application
     */
    public void quit() {
        this.root.quit();
    }

    /**
     * Game turn :
     * - checks if a move is possible
     * - gets the active player picked house number
     * - sows seeds from this house
     * - captures seeds (if possible)
     * - switches active player
     */
    private void turn() {
        List<Integer> eligibleHouses = game.getActivePlayerEligibleHouseNumbers();

        removeHousesStyles();
        displayBoard(game.getGameBoard());

        // if no move is possible, collect remaining seeds and end game
        if (eligibleHouses.isEmpty()) {
            humanPlayerTurnMarker.setText("");
            virtualPlayerTurnMarker.setText("");
            Utils.setText(playerMessage, GameMessage.IMPOSSIBLE_MOVE);
            collectRemainingSeeds();
        } else {
            if (game.activePlayerIsHuman()) {
                humanPlayerTurnMarker.setText(">");
                virtualPlayerTurnMarker.setText("");
                playerMessage.setText(GameMessage.HUMAN_PLAYER_TURN.getText());
                setDisableHumanPlayerHouses(eligibleHouses);
            } else {
                humanPlayerTurnMarker.setText("");
                virtualPlayerTurnMarker.setText(">");
                playerMessage.setText(GameMessage.VIRTUAL_PLAYER_TURN.getText());
                setDisableHumanPlayerHouses();

                runLater(() -> sowSeeds(game.getVirtualPlayerPickedHouse()));
            }
        }
    }

    /**
     * Sows seeds when a player picks a house
     *
     * @param pickedHouseNumber house picked number
     */
    public void sowSeeds(int pickedHouseNumber) {
        Labeled houseNode = getHouseNode(pickedHouseNumber, game.getActivePlayerNumber());
        int nSowing = Integer.parseInt(getActivePlayerNodeByHouseNumber(pickedHouseNumber).getText());
        int player = game.getActivePlayerNumber();

        setDisableHumanPlayerHouses();
        houseNode.getStyleClass().add("picked");
        game.sowSeeds(pickedHouseNumber);
        sowSeeds(pickedHouseNumber, player, pickedHouseNumber, player, nSowing);
    }

    /**
     * Sows seed animation in the next house
     * At last launches the animation to capture seeds
     *
     * @param firstHouse first house from which the sowing starts (kept to avoid sowing in the original house)
     * @param firstPlayer first player from which the sowing starts (kept to avoid sowing in the original house)
     * @param previousHouse house in which the last seed has been planted
     * @param previousPlayer player who has planted the last seed
     * @param nSowing number of sowing remaining
     */
    private void sowSeeds(int firstHouse, int firstPlayer, int previousHouse, int previousPlayer, int nSowing) {
        runLater(() -> {
            if (nSowing == 0) {
                captureSeeds(previousHouse, previousPlayer);
            } else {
                Labeled houseNode;
                int nextHouse;
                int nextPlayer;
                int nextNSowing = nSowing - 1;

                if (previousHouse < GameBoard.N_HOUSES_PER_PLAYER) {
                    nextHouse = previousHouse + 1;
                    nextPlayer = previousPlayer;
                } else {
                    nextHouse = 1;
                    nextPlayer = (previousPlayer + 1) % 2;
                }

                // skip the starting house
                if (nextHouse == firstHouse && nextPlayer == firstPlayer) {
                    if (nextHouse < GameBoard.N_HOUSES_PER_PLAYER) {
                        nextHouse++;
                    } else {
                        nextHouse = 1;
                        nextPlayer = (previousPlayer + 1) % 2;
                    }
                }

                houseNode = getHouseNode(nextHouse, nextPlayer);
                houseNode.getStyleClass().add("brightened");
                houseNode.setText(String.valueOf(Integer.parseInt(houseNode.getText()) + 1));
                sowSeeds(firstHouse, firstPlayer, nextHouse, nextPlayer, nextNSowing);
            }
        });
    }

    /**
     * Animates the capture of the seeds
     *
     * @param lastHouseUpdated last house updated number
     * @param lastPlayer player who owns the last house updated
     */
    private void captureSeeds(int lastHouseUpdated, int lastPlayer) {
        // if the last house updated belongs to the opponent, try to capture seeds
        if (lastPlayer != game.getActivePlayerNumber()) {
            if(game.captureSeeds(lastHouseUpdated, lastPlayer)) {
                Labeled houseNode = getHouseNode(lastHouseUpdated, lastPlayer);
                Labeled stockHouse = getHouseNode(GameBoard.N_HOUSES_PER_PLAYER + 1, game.getActivePlayerNumber());
                // TODO set style to house node and stock node
            }
        }
        if (game.getStatus() == GameStatus.IN_PROGRESS) {
            game.switchActivePlayer();
            turn();
        } else {
            Utils.setText(playerMessage, game.getStatus().getMessage());
        }
    }





    /**
     * Remaining seeds collect animation
     */
    private void collectRemainingSeeds() {
        Labeled activePlayerStockNode = getHouseNode(GameBoard.N_HOUSES_PER_PLAYER + 1, game.getActivePlayerNumber());

        // deactivate all human player houses
        setDisableHumanPlayerHouses();

        // collect seeds, from the last active player house, and add them to the stock house of the active player
        collectSeeds(GameBoard.N_HOUSES_PER_PLAYER, activePlayerStockNode, 0, 48);

        // update the game board
        game.collectRemainingSeeds();
    }

    /**
     * Animation that collects a seed in a house and updates the stock
     * @param houseNumber
     */
    private void collectSeed(int houseNumber, Labeled stock) {
        for (int i = houseNumber; i <= GameBoard.N_HOUSES_PER_PLAYER; i++) {
            Labeled house = getActivePlayerNodeByHouseNumber(houseNumber);
            int seedsInHouse = Integer.parseInt(house.getText());
            if (seedsInHouse > 0) {
                house.setText("0");
                int nextHouseNumber = i + 1;
                executor.schedule(() -> {
                    Platform.runLater(() -> {
                        stock.setText(String.valueOf(Integer.parseInt(stock.getText()) + seedsInHouse));
                        executor.schedule(() -> {
                            collectSeed(nextHouseNumber, stock);
                        }, 200, TimeUnit.MILLISECONDS);
                    });
                }, 200, TimeUnit.MILLISECONDS);
                break;
            }
        }

    }

    /**
     * Capturing seeds animation
     *
     * @param fromHouseNumber house number from which starting the capture
     * @param stock Labeled stock in which the seeds must be added
     * @param minSeeds minimum seeds in the house to allow capture
     * @param maxSeeds maximum seeds in the house to allow the capture
     */
    private void collectSeeds(int fromHouseNumber, Labeled stock, int minSeeds, int maxSeeds) {
        Labeled houseNode = getHouseNode(fromHouseNumber, game.getActivePlayerNumber());
        int seedsInHouse = Integer.parseInt(houseNode.getText());

        // the loop is used to skip animation on empty houses when the method is called by collectRemainingSeeds()
        while (fromHouseNumber > 0 && seedsInHouse >= minSeeds && seedsInHouse <= maxSeeds) {
            int previousHouseNumber = fromHouseNumber - 1;

            if (seedsInHouse > 0) {
                houseNode.getStyleClass().add("brightened");
                houseNode.setText("0");
                runLater(() -> {
                    stock.getStyleClass().add("brightened");
                    stock.setText(String.valueOf(Integer.parseInt(stock.getText()) + seedsInHouse));
                    collectSeeds(previousHouseNumber, stock, minSeeds, maxSeeds);
                });
                break;
            }
            fromHouseNumber--;
        }
    }


    private Labeled getActivePlayerNodeByHouseNumber(int houseNumber) {
        return getHouseNode(houseNumber, game.getActivePlayerNumber());
    }




    /**
     * Disables all human player's houses
     */
    private void setDisableHumanPlayerHouses() {
        setDisableHumanPlayerHouses(new ArrayList<>());
    }

    /**
     * Disables human player's houses that are not eligible for sowing
     *
     * @param eligibleHouses list of eligible houses
     */
    private void setDisableHumanPlayerHouses(List<Integer> eligibleHouses) {
        for (Node node : gridBoard.getChildren()) {
            Integer columnIndex = GridPane.getColumnIndex(node);
            if (GridPane.getRowIndex(node) == 1 && columnIndex > 0 && columnIndex <= awele.model.GameBoard.N_HOUSES_PER_PLAYER) {
                node.setDisable(!eligibleHouses.contains(columnIndex));
            }
        }
    }

    private void removeHousesStyles() {
        for (Node node : gridBoard.getChildren()) {
            node.getStyleClass().remove("brightened");
            node.getStyleClass().remove("picked");
        }
    }








    /**
     * Displays a game board
     *
     * @param board board to display
     */
    private void displayBoard(GameBoard board) {
        for (Node node : gridBoard.getChildren()) {
            ((Labeled) node).setText(getHouseValue(GridPane.getRowIndex(node), GridPane.getColumnIndex(node), board));
        }
    }

    /**
     * @param row house row in the grid (corresponds to the player number)
     * @param column house column in the grid (in the order for the human player and reversed for the virtual player)
     * @param board game board from which get the houses values
     * @return the number of seeds contained in the house at row/column position (see class comment)
     */
    private String getHouseValue(int row, int column, awele.model.GameBoard board) {
        // the first column doesn't correspond to a house
        if (column == 0) return null;

        if (column <= GameBoard.N_HOUSES_PER_PLAYER) {
            if (row == 0) {
                // for the virtual player (player 0), return the values in the reversed order
                return String.valueOf(board.getHouseValueByIndex(GameBoard.N_HOUSES_PER_PLAYER - column));
            } else {
                // for the human player (player 1), return the values in the order
                return String.valueOf(board.getHouseValueByIndex(GameBoard.N_HOUSES_PER_PLAYER + column - 1));
            }
        } else {
            // player stock
            return String.valueOf(board.getStockByPlayer(row));
        }
    }

    /**
     * @param houseNumber house number
     * @param playerNumber player number (0 for virtual player & 1 for human player)
     * @return the house node from the grid associated to the player / house number combination
     */
    private Labeled getHouseNode(int houseNumber, int playerNumber) {
        if (playerNumber == 0) {
            for (Node node : gridBoard.getChildren()) {
                if (GridPane.getRowIndex(node) == playerNumber
                        && GridPane.getColumnIndex(node) == GameBoard.N_HOUSES_PER_PLAYER - houseNumber + 1) {
                    return (Labeled) node;
                }
            }
        } else {
            for (Node node : gridBoard.getChildren()) {
                if (GridPane.getRowIndex(node) == playerNumber && GridPane.getColumnIndex(node) == houseNumber ) {
                    return (Labeled) node;
                }
            }
        }
        throw new IllegalArgumentException(String.format("house number [%d] player number [%d] combination invalid", houseNumber, playerNumber));
    }

    private void runLater(Runnable lastAnimation) {
        animations.add(executor.schedule(() -> {
            animations.pop();
            Platform.runLater(lastAnimation);
        }, ANIMATION_DELAY, TimeUnit.MILLISECONDS));
    }
}
