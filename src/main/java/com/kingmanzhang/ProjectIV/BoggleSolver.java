package com.kingmanzhang.ProjectIV;

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.TST;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;

public class BoggleSolver {

    private TST<Boolean> dictionary; //use a TST for dictionary
    private HashSet<String> validWords;
    //private boolean getAllValidWordsRun; //whether the getAllValidWords() is run

    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary){

        //this.getAllValidWordsRun = false;
        this.dictionary = new TST<>();
        this.validWords = new HashSet<>();
        //put all word in the dictionary to the TST as (word, true) pairs
        for (int i = 0; i < dictionary.length; i++) {
            this.dictionary.put(dictionary[i], true);
        }

    }

    private void boardToDigraph(BoggleBoard board, Digraph digraph, char[] charBoard) {
        if (board == null) {
            throw new IllegalArgumentException();
        }
        int row = board.rows();
        int col = board.cols();
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                int current = i * col + j;
                int left = current - 1;
                int right = current + 1;
                int up = current - col;
                int down = current + col;
                int left_up = up - 1;
                int left_down = down  - 1;
                int right_up = up + 1;
                int right_down = down + 1;
                charBoard[current] = board.getLetter(i, j);
                //add left. cannot be the leftmost
                if (j != 0) {
                    digraph.addEdge(current, left);
                }
                //add right
                if (j != col) {
                    digraph.addEdge(current, right);
                }
                //add up
                if (i != 0) {
                    digraph.addEdge(current, up);
                }
                //add down
                if (i != row) {
                    digraph.addEdge(current, down);
                }

                //add left up corner. cannot be the first column or first row
                if (i != 0 && j != 0) {
                    digraph.addEdge(current, left_up);
                }

                //add left bottom corner. cannot be the first column or last row
                if (i != row && j != 0) {
                    digraph.addEdge(current, left_down);
                }
                //add right up corner. cannot be first row or last col
                if (i != 0 && j != col) {
                    digraph.addEdge(current, right_up);
                }
                //add right bottom corner. cannot be last row or last col
                if (i != row && j != col) {
                    digraph.addEdge(current, right_down);
                }
            }
        }
    }
    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board){
        //this.getAllValidWordsRun = true;
        int row = board.rows();
        int col = board.cols();
        char[] charBoard = new char[row * col]; //chars of the board
        Digraph digraph = new Digraph(row * col);
        //create a digraph from the board
        //and get an array of chars in the board
        boardToDigraph(board, digraph, charBoard);

        boolean[] visited = new boolean[row * col];
        //do dfs from every vertice
        for (int i = 0; i < row * col; i++) {
            Arrays.fill(visited, false);
            String prefix = "";
            dfs(digraph, i, prefix, this.validWords, visited, charBoard);
        }
        return new HashSet<>(this.validWords);
    }

    private void dfs(Digraph digraph, int v, String prefix, HashSet<String> wordset, boolean[] visited, char[] charBoard){
        visited[v] = true;
        prefix = prefix + charBoard[v];
        if (charBoard[v] == 'Q') {
            prefix += 'U';
        }
        if (prefix.length() >= 3 && this.dictionary.get(prefix)) {
            wordset.add(prefix);
        }
        if (this.dictionary.keysWithPrefix(prefix).iterator().hasNext()) {
            for (int w : digraph.adj(v)) {
                if (!visited[w]) {
                    dfs(digraph, w, prefix, wordset, visited, charBoard);
                }
            }
        }
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word){
        if (word == null) {
            throw new IllegalArgumentException();
        }
        Iterator<String> itr = this.validWords.iterator();
        if (this.validWords.contains(word)) {
            int len = word.length();
            switch (len) {
                case 0:
                    return 0;
                case 1:
                    return 0;
                case 2:
                    return 0;
                case 3:
                    return 1;
                case 4:
                    return 1;
                case 5:
                    return 2;
                case 6:
                    return 3;
                case 7:
                    return 5;
                default:
                    return 11;
            }
            /**
            if (len <= 2){
                return 0;
            }
            if (len <= 4){
                return 1;
            }
            if (len == 5){
                return 2;
            }
            if (len == 6){
                return 3;
            }
            if (len == 7){
                return 5;
            }
            return 11;
             **/
        } else {
            return 0;
        }
    }
}
