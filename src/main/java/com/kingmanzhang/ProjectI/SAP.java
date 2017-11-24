package com.kingmanzhang.ProjectI;

import edu.princeton.cs.algs4.*;
import java.util.ArrayList;

public class SAP {
    private final Digraph G;

    /**
     * constructor takes a digraph (not necessarily a DAG)
     * @param G: a diagraph
     */
    public SAP(Digraph G) {
        this.G = new Digraph(G);
    }
    

    /**
     * helper method to test whether a number is a valid vertex
     * @param x
     * @return true if query number is non-negative and less than vertice number (exclusive)
     */
    private boolean isValid(int x) {
        if (x < 0 || x > G.V() - 1) {
            return false;
        }
        return true;
    }

    /**
     * a helper method to find common ancestor and shortest length
     * @param v, w: index of two vertices
     * @return ancestor of vertices (v, w) and length between them
     */
    private int[] sap(int v, int w) {

        int len = Integer.MAX_VALUE;
        int ancestor = -1;
        ArrayList<Integer> ancestors = new ArrayList<>();
        BreadthFirstDirectedPaths v_path = new BreadthFirstDirectedPaths(G, v);
        BreadthFirstDirectedPaths w_path = new BreadthFirstDirectedPaths(G, w);
        for (int i = 0; i < G.V(); i++) {
            if (v_path.hasPathTo(i) && w_path.hasPathTo(i)) {
                ancestors.add(i);
            }
        }
        for (int i : ancestors) {
            int temp = v_path.distTo(i) + w_path.distTo(i);
            if (temp < len) {
                len = temp;
                ancestor = i;
            }
        }

        if (len == Integer.MAX_VALUE) {
            len = -1;
        }
        return new int[]{ancestor, len};

    }

    /**
     * Return the length of shortest ancestral path between v and w; -1 if no such path
     */
    public int length(int v, int w) {

        if (!isValid(v) || !isValid(w)) {
            throw new IllegalArgumentException();
        }


        return sap(v, w)[1];

    }

    /**
     * Return a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
     */
    public int ancestor(int v, int w) {  //Two possibilities: 1. one vertex is the ancestral of another one; 2. share one

        if (!isValid(v) || !isValid(w)) {
            throw new IllegalArgumentException();
        }
        return sap(v, w)[0];
    }

    /**
     * A helper method to calculate the ancestor and length of two sets of vertices
     * @return an array that stores the ancestor and length between two sets of vertices
     */
    private int[] sap(Iterable<Integer> v, Iterable<Integer> w) {
        int len = Integer.MAX_VALUE;
        int ancestor = -1;
        ArrayList<Integer> ancesters = new ArrayList<>();
        BreadthFirstDirectedPaths v_path = new BreadthFirstDirectedPaths(G, v);
        BreadthFirstDirectedPaths w_path = new BreadthFirstDirectedPaths(G, w);
        for (int i = 0; i < G.V(); i++) {
            if (v_path.hasPathTo(i) && w_path.hasPathTo(i)) {
                ancesters.add(i);
            }
        }
        for (int i : ancesters) {
            int temp = v_path.distTo(i) + w_path.distTo(i);
            if (temp < len) {
                len = temp;
                ancestor = i;
            }
        }
        if (len == Integer.MAX_VALUE) {
            len = -1;
        }
        return new int[]{ancestor, len};


    }

    /**
     * Return the length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
     */
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) {
            throw new IllegalArgumentException();
        } else {
            for (Integer x : v) {
                if (!isValid(x)) {
                    throw new IllegalArgumentException();
                }
            }
            for (Integer x : w) {
                if (!isValid(x)) {
                    throw new IllegalArgumentException();
                }
            }
        }

        return sap(v, w)[1];
    }

    /**
     * Return a common ancestor that participates in shortest ancestral path; -1 if no such path
     */
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) {
            throw new IllegalArgumentException();
        } else {
            for (Integer x : v) {
                if (!isValid(x)) {
                    throw new IllegalArgumentException();
                }
            }
            for (Integer x : w) {
                if (!isValid(x)) {
                    throw new IllegalArgumentException();
                }
            }
        }

        return sap(v, w)[0];
    }

}
