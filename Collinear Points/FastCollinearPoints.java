/* *****************************************************************************
 *  Name: Chen Wahng
 *  Date: July 18, 2019
 *  Description: solution to the assignment Collinear Points
 **************************************************************************** */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Detect groups of collinear points with size larger than 4 through a faster algorithm
 */
public class FastCollinearPoints {
    private final List<LineSegment> lineSegments;

    /**
     * constructor to detect all collinear points
     *
     * @param points input Point array
     * @throws IllegalArgumentException input array not valid
     */
    public FastCollinearPoints(Point[] points) {
        if (!validInput(points)) throw new IllegalArgumentException();
        // arraylist to store outputs
        lineSegments = new ArrayList<LineSegment>();
        int N = points.length;
        if (N < 4) return;
        Point[] partner = new Point[N - 1];
        for (int i = 0; i < points.length; ++i) {
            // copy and sort all other points in slope order
            int index = 0;
            for (Point point : points) {
                if (points[i] == point) continue;
                partner[index++] = point;
            }
            // find colinear points
            Arrays.sort(partner, points[i].slopeOrder());
            index = 0;
            int count = 1;
            double current = points[i].slopeTo(partner[index++]);
            double update;
            Point[] temp;
            while (index < N - 1) {
                update = points[i].slopeTo(partner[index]);
                if (current == update)
                    count++;
                else {
                    if (count > 2) {
                        temp = Arrays.copyOfRange(partner, index - count, index);
                        Arrays.sort(temp);
                        if (points[i].compareTo(temp[0]) < 0)
                            lineSegments.add(new LineSegment(points[i],
                                                             temp[count - 1]));
                    }
                    current = update;
                    count = 1;
                }
                index++;
            }
            if (count > 2) {
                temp = Arrays.copyOfRange(partner, index - count, index);
                Arrays.sort(temp);
                if (points[i].compareTo(temp[0]) < 0)
                    lineSegments.add(new LineSegment(points[i],
                                                     temp[count - 1]));
            }
        }
    }

    /**
     * helper method to validate the input
     *
     * @param points input Point array
     * @return whether the input array is valid
     */
    private boolean validInput(Point[] points) {
        // check null input
        if (points == null)
            return false;
        // check null point
        Point[] partner = new Point[points.length];
        for (int i = 0; i < points.length; ++i) {
            if (points[i] == null) return false;
            partner[i] = points[i];
        }
        // check duplicate points
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
     * get the array of segments
     *
     * @return the array of segments
     */
    public LineSegment[] segments() {
        return lineSegments.toArray(new LineSegment[0]);
    }

    /**
     * main method for unit test
     */
    public static void main(String[] args) {
        // do unit test here
    }
}
