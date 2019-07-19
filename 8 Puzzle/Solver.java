/* *****************************************************************************
 *  Name: Chen Wahng
 *  Date: Jult 18, 2019
 *  Description: solution to the assignment 8puzzle
 **************************************************************************** */

import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;

import java.util.Comparator;

/**
 * functional class to slove the 8 puzzle based on A* algorithm
 */
public final class Solver {
    private final int minMove;
    private final Stack<Board> stack;

    /**
     * inner class to keep track of the solver path, as well as caching the priority
     */
    private class Node {
        private final Board board;
        private final Node predecessor;
        private final int move;
        private final int priority; // store the manhattan distance to avoid recomputing

        public Node(Board aBoard, int aMove, Node aPredecessor) {
            board = aBoard;
            move = aMove;
            predecessor = aPredecessor;
            priority = board.manhattan() + move;
        }
    }

    /**
     * find a solution with A* algorithm with the initial board
     *
     * @param initial the initial board
     */
    public Solver(Board initial) {
        if (initial == null) throw new IllegalArgumentException("Initial Board can not be null!");
        Node now1 = null, now2 = null;
        Comparator<Node> comparator = (n1, n2) -> (n1.priority - n2.priority);
        // build two priority to solve the initial board and one of its random twin
        MinPQ<Node> pq1 = new MinPQ<Node>(comparator);
        pq1.insert(new Node(initial, 0, null));
        MinPQ<Node> pq2 = new MinPQ<Node>(comparator);
        pq2.insert(new Node(initial.twin(), 0, null));

        while ((now1 == null || !now1.board.isGoal())
                && (now2 == null || !now2.board.isGoal())) {
            now1 = pq1.delMin();
            now2 = pq2.delMin();
            // enqueue all neighbor boards unless it's identical to the predecessor
            for (Board neighbor : now1.board.neighbors())
                if (now1.predecessor == null || !neighbor.equals(now1.predecessor.board))
                    pq1.insert(new Node(neighbor, now1.move + 1, now1));
            for (Board neighbor : now2.board.neighbors())
                if (now2.predecessor == null || !neighbor.equals(now2.predecessor.board))
                    pq2.insert(new Node(neighbor, now2.move + 1, now2));
        }

        // retrieve the path
        if (!now1.board.isGoal()) {
            minMove = -1;
            stack = null;
        }
        else {
            minMove = now1.move;
            stack = new Stack<Board>();
            while (now1 != null) {
                stack.push(now1.board);
                now1 = now1.predecessor;
            }
        }
    }

    /**
     * is the initial board solvable?
     *
     * @return whether it's solvable
     */
    public boolean isSolvable() {
        return minMove != -1;
    }

    /**
     * min number of moves to solve the initial board, -1 for unsolvable
     *
     * @return min number of moves
     */
    public int moves() {
        return minMove;
    }

    /**
     * sequence of boards in a shortest solution, null if unsolvable
     *
     * @return steps
     */
    public Iterable<Board> solution() {
        return stack;
    }

    /**
     * main method to do unit test
     *
     * @param args input file name
     */
    public static void main(String[] args) {
        // do unit test here
    }
}
