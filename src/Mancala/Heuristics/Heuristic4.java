package Mancala.Heuristics;

import Mancala.Player.MancalaBoard;

import java.util.Random;

public class Heuristic4 extends MancalaHeuristic {
    @Override
    public int getUtilValue(MancalaBoard board) {
        int W1 = new Random().nextInt(MAX_WEIGHT) + 1, W2 = new Random().nextInt(MAX_WEIGHT) + 1;
        int W3 = new Random().nextInt(MAX_WEIGHT) + 1, W4 = new Random().nextInt(MAX_WEIGHT) + 1;
        int maxPlayer = board.getMaxPlayer();
        int minPlayer = MancalaBoard.otherPlayer(maxPlayer);
//		W1 * (stones_in_my_storage – stones_in_opponents_storage) + W2 * (stones_on_my_side –
//		stones_on_opponents_side) + W3 * (additional_move_earned) + W4 * (stones_captured)
        int stones_in_my_storage = board.getStonesInStorage(maxPlayer);//board.getPlayersTotalStones( board.currentPlayer() );
        int stones_in_opponents_storage = board.getStonesInStorage(minPlayer);//board.getPlayersTotalStones( MancalaBoard.otherPlayer( board.currentPlayer() ) );
        int stones_in_my_side = board.getPlayersTotalStones(maxPlayer);
        int stones_in_opponents_side = board.getPlayersTotalStones(minPlayer);
        return W1 * (stones_in_my_storage - stones_in_opponents_storage) + W2 * (stones_in_my_side - stones_in_opponents_side) +
                W4 * board.getStonesMoved();
    }
}
