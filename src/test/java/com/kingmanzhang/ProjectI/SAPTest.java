package com.kingmanzhang.ProjectI;

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class SAPTest {

    static SAP sap1;
    static SAP sap2;
    static SAP sap3;
    static SAP sap4;
    static SAP sap5;
    static SAP sap6;
    static SAP sap9;
    static SAP sap10;
    static SAP sap11;


    @BeforeAll
    static void initAll() {
        //SAPTest sapTest = new SAPTest();
        //Path path = Paths.get(sapTest.getClass().getResource("ProjectI/digraph1.txt").getFile());
        In in1 = new In("src/test/resources/ProjectI/digraph1.txt");
        In in2 = new In("src/test/resources/ProjectI/digraph2.txt");
        In in3 = new In("src/test/resources/ProjectI/digraph3.txt");
        In in4 = new In("src/test/resources/ProjectI/digraph4.txt");
        In in5 = new In("src/test/resources/ProjectI/digraph5.txt");
        In in6 = new In("src/test/resources/ProjectI/digraph6.txt");
        In in9 = new In("src/test/resources/ProjectI/digraph9.txt");
        In in10 = new In("src/test/resources/ProjectI/digraph-ambiguous-ancestor.txt");
        //In in11 = new In("src/test/resources/ProjectI/digraph-wordnet.txt");

        Digraph digraph1 = new Digraph(in1);
        Digraph digraph2 = new Digraph(in2);
        Digraph digraph3 = new Digraph(in3);
        Digraph digraph4 = new Digraph(in4);
        Digraph digraph5 = new Digraph(in5);
        Digraph digraph6 = new Digraph(in6);
        Digraph digraph9 = new Digraph(in9);
        Digraph digraph10 = new Digraph(in10);
        //Digraph digraph11 = new Digraph(in11);
/**
        StdOut.println(digraph1);
        StdOut.println(digraph2);
        StdOut.println(digraph3);
        StdOut.println(digraph4);
        StdOut.println(digraph5);
        StdOut.println(digraph6);
        StdOut.println(digraph9);
        StdOut.println(digraph10);
        //StdOut.println(digraph11);
**/
        sap1 = new SAP(digraph1);
        sap2 = new SAP(digraph2);
        sap3 = new SAP(digraph3);
        sap4 = new SAP(digraph4);
        sap5 = new SAP(digraph5);
        sap6 = new SAP(digraph6);
        sap9 = new SAP(digraph9);
        sap10 = new SAP(digraph10);
        //sap11 = new SAP(digraph11);

    }

    @Test
    void lengthTest() {

        assertEquals(4, sap1.length(3, 11));
        assertEquals(4, sap1.length(7, 2));
        assertEquals(2, sap1.length(7, 8));
        assertEquals(1, sap1.length(11, 10));
        assertEquals(-1, sap1.length(1, 6));

        assertEquals(2, sap2.length(1, 5));
        assertEquals(2, sap2.length(1, 3));

        assertEquals(3, sap3.length(13, 11));
        assertEquals(-1, sap3.length(13, 1));


    }

    @Test
    void ancestorTest() {
        assertEquals(0, sap1.ancestor(1, 2));
        assertEquals(1, sap1.ancestor(3, 11));
        assertEquals(-1, sap3.ancestor(13, 1));
        assertEquals(-1, sap1.ancestor(1, 6));
    }

    @Test
    void lengthTest2() {

        assertEquals(4, sap1.length(Arrays.asList(7, 8), Arrays.asList(9, 10)));
        assertEquals(0, sap1.length(Arrays.asList(7, 3), Arrays.asList(8, 3)));
        assertEquals(-1, sap3.length(Arrays.asList(13, 14), Arrays.asList(1, 2)));
        assertThrows(IllegalArgumentException.class, () -> {
            sap3.length(Arrays.asList(13, 15), Arrays.asList(0, 11));
        });



    }

    @Test
    void ancestorTest2() {

        assertEquals(1, sap1.ancestor(Arrays.asList(7, 8), Arrays.asList(9, 10)));
        assertEquals(1, sap1.ancestor(Arrays.asList(3, 1), Arrays.asList(4, 1)));
        assertEquals(-1, sap3.ancestor(Arrays.asList(0, 14), Arrays.asList(1, 2)));
        assertEquals(8, sap3.ancestor(Arrays.asList(13, 0), Arrays.asList(1, 7)));
        assertThrows(IllegalArgumentException.class, () -> {
            sap3.ancestor(Arrays.asList(13, 15), Arrays.asList(1, 0));
        });

    }

}