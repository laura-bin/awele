package awele.gamelogic;

import awele.model.GameBoard;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

// http://stephane.ayache.perso.luminy.univ-amu.fr/zoom/cours/Cours/IA_Jeux/IAEtJeux2.pdf

public class HardPlayer implements VirtualPlayer {

    private GameBoard board = new GameBoard();
    private final int VIRTUAL_PLAYER = 1;
    private final int OPPONENT = -VIRTUAL_PLAYER;

    private final int DEPTH = 10;
    private final int BOUNDARY = Integer.MAX_VALUE;

    private final int MAX_VALUE = Integer.MAX_VALUE;
    private final int MIN_VALUE = Integer.MIN_VALUE;
    private int maxValue = 10000;
    private int winValue;
    private int loseValue;

    /**
     * Minimax algorithm :
     * tests all the moves and the opponent's next move and selects the best
     * @param eligibleHouseNumbers valid house numbers (between 1 & the number of houses per player)
     * @return
     */

    @Override
    public int pickHouseForSowing(Game game) {
        Node initialNode = new Node(game.getGameBoard().getHouses(), game.getGameBoard().getStocks(), 0);
        return negamax(initialNode, DEPTH, BOUNDARY, -BOUNDARY, VIRTUAL_PLAYER);
    }

/*
    private int minimax(int position, int playerNumber, int depth, List<Integer> eligibleHouses) {
        if (depth == maxdepth) {
            return evaluate(position);
        }
        for (int house : eligibleHouses) {
            List<Integer> newBoard = sowSeedsWithoutUptdate();
            value = minimax(newBoard, opponent, depth +1);
        }
        return max(values);
    }

    private void
*/

    /*
    proc evaluation_mouvement {ref_jeu joueur profondeur mvt alpha} {
        upvar $ref_jeu jeu
        global profondeur_maxi liste_mouvements erreur
        if {[effectuer_mouvement jeu $joueur $mvt] < 0} {return $erreur}
        if {[incr profondeur] >= $profondeur_maxi} {
            return [evaluation_position jeu $joueur]
        }
        set joueur [expr 1 - $joueur]
        set meilleure_evaluation $erreur
        foreach mvt $liste_mouvements {
            array set copie_jeu  [array get jeu]
            set valeur [evaluation_mouvement copie_jeu $joueur $profondeur $mvt [expr - $meilleure_evaluation]]
            if {$valeur == $erreur} {continue}
            set valeur [expr - $valeur]
            if {$valeur > $meilleure_evaluation} {
                set meilleure_evaluation $valeur
                if {$valeur > $alpha} {break}
            }
        }
        return $meilleure_evaluation;
    }
*/


    /*
    function alphabeta(node, depth, i) is
    if depth = 0 or node is a terminal node then
        return the heuristic value of node
    else
        j := -∞
        for each child of node do
            j := max(j, alphabeta(child, depth − 1, j))
            if -j ⩽ i then
                return -j '(* cut-off *)
        return -j

     */

    /**
     * Negamax algorithm with alpha-beta pruning
     * Move evaluation is based on the number of seeds captured by the player
     * and the win / lose / draw end (lose / draw are eliminated)
     */

    // negamax avec elagage alpha beta
    private int negamax(Node node, int depth, int alpha, int beta, int player) {
        // game created for testing
        Game game = new Game(node.getBoard());
        List<Integer> eligibleHouses = game.getEligibleHouseNumbers(convertToGamePlayer(player));

        if (eligibleHouses.isEmpty()) {
            game.collectRemainingSeeds(game.getActivePlayerNumber());
            switch (game.getStatus()) {
                // set the value to eliminate these options :
                case END_LOSE:
                case END_DRAW:
                    node.setValue(-BOUNDARY * player);
                    break;
                // set the value to keep this option :
                case END_WIN:
                    node.setValue(BOUNDARY * player);
                    break;
            }
        }

        if (depth > 0 && game.getStatus() == GameStatus.IN_PROGRESS) {
            int value = -BOUNDARY;
            List<Node> followingNodes = generateFollowingNodes(game.getGameBoard(), player, eligibleHouses);

            for (Node child : followingNodes) {
                value = Math.max(value, -negamax(child, depth - 1, -alpha, -beta, -player));
                alpha = Math.max(alpha, value);
                if (alpha >= beta) break;
            }

            return value;
        }

        return player * node.getValue();
    }

    /**
     * Generates the following nodes (next possible moves) from a given node
     * @param board board from which the following moves are generated
     * @param player active player (1 for virtual player, -1 for human player)
     * @param eligibleHouses house numbers (between 1&6) from which a move can be generated
     * @return list of nodes containing a possible state of the game and a value associated,
     *          reverse ordered for alpha-beta pruning (the higher value come first in the list)
     */
    public List<Node> generateFollowingNodes(GameBoard board, int player, List<Integer> eligibleHouses) {
        List<Node> followingNodes = new ArrayList<>();

        for (int house : eligibleHouses) {
            Game game = new Game(board);
            int houseIndex = game.sowSeeds(house, convertToGamePlayer(player));
            int value = game.captureSeeds(houseIndex);
            switch (game.getStatus()) {
                case END_WIN:
                    value = BOUNDARY;
                    break;
                case END_LOSE:
                case END_DRAW:
                    value = -BOUNDARY;
                    break;
            }
            followingNodes.add(new Node(game.getGameBoard().getHouses(), game.getGameBoard().getStocks(), value));
        }
        followingNodes.sort(Comparator.comparingInt(Node::getValue).reversed());
        return followingNodes;
    }

    private int convertToGamePlayer(int player) {
        return player == VIRTUAL_PLAYER ? PlayerType.VIRTUAL.ordinal() : PlayerType.HUMAN.ordinal();
    }


}
