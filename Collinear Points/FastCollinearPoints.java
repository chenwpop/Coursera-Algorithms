/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Detect collinear 2D points with >= 4 size through the brute force way
 */
public class BruteCollinearPoints {
    private final List<LineSegment> lineSegments;

    /**
     * constructor to detect all groups of collinear points with size larger than 4
     *
     * @param points: input array of Point
     * @throws IllegalArgumentException input array not valid
     */
    public BruteCollinearPoints(Point[] points) {
        if (points == null) throw new IllegalArgumentException();
        Point[] partner = new Point[points.length];
        if (!validInput(points, partner)) throw new IllegalArgumentException();
        // Arraylist to store outputs
        lineSegments = new ArrayList<>();
        int N = partner.length;
        if (N < 4)
            return;
        // Bruteforce
        double slopeij;
        for (int i = 0; i < N; ++i) {
            for (int j = i + 1; j < N; ++j) {
                for (int m = j + 1; m < N; ++m) {
                    for (int n = m + 1; n < N; ++n) {
                        slopeij = partner[i].slopeTo(partner[j]);
                        if (slopeij != partner[i].slopeTo(partner[m])) continue;
                        if (slopeij == partner[i].slopeTo(partner[n]))
                            // in all test cases for BruteCollinearPoints,
                            // it's guaranteed that no more than 4 colinear points
                            lineSegments.add(new LineSegment(partner[i], partner[n]));
                    }
                }
            }
        }
    }

    /**
     * helper method to validate the input Point array
     *
     * @param points  input Point array
     * @param partner auxiliary array to keep {@param points} immute
     * @return whether it's valid
     */
    private boolean validInput(Point[] points, Point[] partner) {
        // check null point or duplicate points
        for (int i = 0; i < points.length; ++i) {
            if (points[i] == null) return false;
            partner[i] = points[i];
        }
        Arrays.sort(partner);
        for (int i = 1; i < partner.length; ++i)
            if (partner[i].compareTo(partner[i - 1]) == 0)
                return false;
        return true;
    }

    /**
     * get the number of segments
     *
     * @return the number of segments
     */
    public int numberOfSegments() {
        return lineSegments.size();
    }

    /**
     * get the line segments
     *
     * @return the array of line segments
     */
    public LineSegment[] segments() {
        return lineSegments.toArray(new LineSegment[0]);
    }

    /**
     * main method to do unit test
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        // do unit test here
    }
}
