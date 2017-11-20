package com.kingmanzhang.ProjectI;


import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import java.util.ArrayList;

public class SAP {
    private Digraph G;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        this.G = new Digraph(G); //TODO: necessary to make a deep copy?
    }

    // helper method to test whether a number is a valid vertex
    private boolean isValid(int x) {
        if (x < 0 || x > G.V() - 1) {
            return false;
        }
        return true;
    }

    //a helper method to find common ancestor and shortest length
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
        /**
        if (w_path.hasPathTo(v)) {
            sap(w, v);
        }
        if (v_path.hasPathTo(w)) {
            len = v_path.distTo(w);
            ancestor = w;
        }
        for (int i = 0; i < G.V(); i++) {
            if (v_path.hasPathTo(i) && i != w) {
                BreadthFirstDirectedPaths i_path = new BreadthFirstDirectedPaths(G.reverse(), i);
                if (i_path.hasPathTo(w) && v_path.distTo(i) + i_path.distTo(w) < len) {
                    len = v_path.distTo(i) + i_path.distTo(w);
                    ancestor = i;
                }
            }
        }
         **/

        return new int[]{ancestor, len};

    }
    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {

        if (!isValid(v) || !isValid(w)) {
            throw new IllegalArgumentException();
        }


        return sap(v, w)[1];

    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {  //Two possibilities: 1. one vertex is the ancestral of another one; 2. share one

        if (!isValid(v) || !isValid(w)) {
            throw new IllegalArgumentException();
        }
        return sap(v, w)[0];
    }



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
        return new int[]{ancestor, len};


    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
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

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
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

    // do unit testing of this class
    public static void main(String[] args) {

    }
}
