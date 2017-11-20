package com.kingmanzhang.ProjectI;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CodeTest {
    public static void main(String[] args) {
//both works
        //CodeTest codetest = new CodeTest();
        //File newfile = new File(codetest.getClass().getResource("/ProjectI/synsets3.txt").getFile());
        File newfile = new File("src/test/resources/ProjectI/synsets3.txt");
        In test = new In(newfile);
        while(test.hasNextLine()) {
            StdOut.println(test.readLine());
        }
    }



}
