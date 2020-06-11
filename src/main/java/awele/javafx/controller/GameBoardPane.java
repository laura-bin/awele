package awele.javafx.controller;

import awele.gamelogic.*;
import awele.model.GameBoard;
import awele.ui.GameMessage;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
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
    private static final long ANIMATION_DELAY = 600; // time delay for the game animations in milliseconds

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
    @FXML
    private CheckBox playableHouseHint;
    @FXML
    private Button goBackButton;
    @FXML
    private Button quitButton;

    private RootStack root;

    private Game game;

    private final Deque<ScheduledFuture<?>> animations = new ArrayDeque<>(); // animations in progress


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Utils.setText(goBackButton);
        Utils.setText(quitButton);

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

        // add a listener to the hint checkbox to show / hide which houses are eligible for sowing
        playableHouseHint.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                gridBoard.getStyleClass().add("display-hints");
            } else {
                gridBoard.getStyleClass().remove("display-hints");
            }
        });
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
        displayBoard();

        // if no move is possible, collect remaining seeds and end game
        if (eligibleHouses.isEmpty()) {
            setDisableHumanPlayerHouses();
            humanPlayerTurnMarker.setText("");
            virtualPlayerTurnMarker.setText("");
            Utils.setText(playerMessage, GameMessage.IMPOSSIBLE_MOVE);

            game.collectRemainingSeeds();
            runLater(() -> collectRemainingSeeds(1, game.getActivePlayerNumber(),
                    getHouseNode(GameBoard.N_HOUSES_PER_PLAYER + 1, game.getActivePlayerNumber())));
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
                int pickedHouse = game.getVirtualPlayerPickedHouse();
                runLater(() -> sowSeeds(pickedHouse));
            }
        }
    }

    /**
     * Sows seeds when a player picks a house
     *
     * @param pickedHouse house picked number
     */
    public void sowSeeds(int pickedHouse) {
        Labeled houseNode = getHouseNode(pickedHouse, game.getActivePlayerNumber());
        int nSowing = Integer.parseInt(houseNode.getText());
        int player = game.getActivePlayerNumber();

        setDisableHumanPlayerHouses();
        game.sowSeeds(pickedHouse);
        houseNode.getStyleClass().add("picked");
        houseNode.setText("0");
        sowSeeds(pickedHouse, player, pickedHouse, player, nSowing);
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
        if (nSowing == 0) {
            // capture seeds and if the last house updated belongs to the opponent and the capture doesn't starved him,
            // launch the animation
            if (previousPlayer != game.getActivePlayerNumber() && game.captureSeeds(previousHouse, previousPlayer)) {
                runLater(() -> {
                    Labeled stockNode = getHouseNode(GameBoard.N_HOUSES_PER_PLAYER + 1, game.getActivePlayerNumber());
                    captureSeeds(previousHouse, previousPlayer, stockNode);
                });
            } else {
                runLater(() -> {
                    continueGame();
                });
            }
        } else {
            runLater(() -> {
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
            });
        }
    }

    /**
     * Capturing seeds animation
     *
     * @param houseNumber house number from which starting the capture
     * @param fromPlayer player who owns the house from which capture seeds
     * @param stockNode Labeled stock in which the seeds must be added
     */
    private void captureSeeds(int houseNumber, int fromPlayer, Labeled stockNode) {
        Labeled houseNode = getHouseNode(houseNumber, fromPlayer);
        int seedsInHouse = 0;
        if (houseNumber > 0) seedsInHouse = Integer.parseInt(houseNode.getText());
        if (seedsInHouse >= 2 && seedsInHouse <= 3) {
            int previousHouseNumber = houseNumber - 1;
            int seeds = seedsInHouse;
            houseNode.getStyleClass().add("captured");
            houseNode.setText("0");
            runLater(() -> {
                stockNode.getStyleClass().add("stock-update");
                stockNode.setText(String.valueOf(Integer.parseInt(stockNode.getText()) + seeds));
                runLater(() -> captureSeeds(previousHouseNumber, fromPlayer, stockNode));
            });
        } else {
            continueGame();
        }
    }

    /**
     * Collect remaining seeds animation
     *
     * @param houseNumber house number from which starting the collect
     * @param playerNumber player who owns the house from which capture seeds
     * @param stockNode Labeled stock in which the seeds must be added
     */
    private void collectRemainingSeeds(int houseNumber, int playerNumber, Labeled stockNode) {
        if (houseNumber <= GameBoard.N_HOUSES_PER_PLAYER) {
            // the loop is used to skip animation on empty houses
            for (int i = houseNumber; i <= GameBoard.N_HOUSES_PER_PLAYER; i++) {
                Labeled houseNode = getHouseNode(i, playerNumber);
                int seedsInHouse = Integer.parseInt(houseNode.getText());
                if (seedsInHouse > 0) {
                    int nextHouse = houseNumber + 1;
                    houseNode.getStyleClass().add("captured");
                    houseNode.setText("0");
                    runLater(() -> {
                        stockNode.getStyleClass().add("brightened");
                        stockNode.setText(String.valueOf(Integer.parseInt(stockNode.getText()) + seedsInHouse));
                        runLater(() -> collectRemainingSeeds(nextHouse, playerNumber, stockNode));
                    });
                    break;
                }
            }
        } else {
            continueGame();
        }
    }

    /**
     * Continue the game if its status is still in progress
     */
    private void continueGame() {
        if (game.getStatus() == GameStatus.IN_PROGRESS) {
            runLater(() -> {
                game.switchActivePlayer();
                turn();
            });
        } else {
            end();
        }
    }

    /**
     * End animation :
     * - displays the winner
     * - asks for saving score
     */
    private void end() {
        runLater(() -> {
            displayBoard();
            Utils.setText(playerMessage, game.getStatus().getMessage());
            // TODO -> scores / go back ?
        });
    }

    /**
     * Displays the game board
     */
    private void displayBoard() {
        GameBoard board = game.getGameBoard();

        for (Node node : gridBoard.getChildren()) {
            String houseValue = null;
            int houseNumber = GridPane.getColumnIndex(node);
            int playerNumber = GridPane.getRowIndex(node);

            if (houseNumber >= 1 && houseNumber <= GameBoard.N_HOUSES_PER_PLAYER) {
                if (playerNumber == 0) {
                    houseValue = String.valueOf(board.getHouseValueByIndex(GameBoard.N_HOUSES_PER_PLAYER - houseNumber));
                } else {
                    houseValue = String.valueOf(board.getHouseValueByIndex(GameBoard.N_HOUSES_PER_PLAYER + houseNumber - 1));
                }
            } else if (houseNumber == GameBoard.N_HOUSES_PER_PLAYER + 1) {
                houseValue = String.valueOf(board.getStockByPlayer(playerNumber));
            }
            ((Labeled) node).setText(houseValue);
        }
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

    /**
     * Resets all the styles added during the animations
     */
    private void removeHousesStyles() {
        for (Node node : gridBoard.getChildren()) {
            while (node.getStyleClass().contains("brightened")) node.getStyleClass().remove("brightened");
            while (node.getStyleClass().contains("picked")) node.getStyleClass().remove("picked");
            while (node.getStyleClass().contains("captured")) node.getStyleClass().remove("captured");
            while (node.getStyleClass().contains("stock-update")) node.getStyleClass().remove("stock-update");
        }
    }

    /**
     * @param houseNumber house number
     * @param playerNumber player number (0 for virtual player & 1 for human player)
     * @return the house node from the grid associated to the player / house number combination
     */
    private Labeled getHouseNode(int houseNumber, int playerNumber) {
        if (playerNumber == 1 || houseNumber == GameBoard.N_HOUSES_PER_PLAYER + 1) {
            for (Node node : gridBoard.getChildren()) {
                if (GridPane.getRowIndex(node) == playerNumber && GridPane.getColumnIndex(node) == houseNumber ) {
                    return (Labeled) node;
                }
            }
        } else {
            for (Node node : gridBoard.getChildren()) {
                if (GridPane.getRowIndex(node) == playerNumber
                        && GridPane.getColumnIndex(node) == GameBoard.N_HOUSES_PER_PLAYER - houseNumber + 1) {
                    return (Labeled) node;
                }
            }
        }
        throw new IllegalArgumentException(String.format("house number [%d] player number [%d] combination invalid", houseNumber, playerNumber));
    }

    /**
     * Adds an animation in the queue of the application,
     * saves the animation in a list to cancel it on going back
     * adn removes it from the list when it is launched
     *
     * @param animation Runnable animation
     */
    private void runLater(Runnable animation) {
        animations.add(executor.schedule(() -> {
            animations.pop();
            Platform.runLater(animation);
        }, ANIMATION_DELAY, TimeUnit.MILLISECONDS));
    }
}
