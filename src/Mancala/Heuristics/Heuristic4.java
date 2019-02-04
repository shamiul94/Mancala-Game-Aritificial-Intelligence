package Mancala.Heuristics;

import Mancala.Player.MancalaBoard;

import java.util.Random;

public class Heuristic4 extends MancalaHeuristic {

    int thisBin;
    int thisBinStones;
    int row;

    public int get_additional_move(MancalaBoard board) {
        int result = 0;
        if (thisBin == thisBinStones) {
            result = 1;
        }
        return result;
    }

    public int get_stones_capture_count(MancalaBoard board) {

        int opponent_row = board.opponentPlayer();

        int result = 0;
        if (thisBin == thisBinStones) {
            result = 0;
        } else if (thisBin > thisBinStones) {
            if (board.getBin(board.currentPlayer(), thisBin - thisBinStones) == 0)
            {
                result += board.getBin(opponent_row, thisBin- thisBinStones);
            }
            else result = 0 ;
        }
        else if(thisBin < thisBinStones)
        {
            if((thisBin - thisBinStones + 13)%13 >= 1 && (thisBin - thisBinStones + 13)%13 <=6)
            {
                if(board.getBin(board.currentPlayer(), (thisBin - thisBinStones + 13)%13) == 0)
                {
                    result += board.getBin(opponent_row, (thisBin - thisBinStones + 13)%13);
                }
            }
            else
                result = 0 ;
        }

        return result;
    }


    @Override
    public int getUtilValue(MancalaBoard board) {
//        row = board.currentPlayer();
//        thisBin = selectMove(board);
//        thisBinStones = board.getbin(row, thisBin);

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
        int additional_move_earned = 1;
        int stones_captured = 1;

//        return W1 * (stones_in_my_storage - stones_in_opponents_storage) + W2 * (stones_in_my_side - stones_in_opponents_side) +
//                + W3 * (additional_move_earned) + W4 * board.getStonesMoved();

        return W1 * (stones_in_my_storage - stones_in_opponents_storage) + W2 * (stones_in_my_side - stones_in_opponents_side) +
                +W3 * (additional_move_earned) + W4 * stones_captured;
    }
}
