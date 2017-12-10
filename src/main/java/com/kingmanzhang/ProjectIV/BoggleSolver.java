package com.kingmanzhang.ProjectIV;

import edu.princeton.cs.algs4.Digraph;
import java.util.Arrays;
import java.util.HashSet;

public class BoggleSolver {

    private TrieST_lite<Boolean> dictionary; //use a TST for dictionary
    private HashSet<String> validWords;

    //private boolean getAllValidWordsRun; //whether the getAllValidWords() is run

    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary){

        this.dictionary = new TrieST_lite<>();
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
                //charSet.put(current, board.getLetter(i, j));
                //add left. cannot be the leftmost
                if (j != 0) {
                    digraph.addEdge(current, left);
                }
                //add right
                if (j != col - 1) {
                    digraph.addEdge(current, right);
                }
                //add up
                if (i != 0) {
                    digraph.addEdge(current, up);
                }
                //add down
                if (i != row - 1) {
                    digraph.addEdge(current, down);
                }

                //add left up corner. cannot be the first column or first row
                if (i != 0 && j != 0) {
                    digraph.addEdge(current, left_up);
                }

                //add left bottom corner. cannot be the first column or last row
                if (i != row - 1 && j != 0) {
                    digraph.addEdge(current, left_down);
                }
                //add right up corner. cannot be first row or last col
                if (i != 0 && j != col - 1) {
                    digraph.addEdge(current, right_up);
                }
                //add right bottom corner. cannot be last row or last col
                if (i != row - 1 && j != col - 1) {
                    digraph.addEdge(current, right_down);
                }
            }
        }
    }
    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board){
        //this.getAllValidWordsRun = true;
        this.validWords = new HashSet<>();
        int row = board.rows();
        int col = board.cols();
        char[] charBoard = new char[row * col]; //chars of the board
        //HashMap<Integer, Character> charSet = new HashMap<>();
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

    private String updated_prefix(String prefix, char c) {
        if (c == 'Q') {
            return prefix + "QU";
        } else {
            return prefix + c;
        }
    }

    private void dfs(Digraph digraph, int v, String prefix, HashSet<String> wordset, boolean[] visited, char[] charBoard){
        boolean [] local_visited = Arrays.copyOf(visited, visited.length);
        local_visited[v] = true;
        prefix = updated_prefix(prefix, charBoard[v]);
        if (prefix.length() >= 3 && this.dictionary.contains(prefix)) {
            wordset.add(prefix);
        }
        for (int w : digraph.adj(v)) {
            if (!local_visited[w] && this.dictionary.prefixExist(prefix)) {
                dfs(digraph, w, prefix, wordset, local_visited, charBoard);
            }
        }
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word){
        if (word == null) {
            throw new IllegalArgumentException();
        }
        if (this.dictionary.contains(word)) {
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
        } else {
            return 0;
        }
    }
}
