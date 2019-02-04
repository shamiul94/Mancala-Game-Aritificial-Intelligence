package Mancala.Heuristics;


import Mancala.Player.Main;
import Mancala.Player.MancalaBoard;
import Algorithms.Minimax.Minimax;

public abstract class MancalaHeuristic {
    public static final int MAX_WEIGHT = 15;

    public int selectMove(MancalaBoard board) {
        int bin = 0;
        try {
            bin = Minimax.minimax(board, Main.MAX_DEPTH) + 1; // index starts from 0 bt bin from 1
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bin;
    }
    // Return the index of a non-empty bin to move.
    // Assumes that at least one move is possible.

    public static MancalaHeuristic intToStrategy(int i) {
        // Returns a new MancalaHeuristic corresponding to the number between 0 and 3.
        switch (i) {
            case 1:
                return new Heuristic1();
            case 2:
                return new Heuristic2();
            case 3:
                return new Heuristic3();
            case 4:
                return new Heuristic4();
            default:
                return new UserHeuristic();
        }
    }

    public abstract int getUtilValue(MancalaBoard board);
}
 