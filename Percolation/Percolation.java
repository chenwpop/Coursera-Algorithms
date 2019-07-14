/* *****************************************************************************
 *  Name: Chen Wang
 *  Date: Apr 17, 2019
 *  Description: Percolation based on Weighted & Path Compressed Quick Union Find
 *  Usage: javac-algs4 Percolation.java
 **************************************************************************** */

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

/**
 * class to model the percolation process
 */
public class Percolation {
    private boolean[] system;
    private final WeightedQuickUnionUF unionFind;
    private final WeightedQuickUnionUF unionFindHelper;
    private final int length;
    private int count;

    /**
     * create n-by-n grid, with all sites blocked
     *
     * @param n size of system
     * @throws IllegalArgumentException when n is nonpositive
     */
    public Percolation(int n) {
        if (n <= 0) throw new IllegalArgumentException();
        system = new boolean[n * n]; // check status (open / closed)
        // create two virtual site for top and bottom row
        // index n^2 for top row, index (n^2 + 1) for bottom row
        unionFind = new WeightedQuickUnionUF(n * n + 2);
        // another UnionFind structure to solve the backwash problem
        unionFindHelper = new WeightedQuickUnionUF(n * n + 1);
        length = n;
        count = 0;
    }

    /**
     * check whether index is valid
     *
     * @param n col# or row#, in range of [1, length]
     * @return true for valid index, false for invalid index
     * @throws IllegalArgumentException when n is nonpositive
     */
    private boolean valid(int n) {
        if (n < 1 || n > length) throw new IllegalArgumentException();
        else return true;
    }


    /**
     * get the plain index of target site
     *
     * @param row row# of target site
     * @param col col# of target site
     * @return the plain index of target site
     */
    private int flatten(int row, int col) {
        if (valid(row) && valid(col))
            return (row - 1) * length + col - 1;
        else return -1;
    }

    /**
     * open the given site if it's not open already
     *
     * @param row row# of target site
     * @param col col# of target site
     */
    public void open(int row, int col) {
        if (valid(row) && valid(col) && !isOpen(row, col)) {
            int index = flatten(row, col);
            system[index] = true;
            count++;
            // open the virtual top row site
            if (row == 1) {
                unionFind.union(col - 1, length * length);
                unionFindHelper.union(col - 1, length * length);
            }
            // open the virtual bottom row site
            if (row == length)
                unionFind.union((length - 1) * length + col - 1,
                                length * length + 1);
            // check for the above site, if open, union them
            if (row > 1 && system[index - length]) {
                unionFind.union(index, index - length);
                unionFindHelper.union(index, index - length);
            }
            // check for the below site, if open, union them
            if (row < length && system[index + length]) {
                unionFind.union(index, index + length);
                unionFindHelper.union(index, index + length);
            }
            // check for the left site, if open, union them
            if (col > 1 && system[index - 1]) {
                unionFind.union(index, index - 1);
                unionFindHelper.union(index, index - 1);
            }
            // check for the right site, if open, union them
            if (col < length && system[index + 1]) {
                unionFind.union(index, index + 1);
                unionFindHelper.union(index, index + 1);
            }
        }
    }

    /**
     * is the site open?
     *
     * @param row row# of target site
     * @param col col# of target site
     * @return whther the site is open
     */
    public boolean isOpen(int row, int col) {
        if (valid(row) && valid(col))
            return system[flatten(row, col)];
        else
            return false;
    }

    /**
     * is site full?
     *
     * @param row row# of target site
     * @param col col# of target site
     * @return whether the site is connected with top row
     */
    public boolean isFull(int row, int col) {
        return isOpen(row, col) && unionFindHelper
                .connected(flatten(row, col), length * length);
    }

    /**
     * number of open sites
     *
     * @return # of open site
     */
    public int numberOfOpenSites() {
        return count;
    }

    /**
     * does the system percolates?
     *
     * @return whether the system percolatesw
     */
    public boolean percolates() {
        return unionFind.connected(length * length, length * length + 1);
    }

    /**
     * main function for unit test
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        // do unit test here
    }
}
