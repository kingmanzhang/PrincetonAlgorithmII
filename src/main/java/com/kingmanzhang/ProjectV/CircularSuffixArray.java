package com.kingmanzhang.ProjectV;

import edu.princeton.cs.algs4.StdOut;

public class CircularSuffixArray {

    private char[] s;
    private final int N;
    private int[] index;
    private final int CUTOFF = 5;

    // circular suffix array of s
    public CircularSuffixArray(String s){
        if (s == null) {
            throw new IllegalArgumentException();
        }

        this.N = s.length();
        this.s = s.toCharArray();
        this.index = new int[this.N];
        for (int i = 0; i < this.N; i++) {
            this.index[i] = i;
        }

        sort(this.s, 0, this.N - 1, 0);
    }

    private void sort(char[] s, int low, int high, int offset) {
        if (high - low < this.CUTOFF) {
            insertion(s, low, high, offset);
            return;
        } else {
            int lt = low;
            int gt = high;
            int pv = charAt(s, offset, index[low]);
            int i = low + 1;
            while (i <= gt) {
                int q = charAt(s, offset, index[i]);
                if (q < pv) {
                    swap(this.index, lt++, i++);
                } else if (q > pv) {
                    swap(this.index, i, gt--);
                } else {
                    i++;
                }
            }
            sort(s, low, lt - 1, offset);
            if(pv >= 0 && offset < this.N) {
                sort(s, lt, gt, offset + 1);
            }
            sort(s, gt + 1, high, offset);
        }
    }

    private void insertion(char[] s, int low, int high, int offset) {

        for(int i = low; i <= high; i++) {
            for (int j = i; j > low && less(s, index[j], index[j - 1], offset); j--) {
                swap(this.index, j, j - 1);
            }
        }


    }

    private boolean less(char[] s, int l, int r, int offset) {

        for (int i = offset; i < this.N; i++) {
            char lc = (char) charAt(s, i, l);
            char rc = (char) charAt(s, i, r);
            if (lc < rc) {
                return true;
            }
            if (lc > rc) {
                return false;
            }
        }
        return false;

    }
    private int charAt(char[] s, int offset, int i) {

        return s[(offset + i) % this.N];

    }

    private void swap(int[] index, int lt, int rt) {
        int temp = index[lt];
        index[lt] = index[rt];
        index[rt] = temp;
    }
    // N of s
    public int length(){

        return this.N;

    }

    // returns index of ith sorted suffix
    public int index(int i){

        if (i < 0 || i >= this.N) {
            throw new IllegalArgumentException();
        }
        return this.index[i];
    }


    // unit testing (required)
    public static void main(String[] args){

        String test = "ABRACADABRA!";
        CircularSuffixArray circularSuffixArray = new CircularSuffixArray(test);
        int len = circularSuffixArray.length();
        StdOut.println("N is: " + len);
        for (int i = 0; i < len; i++) {
            StdOut.println(i + "\t" + circularSuffixArray.index(i));
        }

    }
}
