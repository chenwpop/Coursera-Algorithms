/* *****************************************************************************
 *  Name: Chen Wahng
 *  Date: 19-07-11
 *  Description: The solution to Coursera Algorithms II, Week 1 assignment
 *****************************************************************************/

import edu.princeton.cs.algs4.Digraph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

/**
 * A functional immutable class to solve problems about Shortest Ancestral Path (SAP). Basically,
 * aiming at find the length of SAP, as well as find out the Lowest Common Ancestor (LCA)
 */
public class SAP {
    private final Digraph G;

    /**
     * constructor takes a digraph (not necessarily a DAG)
     *
     * @param aG the digraph
     */
    public SAP(Digraph aG) {
        G = new Digraph(aG);
    }

    /**
     * length of the SAP between v and w; -1 for no such path
     *
     * @param v vertex v
     * @param w vertex w
     * @return length of SAP, -1 for no such path
     */
    public int length(int v, int w) {
        List<Integer> vv = new ArrayList<>();
        vv.add(v);
        List<Integer> ww = new ArrayList<>();
        ww.add(w);
        return length(vv, ww);
    }

    /**
     * find the LCA; -1 for no such path
     *
     * @param v vertex v
     * @param w vertex w
     * @return the LCA, -1 for no such vertex
     */
    public int ancestor(int v, int w) {
        List<Integer> vv = new ArrayList<>();
        vv.add(v);
        List<Integer> ww = new ArrayList<>();
        ww.add(w);
        return ancestor(vv, ww);
    }

    /**
     * A helper function to find the length of SAP and LCA. Idea is to go through all ancestors of v
     * first, at the same time track their (shortest) distance to v, then walk through w's ancestor
     * and meanwhile update the length of SAP.
     *
     * @param v a collection of vertices
     * @param w a collection of vertices
     * @return {LCA, length of SAP}
     */

    private int[] helper(Iterable<Integer> v, Iterable<Integer> w) {
        // queue to walk through all vertices
        Queue<Integer> queue = new LinkedList<>();
        // to keep track of all visited vertices
        boolean[] marked = new boolean[G.V()];
        v.forEach(x -> {
            if (x == null || x < 0 || x > G.V() - 1) throw new IllegalArgumentException();
            marked[x] = true;
            queue.add(x);
        });
        // to compute the distance from each ancestor to v
        int[] distToV = new int[G.V()];
        // to compute the distance from each ancestor to w
        int[] distToW = new int[G.V()];
        // to store all ancestors of vertex v.
        Set<Integer> set = new HashSet<>();
        // find all ancestors of vertex v and count their distance to v
        while (!queue.isEmpty()) {
            int current = queue.poll();
            set.add(current);
            for (int i : G.adj(current))
                if (!marked[i]) {
                    marked[i] = true;
                    distToV[i] = distToV[current] + 1;
                    queue.add(i);
                }
        }
        // erase records of visited vertices
        Arrays.fill(marked, false);
        w.forEach(x -> {
            if (x == null || x < 0 || x > G.V() - 1) throw new IllegalArgumentException();
            marked[x] = true;
            queue.add(x);
        });
        // to store the LCA and length of SAP
        int[] ans = { G.V(), G.E() };
        while (!queue.isEmpty()) {
            int current = queue.poll();
            if (set.contains(current) && ans[1] >= distToV[current] + distToW[current]) {
                ans[0] = current;
                ans[1] = distToV[current] + distToW[current];
            }
            for (int i : G.adj(current))
                if (!marked[i]) {
                    marked[i] = true;
                    distToW[i] = distToW[current] + 1;
                    queue.add(i);
                }
        }
        if (ans[0] == G.V()) return new int[] { -1, -1 };
        return ans;
    }

    /**
     * length of shortest ancestral path between any vertex in v and any vertex in w; -1 for no such
     * path
     *
     * @param v a collection of vertices
     * @param w a collection of vertices
     * @return length of SAP, -1 for no such path
     */
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) throw new IllegalArgumentException();
        return helper(v, w)[1];

    }

    /**
     * a common ancestor that participates in a shortest ancestral path; -1for no such path
     *
     * @param v a collection of vertices
     * @param w a collection of vertices
     * @return the common ancestor, -1 for no such vertex
     */
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) throw new IllegalArgumentException();
        return helper(v, w)[0];
    }

    /**
     * doing unit test here
     *
     * @param args cmd args
     */
    public static void main(String[] args) {
        // do unit test here.
    }
}
