package com.kingmanzhang.ProjectV;

import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BurrowsWheeler {

    private class Node implements Comparable<Node> {
        private char c;
        private int i;
        private Node(char c, int i) {
            this.c = c;
            this.i = i;
        }


        @Override
        public int compareTo(Node o) {
            if (this.c < o.c) {
                return -1;
            } else if (this.c > o.c) {
                return 1;
            } else {
                return 0;
            }
        }
    }
    // apply Burrows-Wheeler transform, reading from standard input and writing to standard output
    public static void transform(){
        String input = BinaryStdIn.readString();
        BinaryStdIn.close();
        final int N = input.length();
        CircularSuffixArray circularSuffixArray = new CircularSuffixArray(input);
        int first = 0;
        char[] output = new char[N];
        for (int i = 0; i < N; i++) {
        //StdOut.printf("i: %d, index(%d): %d \n", i, i, circularSuffixArray.index(i));
            if (circularSuffixArray.index(i) == 0) {
                first = i;
        //StdOut.println("first is changed to " + i);
                output[i] = input.charAt(N - 1);
            } else {
                output[i] = input.charAt(circularSuffixArray.index(i) - 1);
            }
        }

        BinaryStdOut.write(first, 32);
        for (int i = 0; i < N; i++) {
            BinaryStdOut.write(output[i]);
        }
        BinaryStdOut.close();
    }

    // apply Burrows-Wheeler inverse transform, reading from standard input and writing to standard output
    public static void inverseTransform(){

        int first = BinaryStdIn.readInt(); //read in first int
        List<Node> compressed = new ArrayList<>();
        int i = 0;
        while(!BinaryStdIn.isEmpty()) {
            char c = BinaryStdIn.readChar();
            compressed.add(new BurrowsWheeler().new Node(c, i++));
        }
        BinaryStdIn.close();
        Collections.sort(compressed);
        final int N = compressed.size();
        i = 0;
        int [] next = new int[N];
        for (Node node : compressed) {
           next[i++] = node.i;
        }

        char[] ori = new char[N];
        int j = first;
        for(i = 0; i < N; i++) {
            ori[i] = compressed.get(j).c;
            j = next[j];
        }

        for(i = 0; i < N; i++) {
            BinaryStdOut.write(ori[i]);
        }
        BinaryStdOut.close();
    }

    // if args[0] is '-', apply Burrows-Wheeler transform
    // if args[0] is '+', apply Burrows-Wheeler inverse transform
    public static void main(String[] args){
        if (args[0].equals("-")) {
            transform();
        } else if (args[0].equals("+")) {
            inverseTransform();
        } else {
            throw new IllegalArgumentException();
        }

    }
}
