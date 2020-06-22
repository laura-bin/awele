package awele.gamelogic;

import awele.model.GameBoard;

import java.util.List;

public class Node {

    GameBoard board; // game state

    GameStatus status;


    // houses stocked to calculate the following moves
    List<Integer> houses;
    List<Integer> stocks; // stocks kept

    // node value
    int value; // seeds captured by the player who played the move

    // player -1, 1 or 0 (draw)
    int winner;

    boolean isTerminal; // if the Node ends the game i.e. win by captured more than 24 seeds or no more move possible


    public Node(List<Integer> houses, List<Integer> stocks, int value) {
        this.houses = houses;
        this.stocks = stocks;
        this.value = value;
        this.isTerminal = false;
    }

    public boolean isTerminal() {
        return false;
    }

    public List<Integer> getHouses() {
        return houses;
    }

    public List<Integer> getStocks() {
        return stocks;
    }

    public GameBoard getBoard() {
        return board;
    }

    public GameStatus getStatus() {
        return status;
    }

    public void setStatus(GameStatus status) {
        this.status = status;
    }

    public int getValue() {
        return value;
    }

    public void addToValue(int valueToAdd) {
        value += valueToAdd;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
