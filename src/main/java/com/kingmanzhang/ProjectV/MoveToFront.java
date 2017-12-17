package com.kingmanzhang.ProjectV;

import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;
import edu.princeton.cs.algs4.StdOut;

import java.util.LinkedList;
import java.util.List;

public class MoveToFront {

    //private LinkedList<Character> ascii;
    private static final int R = 256;

    public MoveToFront() {

    }
    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode() {

        List<Character> posChar = new LinkedList<>(); //stores ascii symbols in a linkedlist
        int[] charPos = new int[256]; //index is character, value is their position in the linkedlist
        for (int i = 0; i < R; i++) {
            posChar.add((char)i);         //add ascii symbols to the linkedlist
            charPos[i] = i;               //initialize the position for each symbol
        }


        while(!BinaryStdIn.isEmpty()) {
            char next = BinaryStdIn.readChar(); //takes an input char
            int out = charPos[next];            //output the position of
            //StdOut.print("out: " + out + "\t");
            //move next to first
            if (out != 0) {
                posChar.remove(out);
                posChar.add(0, next);
                //update position for chars at [0, out] in charPos
                charPos[next] =0;
                for (int i = 1; i <= out; i++) {
                    charPos[posChar.get(i)] += 1;
                }
            }
            BinaryStdOut.write(out, 8);
  //StdOut.println("in: " + next + " out: " + (char)out);
        }
        BinaryStdIn.close();
        BinaryStdOut.close();

    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {

        List<Character> posChar = new LinkedList<>();
        //int[] charPos = new int[255];
        for (int i = 0; i < R; i++) {
            posChar.add((char)i);
            //charPos[i] = i;
        }

        while(!BinaryStdIn.isEmpty()) {
            int next = BinaryStdIn.readInt(8); //read 8 bit
            //StdOut.println("next: " + next);
            char out = posChar.get(next);
            //StdOut.println("out: " + out);
            if (next != 0) {
                posChar.remove(next);
                posChar.add(0, out);
                //charPos[next] = 0;
                //for (int i = 1; i <= next; i++) {
                //    charPos[posChar.get(i)] += 1;
                //}
            }
            BinaryStdOut.write(out);
        }
        BinaryStdIn.close();
        BinaryStdOut.close();
    }

    // if args[0] is '-', apply move-to-front encoding
    // if args[0] is '+', apply move-to-front decoding
    public static void main(String[] args) {
        if (args.length == 0) {
            throw new IllegalArgumentException("No argument is provided!");
        }

        if (args[0].equals("-")) {
            encode();
        }
        if (args[0].equals("+")) {
            decode();
        }

    }


}
