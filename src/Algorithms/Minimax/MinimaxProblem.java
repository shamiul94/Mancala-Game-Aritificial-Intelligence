package Algorithms.Minimax;

import java.util.ArrayList;

public interface MinimaxProblem {
	// heuristic value to be returned
	double getUtilVal();
	// list of successors to be generated & returned maxPlayer / minPlayer also to be managed here
	ArrayList< MinimaxProblem > getSuccessors();
	// returns true for leaf nodes
	boolean isTerminal();
	// returns true if this player to be maximized, false to minimize
	boolean isMaximizing();
	// compare two objects to check whether they are same or not
	boolean problemequals( MinimaxProblem o );
}
