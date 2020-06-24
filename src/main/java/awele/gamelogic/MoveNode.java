package awele.gamelogic;

import awele.model.GameBoard;


/**
 * Node representing a move (game board state, house number from which the move has been played and value)
 * used to calculate the best move by the machine
 */
public class MoveNode {

    GameBoard board;    // game state
    int originalHouse;  // house originally picked to play the move
    int value;          // move value (seeds captured or win / lose / draw)


    public MoveNode(GameBoard board, int housePicked, int value) {
        this.board = board.copy();
        this.originalHouse = housePicked;
        this.value = value;
    }

    public GameBoard getBoard() {
        return board;
    }

    public int getHouse() {
        return originalHouse;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

}
