/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;
import java.util.List;

/**
 * a mutable class uses a 2-D tree to performs range search as well as nearest point
 */
public class KdTree {
    private Node root;
    private int size;
    /**
     * These parameters are used to track the nearest points. only works for serial program. As for
     * parallel computing, create another private class to simutaneouly update the minimum distance
     * and nearest point.
     */
    private double dist;
    private Point2D nearest;

    private class Node {
        private Point2D point;
        private RectHV rect;
        private Node left;
        private Node right;

        public Node() {
        }

        public Node(Point2D p, RectHV rect) {
            this.point = p;
            this.rect = rect;
        }

        public Node(Point2D p, RectHV rect, Node left, Node right) {
            this.point = p;
            this.rect = rect;
            this.left = left;
            this.right = right;
        }

    }

    /**
     * Construct an empty set of points
     */
    public KdTree() {
    }

    /**
     * is the set empty?
     *
     * @return whether the set is empty
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * number of points in this set
     *
     * @return # points in this set
     */
    public int size() {
        return size;
    }

    /**
     * add the point to the set if it's not already in the set
     *
     * @param p the point to be inserted
     * @throws IllegalArgumentException input Point2D is null
     */
    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        if (navigate(root, p, true)) return;
        root = navigate(root, p, new RectHV(0, 0, 1, 1), true);
        size++;
    }

    /**
     * navigate across the Kd Tree and insert a new point
     *
     * @param node  the node contains the base point
     * @param p     the point to be inserted
     * @param rect  the RectHV coordinated with this the point in node
     * @param layer true for even layer, which compares x-coordanate, vice versa
     * @return the Node to be visited next
     */
    private Node navigate(Node node, Point2D p, RectHV rect, boolean layer) {
        if (node == null) {
            return new Node(p, rect);
        }

        if (layer) {
            if (p.x() < node.point.x())
                node.left = navigate(node.left, p,
                                     new RectHV(rect.xmin(), rect.ymin(), node.point.x(),
                                                rect.ymax()), !layer);
            else node.right = navigate(node.right, p,
                                       new RectHV(node.point.x(), rect.ymin(), rect.xmax(),
                                                  rect.ymax()), !layer);
        }
        else {
            if (p.y() < node.point.y())
                node.left = navigate(node.left, p,
                                     new RectHV(rect.xmin(), rect.ymin(), rect.xmax(),
                                                node.point.y()), !layer);
            else node.right = navigate(node.right, p,
                                       new RectHV(rect.xmin(), node.point.y(), rect.xmax(),
                                                  rect.ymax()), !layer);
        }
        return node;
    }

    /**
     * navigate across the KdTree and test whether a point is inside
     *
     * @param p     the point to be tested
     * @param node  the node that contains the base point
     * @param layer true for even layer, which compares x coordinate, vice versa
     * @return true for contains, false for not contains
     */
    private boolean navigate(Node node, Point2D p, boolean layer) {
        if (node == null || node.point == null) return false;
        if ((node.point.y() == p.y()) &&
                (node.point.x() == p.x())) return true;
        if ((layer && p.x() < node.point.x()) ||
                (!layer && p.y() < node.point.y()))
            return navigate(node.left, p, !layer);
        else {
            return navigate(node.right, p, !layer);
        }
    }

    /**
     * does the set contains point p?
     *
     * @param p the point to be tested
     * @return true for contains, vice versa
     * @throws IllegalArgumentException input Point2D is null
     */
    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        return navigate(root, p, true);
    }

    /**
     * navigate across the KdTree and draw each point and splitting bar
     *
     * @param node  the current node
     * @param layer true for even layer, which compares x-coordinate, vice versa
     */
    private void draw(Node node, boolean layer) {
        if (node == null || node.point == null) return;
        RectHV rect = node.rect;
        if (layer) {
            StdDraw.setPenColor(StdDraw.RED);
            new Point2D(node.point.x(), rect.ymin())
                    .drawTo(new Point2D(node.point.x(), rect.ymax()));
            draw(node.left, !layer);
            draw(node.right, !layer);
        }
        else {
            StdDraw.setPenColor(StdDraw.BLUE);
            new Point2D(rect.xmin(), node.point.y())
                    .drawTo(new Point2D(rect.xmax(), node.point.y()));
            draw(node.left, !layer);
            draw(node.right, !layer);
        }
        StdDraw.setPenColor(StdDraw.BLACK);
        // StdDraw.setPenRadius(0.006);
        node.point.draw();
        // StdDraw.setPenRadius(0.002);
    }

    /**
     * draw all points to standard draw
     */
    public void draw() {
        draw(root, true);
    }

    /**
     * search across the KdTree to find all points inside rect
     *
     * @param rect the RectHV
     * @param node current node that is being searched
     * @param list the Iterable<KdTree> to store the insiders
     */
    private void search(RectHV rect, Node node, List<Point2D> list) {
        if (node == null || !node.rect.intersects(rect)) return;
        if (rect.contains(node.point)) list.add(node.point);
        search(rect, node.left, list);
        search(rect, node.right, list);
    }

    /**
     * Find all points that are inside the rectangle
     *
     * @param rect the Rectangle
     * @return all insiders
     * @throws IllegalArgumentException query RectHV is null
     */
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException();
        List<Point2D> list = new ArrayList<Point2D>();
        search(rect, root, list);
        return list;
    }


    /**
     * search across the KdTree to find the nearest point to p
     *
     * @param p     the base point
     * @param node  current node neing searched
     * @param layer a flag to mark searching order
     */
    private void search(Point2D p, Node node, boolean layer) {
        if (node == null || node.rect.distanceSquaredTo(p) >= dist) return;
        double localDist = node.point.distanceSquaredTo(p);
        if (localDist < dist) {
            dist = localDist;
            nearest = node.point;
        }
        if ((layer && p.x() < node.point.x()) || (!layer && p.y() < node.point.y())) {
            search(p, node.left, !layer);
            search(p, node.right, !layer);
        }
        else {
            search(p, node.right, !layer);
            search(p, node.left, !layer);
        }
    }

    /**
     * find the nearest point in the set to point p, null if the set is empty
     *
     * @param p the base point
     * @return nearest point to point p
     * @throws IllegalArgumentException query Point2D is null
     */
    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        dist = Math.sqrt(2);
        search(p, root, true);
        return nearest;
    }


    /**
     * main method for unit test
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        // for unit test
    }
}
