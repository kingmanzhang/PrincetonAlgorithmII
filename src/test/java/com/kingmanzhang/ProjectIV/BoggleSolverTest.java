package com.kingmanzhang.ProjectIV;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import org.junit.jupiter.api.Test;

class BoggleSolverTest {
    @Test
    void testBoth() {
        String dict_path = "src/test/resources/ProjectIV/dictionary-algs4.txt";
        In in = new In(dict_path);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        String board_path = "src/test/resources/ProjectIV/board4x4.txt";
        BoggleBoard board = new BoggleBoard(board_path);
        int score = 0;
        for (String word : solver.getAllValidWords(board)) {
            StdOut.print(word);
            score += solver.scoreOf(word);
            StdOut.println(" score: " + solver.scoreOf(word));
        }
        StdOut.println("total score: " + score);

    }


}