/* *****************************************************************************
 *  Name: Chen Wahng
 *  Date: July 24, 2019
 *  Description: solution to the assignment Kd-Trees
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeSet;

/**
 * Brute-force implementation based on java.util.TreeSet
 */
public class PointSET {
    private final TreeSet<Point2D> rbt;

    /**
     * construct an empty set points
     */
    public PointSET() {
        rbt = new TreeSet<Point2D>();
    }

    /**
     * is the set empty?
     *
     * @return true for empty, vice versa
     */
    public boolean isEmpty() {
        return rbt.isEmpty();
    }

    /**
     * how many points contained in the set
     *
     * @return number of points in the set
     */
    public int size() {
        return rbt.size();
    }

    /**
     * Add a point to the set if it's not already inside
     *
     * @param p the point to be inserted
     */
    public void insert(Point2D p) {
        if (!contains(p))
            rbt.add(p);
    }

    /**
     * does the point contained in the set?
     *
     * @param p the point to be checked
     * @return true for already contained, vice versa
     */
    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException("Input point can not be null!");
        return rbt.contains(p);

    }

    /**
     * draw all points to standard draw
     */
    public void draw() {
        for (Point2D point : rbt)
            point.draw();
    }

    /**
     * find all points that are inside the rectangle (boundary included)
     *
     * @param rect the rectange
     * @return an iterable collection of all points included
     */
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException("Illegal Input Rectangle!");
        ArrayList<Point2D> innerPoints = new ArrayList<Point2D>();
        Iterator<Point2D> iterator = rbt.iterator();
        Point2D p;
        while (iterator.hasNext()) {
            p = iterator.next();
            if (rect.contains(p))
                innerPoints.add(p);
        }
        return innerPoints;
    }

    /**
     * find the nearest neighbor in this set to the point, return null for empty set
     *
     * @param p the point to be processed
     * @return the nearest neighbor of point p
     */
    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException("Illegal Input Point!");
        Iterator<Point2D> iterator = rbt.iterator();
        if (!iterator.hasNext()) return null;
        Point2D nearest = iterator.next();
        double dist = nearest.distanceSquaredTo(p);
        double currentDist;
        Point2D currentPoint;
        while (iterator.hasNext()) {
            currentPoint = iterator.next();
            currentDist = currentPoint.distanceSquaredTo(p);
            if (currentDist < dist) {
                nearest = currentPoint;
                dist = currentDist;
            }
        }
        return nearest;
    }

    /**
     * main function for unit test
     *
     * @param args cmd arguments
     */
    public static void main(String[] args) {
        // for unit test
    }
}
