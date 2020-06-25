package awele.gamelogic;

import awele.console.view.ConsoleDisplay;
import awele.model.GameBoard;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

// http://stephane.ayache.perso.luminy.univ-amu.fr/zoom/cours/Cours/IA_Jeux/IAEtJeux2.pdf
// https://en.wikipedia.org/wiki/Alpha%E2%80%93beta_pruning
public class HardPlayer implements VirtualPlayer {

    private final int VIRTUAL_PLAYER = 1;
    private final int BOUNDARY = Integer.MAX_VALUE;
    private ConsoleDisplay c = new ConsoleDisplay();

    /**
     * Negamax algorithm :
     * tests all the moves and the opponent's next move and selects the best
     *
     * @param game from which choose a house number (between 1 & the number of houses per player)
     * @return the chosen number (between 1&6)
     */
    @Override
    public int pickHouseForSowing(Game game) {
        long time = System.nanoTime();

        MoveNode initialNode = new MoveNode(game.getGameBoard(), 0, 0);
        MoveNode selectedNode = minimax(initialNode, 10, VIRTUAL_PLAYER, -BOUNDARY, BOUNDARY);
        return selectedNode.getHouse();
    }

    /**
     * Minimax algorithm with alpha-beta pruning
     * Move evaluation is based on the number of seeds captured by the player
     * and the win / lose / draw end (lose / draw are eliminated)
     */
    private MoveNode minimax(MoveNode node, int depth, int player, int alpha, int beta) {
        Game game = new Game(node.getBoard());
        List<Integer> eligibleHouses = game.getEligibleHouseNumbers(convertToGamePlayer(player));

        if (eligibleHouses.isEmpty()) {
            game.collectRemainingSeeds(convertToGamePlayer(player));
            node.setValue(-BOUNDARY * player); // set the value to eliminate this option
        }

        if (depth > 0 && game.getStatus() == GameStatus.IN_PROGRESS) {
            List<MoveNode> followingNodes = generateFollowingNodes(
                    game.getGameBoard(), player, eligibleHouses, node.getHouse(), depth);

            if (player == VIRTUAL_PLAYER) {
                // max evaluation
                MoveNode bestMove = new MoveNode(game.getGameBoard(), 0, -BOUNDARY);
                for (MoveNode followingNode : followingNodes) {
                    MoveNode nextMove = minimax(followingNode, depth - 1, -player, alpha, beta);
                    bestMove = bestMove.getValue() > nextMove.getValue() ? bestMove : nextMove;
                    if (bestMove.getValue() >= beta) break; // beta break
                    alpha = Math.max(alpha, bestMove.getValue());
                }
                return bestMove;
            } else {
                // min evaluation
                MoveNode bestMove = new MoveNode(game.getGameBoard(), 0, BOUNDARY);
                for (MoveNode followingNode : followingNodes) {
                    MoveNode nextMove = minimax(followingNode, depth - 1, -player, alpha, beta);
                    bestMove = bestMove.getValue() < nextMove.getValue() ? bestMove : nextMove;
                    if (alpha >= bestMove.getValue()) break; // alpha break
                    beta = Math.min(beta, bestMove.getValue());
                }
                return bestMove;
            }
        }
        return node;
    }

    /**
     * Generates the following nodes (next possible moves) from a given node
     *
     * @param board          board from which the following moves are generated
     * @param player         active player (1 for virtual player, -1 for human player)
     * @param eligibleHouses house numbers (between 1&6) from which a move can be generated
     * @return list of nodes containing a possible state of the game and a value associated,
     * reverse ordered for alpha-beta pruning (the higher value come first in the list)
     */
    public List<MoveNode> generateFollowingNodes(GameBoard board, int player, List<Integer> eligibleHouses,
                                                 int originalHouse, int depth) {
        List<MoveNode> followingNodes = new ArrayList<>();

        for (int house : eligibleHouses) {
            Game game = new Game(board);
            int playerNumber = convertToGamePlayer(player);
            game.captureSeedsFromHouseIndex(game.sowSeeds(house, playerNumber), playerNumber);
            // set the value depending on the player's stock and with higher value for immediate gain
            int value = game.getGameBoard().getStockByPlayer(convertToGamePlayer(player));// * depth;
            switch (game.getStatus()) {
                case END_WIN:
                    value = BOUNDARY;
                    break;
                case END_LOSE:
                    value = -BOUNDARY;
                    break;
                case END_DRAW:
                    value = -BOUNDARY * player;
                    break;
            }
            MoveNode newNode = new MoveNode(game.getGameBoard(), originalHouse == 0 ? house : originalHouse, value);
            followingNodes.add(newNode);
        }

        if (player == VIRTUAL_PLAYER) {
            followingNodes.sort(Comparator.comparingInt(MoveNode::getValue).reversed());
        } else {
            followingNodes.sort(Comparator.comparingInt(MoveNode::getValue));
        }

        return followingNodes;
    }

    /**
     * Converts the player number of the algorithm to the corresponding player number in the game
     *
     * @param player player number to convert
     * @return player number in the game
     */
    private int convertToGamePlayer(int player) {
        return player == VIRTUAL_PLAYER ? PlayerType.VIRTUAL.ordinal() : PlayerType.HUMAN.ordinal();
    }

}
