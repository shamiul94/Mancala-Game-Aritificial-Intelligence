package Mancala.Player;


import Mancala.Heuristics.MancalaHeuristic;

import java.io.FileNotFoundException;
import java.io.PrintStream;

public class Main {
    public static final int numBinsOnEachSide = 6;
    public static final int numStonesPerBin = 4;
    public static final boolean DEBUG = false;
    public static int MAX_DEPTH = 15;
    public static Stdin stdin;
    public static final int nMaxStages = 150;

    public static void main(String[] args) {
        stdin = new Stdin();
        playLoop();
    }


///======================== IO Methods =======================================////

    public static void playLoop() {
        int bins = numBinsOnEachSide;//stdin.readInt( "Specify the number of bins on each side." );
        int stones = numStonesPerBin;//stdin.readInt( "Specify the number of stones initially in each bin." );
        MancalaHeuristic s0 = selectStrategy(1);
        MancalaHeuristic s1 = selectStrategy(2);
//		PrintStream ps = System.out;
        try {
            System.setOut(new PrintStream("out.log"));
            System.setErr(new PrintStream("err.log"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        int player_1_Win = 0, player_2_Win = 0, draw = 0;
        int times = 100;
        for (int i = 0; i < times; i++) {
            System.out.println("\n\nGame " + (i + 1) + " Starts.");
            MAX_DEPTH += 1;
            int winner = startGamePlay(bins, stones, s0, s1);
            if (winner == 0) {
                draw++;
            } else if (winner == 1) {
                player_1_Win++;
            } else if (winner == 2) {
                player_2_Win++;
            }
        }

        System.out.println("Total games: " + times);
        System.out.println("Player 1 wins: " + player_1_Win + " times.");
        System.out.println("Player 2 wins: " + player_2_Win + " times.");
        System.out.println("Match drawn: " + draw + " times.");

//        startGamePlay(bins, stones, s0, s1);
//		System.setOut( ps );
    }

    public static MancalaHeuristic selectStrategy(int n) {
        return MancalaHeuristic.intToStrategy(n);
    }


    ///======================== IO Methods =======================================////
//================================================================================

    public static int startGamePlay(int bins, int stones, MancalaHeuristic s0, MancalaHeuristic s1) {
        MancalaBoard board = new MancalaBoard(bins, stones, s0, s1);

        System.out.println(board);

        int round = 0;

        while (!board.isGameOver() && round < nMaxStages) {
            System.out.println("------------" + round + "--------------");
            int currentPlayer = board.currentPlayer();
            System.out.println("Player " + currentPlayer + "\'s move.");
            int bin = board.move();
            if (bin <= 0) break;
            System.out.println("Player " + currentPlayer + " selects "
                    + board.stonesMoved() + " stones from bin " + bin);
            System.out.println(board);
            System.out.println("\n\n\n--------------------------\n\n\n");
            round++;
        }
        System.out.println("Final board configuration:\n");
        System.out.println(board);
        if (board.getBin(0, 0) == board.getBin(1, 0)) {
            System.out.println("The game ends in a tie!");
            return 0;
        } else if (board.getBin(0, 0) > board.getBin(1, 0)) {
            System.out.println("Player0 wins!");
            return 1;
        } else {
            System.out.println("Player1 wins!");
            return 2;
        }
    }


}
