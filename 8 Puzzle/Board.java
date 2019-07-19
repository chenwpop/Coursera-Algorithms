/* *****************************************************************************
 *  Name: Chen Wahng
 *  Date: Jult 18, 2019
 *  Description: solution to the assignment 8puzzle
 **************************************************************************** */

import edu.princeton.cs.algs4.StdRandom;

import java.util.ArrayList;
import java.util.List;

/**
 * an immutable class to cache data of a 8 puzzle board, generate twin board, and compute board
 * diatances
 */
public final class Board {
    private final int[][] blocks;
    private Board twin;
    private int blank;

    /**
     * Construct a board instance from an n-by-n array of blocks
     *
     * @param blocks a n-by-n array to initialize the board
     */
    public Board(int[][] blocks) {
        int dim = blocks.length;
        this.blocks = new int[dim][dim];
        for (int i = 0; i < dim; ++i) {
            for (int j = 0; j < dim; ++j) {
                this.blocks[i][j] = blocks[i][j];
                if (blocks[i][j] == 0) {
                    blank = i * dim + j;
                }
            }
        }
    }

    /**
     * board dimension n
     *
     * @return board dimenstion n
     */
    public int dimension() {
        return blocks.length;
    }

    /**
     * number of blocks out of place
     *
     * @return hamming distance
     */
    public int hamming() {
        int count = 0, dim = blocks.length;
        for (int i = 0; i < dim; ++i) {
            for (int j = 0; j < dim; ++j) {
                if (blocks[i][j] != (i * dim + j + 1) && (i != dim - 1 || j != dim - 1)) count++;
            }
        }
        return count;
    }

    /**
     * sum of Manhantan distance between board and the goal board
     *
     * @return Manhantan distance
     */
    public int manhattan() {
        int count = 0, dim = blocks.length;
        int x, y;
        for (int i = 0; i < dim; ++i) {
            for (int j = 0; j < dim; ++j) {
                if (blocks[i][j] == 0 ||
                        blocks[i][j] == i * dim + j + 1)
                    continue;
                x = (blocks[i][j] - 1) / dim;
                y = (blocks[i][j] - 1) % dim;
                count += Math.abs(x - i) + Math.abs(y - j);
            }
        }
        return count;
    }

    /**
     * is this the board the goal board?
     *
     * @return true for the goal board, vice versa
     */
    public boolean isGoal() {
        return hamming() == 0;
    }

    /**
     * a board that is obtained by exchanging any pair of blocks
     *
     * @return one possible twin board
     */
    public Board twin() {
        if (twin != null) return twin;
        int dim = blocks.length;
        int x = StdRandom.uniform(dim);
        int y = StdRandom.uniform(dim);
        while (blocks[x][y] == 0) {
            x = StdRandom.uniform(dim);
            y = StdRandom.uniform(dim);
        }
        int x2 = StdRandom.uniform(dim);
        int y2 = StdRandom.uniform(dim);
        while (blocks[x2][y2] == 0 || (x == x2 && y == y2)) {
            x2 = StdRandom.uniform(dim);
            y2 = StdRandom.uniform(dim);
        }
        int[][] newBlocks = new int[dim][dim];
        for (int i = 0; i < dim; ++i) {
            for (int j = 0; j < dim; ++j) {
                newBlocks[i][j] = blocks[i][j];
            }
        }
        swap(newBlocks, x, y, x2, y2);
        twin = new Board(newBlocks);
        return twin;
    }

    /**
     * is this board equals to y?
     *
     * @param y another board
     * @return true for equal, vice versa
     */
    public boolean equals(Object y) {
        if (y == this) return true;
        if (y == null) return false;
        if (y.getClass() != this.getClass())
            return false;
        Board temp = (Board) y;
        int dim = blocks.length;
        if (temp.blocks.length != dim) return false;
        for (int i = 0; i < dim; ++i) {
            for (int j = 0; j < dim; ++j) {
                if (temp.blocks[i][j] != this.blocks[i][j]) return false;
            }
        }
        return true;
    }

    /**
     * helper function to swap two values in a 2d array
     *
     * @param array the array to be modified
     * @param x1    x-coordinate of the first site
     * @param y1    y-coordinate of the first site
     * @param x2    x-coordinate of the second site
     * @param y2    y-coordinate of the second site
     */
    private void swap(int[][] array, int x1, int y1, int x2, int y2) {
        int temp = array[x1][y1];
        array[x1][y1] = array[x2][y2];
        array[x2][y2] = temp;
    }

    /**
     * helper function to create a twin board coming from switching one pair of random non-blank
     * sites. Swap the board twice to keep it immute
     *
     * @param x1 x-coordinate of the first site
     * @param y1 y-coordinate of the first site
     * @param x2 x-coordinate of the second site
     * @param y2 y-coordinate of the second site
     * @return the twin board
     */
    private Board create(int x1, int y1, int x2, int y2) {
        swap(blocks, x1, y1, x2, y2);
        Board newBoard = new Board(blocks);
        swap(blocks, x1, y1, x2, y2);
        return newBoard;
    }

    /**
     * all neighboring boards
     *
     * @return an iterable collection for all neighbors
     */
    public Iterable<Board> neighbors() {
        List<Board> nbs = new ArrayList<Board>();
        int dim = blocks.length, blankX = blank / dim, blankY = blank % dim;
        if (blankX > 0) nbs.add(create(blankX, blankY, blankX - 1, blankY));
        if (blankX < dim - 1) nbs.add(create(blankX, blankY, blankX + 1, blankY));
        if (blankY > 0) nbs.add(create(blankX, blankY, blankX, blankY - 1));
        if (blankY < dim - 1) nbs.add(create(blankX, blankY, blankX, blankY + 1));
        return nbs;
    }

    /**
     * string representation of this board in specific output format
     *
     * @return the string representation
     */
    public String toString() {
        StringBuilder sb = new StringBuilder(blocks.length + "\n");
        int dim = blocks.length;
        for (int i = 0; i < dim; ++i) {
            for (int j = 0; j < dim; ++j) {
                sb.append(String.format("%d ", blocks[i][j]));
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    /**
     * unit test
     *
     * @param args stdin input
     */
    public static void main(String[] args) {
        // for unit test
    }

}
