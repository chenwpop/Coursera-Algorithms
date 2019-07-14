
/* *****************************************************************************
 *  Name: Chen Wang
 *  Date: Apr 17, 2019
 *  Description: Mente Carlo Percolation Simulation
 *  Useage:
 *      javac-algs4 PercolationStats.java
 *      java-algs4 PercolationStats [size of percolation grid]
 *                                  [number of experiments]
 **************************************************************************** */

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.Stopwatch;

/**
 * functional class to compute the statistics of the percolation process
 */
public class PercolationStats {
    private static final double CONFIDENCE = 1.96;
    private final int trials;
    private final double mean;
    private final double stddev;

    /**
     * perform trials independent experiments on an n-by-n grids
     *
     * @param n       size of square grid
     * @param aTrials number of experiments
     */
    public PercolationStats(int n, int aTrials) {
        if (n < 1 || aTrials < 1) throw new IllegalArgumentException();
        trials = aTrials;
        double[] stats = new double[trials];
        for (int i = 0; i < trials; ++i) {
            Percolation percolation = new Percolation(n);
            while (!percolation.percolates()) {
                int col = StdRandom.uniform(n) + 1;
                int row = StdRandom.uniform(n) + 1;
                while (percolation.isOpen(row, col)) {
                    col = StdRandom.uniform(n) + 1;
                    row = StdRandom.uniform(n) + 1;
                }
                percolation.open(row, col);
            }
            stats[i] = (double) percolation.numberOfOpenSites() / (n * n);
        }
        mean = StdStats.mean(stats);
        stddev = StdStats.stddev(stats);
    }

    /**
     * sample mean of percolation threshold
     *
     * @return mean of stats[]
     */
    public double mean() {
        return mean;
    }

    /**
     * sample standard deviation of percolation threshold
     *
     * @return standard deviation
     */
    public double stddev() {
        return stddev;
    }

    /**
     * compute the low endpoint of 95% confidence interval
     *
     * @return low endpoint of 95% confidence interval
     */
    public double confidenceLo() {
        return mean - CONFIDENCE * stddev / Math.sqrt(trials);
    }

    /**
     * compute the high endpoint of 95% confidence interval
     *
     * @return high endpoint of 95% confidence interval
     */
    public double confidenceHi() {
        return mean + CONFIDENCE * stddev / Math.sqrt(trials);
    }

    /**
     * main function to do unit test
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        Stopwatch stopwatch = new Stopwatch();
        int n = Integer.parseInt(args[0]);
        int trails = Integer.parseInt(args[1]);
        PercolationStats percolationStats = new PercolationStats(n, trails);
        System.out.println(String.format("%-24s= %.10f",
                                         "mean ",
                                         percolationStats.mean()));
        System.out.println(String.format("%-24s= %.10f",
                                         "stddev",
                                         percolationStats.stddev()));
        System.out.println(String.format("%-24s= [%.10f, %.10f]",
                                         "95% confidence interval ",
                                         percolationStats.confidenceLo(),
                                         percolationStats.confidenceHi()));
        System.out.printf("Total Runnint Time: %f\n", stopwatch.elapsedTime());
    }
}
