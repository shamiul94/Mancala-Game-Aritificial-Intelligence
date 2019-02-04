package Mancala.Player;

// A simple mancala game with a text based interface.


import Mancala.Heuristics.MancalaHeuristic;
import Algorithms.Minimax.MinimaxProblem;

import java.util.ArrayList;
import java.util.Arrays;

public class MancalaBoard implements MinimaxProblem, Cloneable {

    private int[][] board;
    private int bins;
    private int totalStones;
    private int currentPlayer; // Player whose move is next.
    private int maxPlayer;
    private MancalaHeuristic[] strategies; // An array with two elements = the two board' strategies.
    private int stonesMoved = 0; // Stones moved on last move.
    private boolean debugMode = false;
    private int lastMovedBin = 0;

    private static final int STORAGE = 0;

    /// bins is the number of bins per side, not including the mancala.
    /// stones is the number of stones initially in each bin.
    public MancalaBoard(int bins, int stonesPerBin, MancalaHeuristic s1, MancalaHeuristic s2) {
        this.bins = bins;
        this.totalStones = bins * stonesPerBin * 2;
        board = new int[2][bins + 1];

        for (int i = 0; i < 2; i++) {
            for (int j = 1; j <= bins; j++) { // Initially 0 stones in the mancala.
                board[i][j] = stonesPerBin;
            }
        }


        currentPlayer = 0; // Player0 always starts first.

        maxPlayer = 0;
        strategies = new MancalaHeuristic[2];
        strategies[0] = s1;
        strategies[1] = s2;
    }

///=============================== Getter-Setters ===========================///

    public int getBins() {
        return bins;
    }

    public int getPlayersTotalStones(int playerNo) {
        int sum = 0;
        for (int j = 1; j <= bins; j++) {
            sum += board[playerNo][j];
        }
        return sum;
    }

    // stones of mancala cell for a player
    public int getStonesInStorage(int player) {
        return board[player][STORAGE];
    }

    public int getFirstNonEmptyBin(int player) {
        for (int j = 1; j <= bins; j++) {
            if (board[player][j] > 0) return j;
        }
        return -1;
    }

    public int getStonesMoved() {
        return stonesMoved;
    }
///=============================== Getter-Setters ===========================///

    public int countTotalStones() {
        int result = 0;
        for (int i = 0; i <= 1; i++) {
            for (int j = 0; j <= bins; j++) {
                result = result + board[i][j];
            }
        }
        return result;
    }

    public int stonesMoved() {
        return stonesMoved;
    }

    public int currentPlayer() {
        return currentPlayer;
    }

    public int getMaxPlayer() {
        return maxPlayer;
    }

    public void setMaxPlayer(int maxPlayer) {
        this.maxPlayer = maxPlayer;
    }

    public int opponentPlayer() {
        return otherPlayer(currentPlayer);
    }

    void switchPlayer() {
        setCurrentPlayer(opponentPlayer());
    }

    // Given one player, return the other.
    public static int otherPlayer(int player) {
        return (player + 1) % 2;
    }

    // Player defaults to current player.
    public int getBin(int bin) {
        return board[currentPlayer][bin];
    }

    // Bin 0 is the mancala; other bins are numbered 1 through max number.
    public int getBin(int player, int bin) {
        return board[player][bin];
    }

    // Returns true if the currentPlayer cannot move.
    public boolean cannotMove() {
        for (int i = 1; i <= bins; i++) {
            if (board[currentPlayer][i] > 0) {
                return false;
            }
        }
        return true;
    }

    // Steal stones from bin opposite specified bin to put in this player's mancala,
    // along with extra stone about to be place in empty bin.
    private void captureOppositeBin(int player, int bin) {
        int oppositeBin = bins + 1 - bin;
        int oppositePlayer = otherPlayer(player);
        board[player][STORAGE] += board[oppositePlayer][oppositeBin] + 1;
        board[oppositePlayer][oppositeBin] = 0;
    }

    public void flushStones(int player) {
        for (int i = 1; i <= bins; i++) {
            flushBin(player, i);
        }
    }

    // Add stones from bin n to the mancala.
    private void flushBin(int player, int bin) {
        stonesMoved = board[player][bin];
        board[player][STORAGE] += board[player][bin];
        board[player][bin] = 0;
    }

    public void setCurrentPlayer(int currentPlayer) {
        this.currentPlayer = currentPlayer;
//		maxPlayer = currentPlayer;
    }

    public boolean isGameOver() {
        return getPlayersTotalStones(currentPlayer) == 0 && getPlayersTotalStones(opponentPlayer()) == 0;
    }

    public int move() {

        int bin = getFirstNonEmptyBin(currentPlayer());
        if (getPlayersTotalStones(opponentPlayer()) != 0) {
            // Invokes strategy of current player to make move. Returns the bin selected.
            this.setMaxPlayer(currentPlayer);
            bin = strategies[currentPlayer].selectMove(this);
//		if (bin <= 0) return -1;
        }

        move(bin);
        return bin;
    }


    public void move(int bin) {
        lastMovedBin = bin;
        // Bin should be a bin index for current player that holds one or more stones.
        // Performs the basic MancalaBoard move: removes stones from bin
        // and places around board. IF last stone is placed in empty
        // bin one player's side, that stone and opposing stones are
        // placed in player's mancala. If last stone goes in player's
        // MancalaBoard, current player stays the same and so goes again.
        // Otherwise, current player becomes other player.
        int stones = board[currentPlayer][bin];
        if (stones == 0) {
            System.err.println("Error");
            return;
        }
        if (getPlayersTotalStones(opponentPlayer()) == 0) {
            flushStones(currentPlayer);
        } else {
            stonesMoved = stones;
            board[currentPlayer][bin] = 0;
            int currentSide = currentPlayer;
            int currentBin = bin - 1; // Start distributing stones in well to right of selected bin.
            for (int s = stones; s > 0; s--) {
                if ((s == 1) && (currentSide == currentPlayer) && (currentBin > 0)
                        && (board[currentSide][currentBin] == 0)) {
                    // Check for case of "stealing" stones from other side.
                    // Special cases for distributing stones to a mancala.
                    captureOppositeBin(currentPlayer, currentBin);
                } else if (currentBin == 0) {
                    if (currentSide == currentPlayer) {
                        // If it's our mancala, place a stone.
                        board[currentSide][currentBin]++;
                        if (s == 1) {
                            // We're placing our last stone in our own MancalaBoard.
                            if (cannotMove()) {
                                flushStones(otherPlayer(currentPlayer));
                            }
                            return;
                        }
                    } else {
                        // If it's other mancala, don't place a stone.
                        s++; // Counteract subtract of for loop. Yuck!
                    }
                    // In any case, switch to other side.
                    currentSide = otherPlayer(currentSide);
                    currentBin = bins;
                } else {
                    // Regular stone distribution.
                    board[currentSide][currentBin]++;
                    currentBin--;
                }
            }
//			currentPlayer = otherPlayer( currentPlayer );
            setCurrentPlayer(opponentPlayer());
            if (cannotMove()) {
                flushStones(otherPlayer(currentPlayer));
            }
        }
    }

//========================== Minimax Methods =================================

    @Override
    public double getUtilVal() {
        return strategies[maxPlayer].getUtilValue(this);
    }

    public MancalaBoard getSuccessor(int bin) throws CloneNotSupportedException {
        MancalaBoard suc = (MancalaBoard) this.clone();
        suc.move(bin);
        return suc;
    }

    @Override
    public ArrayList<MinimaxProblem> getSuccessors() {
        ArrayList<MinimaxProblem> successorList = new ArrayList<>();
        for (int i = 1; i <= bins; ++i) {
            try {
                if (board[currentPlayer][i] > 0)
                    successorList.add(i - 1, getSuccessor(i));
                else
                    successorList.add(i - 1, null);
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
        }
        return successorList;
    }

    @Override
    public boolean isTerminal() {
        return isGameOver();
    }

    @Override
    public boolean isMaximizing() {
        return currentPlayer == maxPlayer;
    }

    @Override
    public boolean Equals(MinimaxProblem o) {
        if (this == o) return true;
        if (o == null) return false;
        MancalaBoard mancalaBoard = (MancalaBoard) o;
        return bins == mancalaBoard.bins &&
                totalStones == mancalaBoard.totalStones &&
                currentPlayer == mancalaBoard.currentPlayer &&
                stonesMoved == mancalaBoard.stonesMoved &&
//				       maximizing == mancalaBoard.maximizing &&
                Arrays.deepEquals(board, mancalaBoard.board);
    }
// ==========================================================================

    @Override
    protected Object clone() throws CloneNotSupportedException {
        MancalaBoard clone = (MancalaBoard) super.clone();
        clone.board = new int[this.board.length][this.board[0].length];
        for (int r = 0; r < this.board.length; r++)
            if (this.board[r].length >= 0)
                System.arraycopy(this.board[r], 0, clone.board[r], 0, this.board[r].length);
        clone.strategies = this.strategies.clone();
        return clone;
    }


///======================== Print Utilities ==================================////

    public String toString() {
        // Return a string-based representation of this game.
        return edgeLine() + player0Line() + middleLine() + player1Line() + edgeLine()
                + (debugMode ? (countTotalStones() + " stones.\n") : "")
                ;
    }

    public String edgeLine() {
        return "+----" + middleDashes() + "----+\n";
    }

    public String player0Line() {
        StringBuilder sb = new StringBuilder();
        sb.append("|    |");
        for (int i = 1; i <= bins; i++) {
            sb.append(" ").append(numberString(getBin(0, i))).append(" |");
        }
        sb.append("    |\n");
        return sb.toString();
    }

    public String middleLine() {
        return "| " + numberString(getBin(0, 0)) + " "
                + middleDashes()
                + " " + numberString(getBin(1, 0)) + " |\n";
    }

    public String player1Line() {
        StringBuilder sb = new StringBuilder();
        sb.append("|    |");
        for (int i = bins; i > 0; i--) {
            sb.append(" ").append(numberString(getBin(1, i))).append(" |");
        }
        sb.append("    |\n");
        return sb.toString();
    }

    public String middleDashes() {
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i <= bins; i++) {
            sb.append("+----");
        }
        sb.append("+");
        return sb.toString();
    }

    public String numberString(int n) {
        // Return a two character string with an integer.
        // Assumes input is in range [0..99].
        if ((0 <= n) && (n < 10)) {
            return " " + n;
        } else {
            return Integer.toString(n);
        }
    }

    public int getLastMovedBin() {
        return lastMovedBin;
    }

    public void setLastMovedBin(int lastMovedBin) {
        this.lastMovedBin = lastMovedBin;
    }

///======================== Print Utilities ==================================////
//================================================================================


}

