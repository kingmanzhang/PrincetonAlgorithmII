package com.kingmanzhang.ProjectI;

import edu.princeton.cs.algs4.*;

public class App 
{
    public static void main( String[] args )
    {
        Graph test = new Graph(7);
        test.addEdge(0, 5);
        test.addEdge(0, 1);
        test.addEdge(0, 2);
        test.addEdge(0, 6);
        test.addEdge(1, 3);
        test.addEdge(2, 3);
        test.addEdge(2, 4);
        test.addEdge(4, 5);
        test.addEdge(4, 6);
        Cycle cycletest = new Cycle(test);
        System.out.print("has cycle: " + cycletest.hasCycle());
        for (Integer i : cycletest.cycle()) {
            System.out.print(i + "\t");
        }

        Graph test2 = new Graph(4);
        test2.addEdge(0, 1);
        test2.addEdge(1, 2);
        test2.addEdge(2, 3);
        test2.addEdge(3, 0);

        CycleTest cycleTest2 = new CycleTest(test2);
        System.out.print("has cycle: " + cycleTest2.hasCycle());

    }
}
