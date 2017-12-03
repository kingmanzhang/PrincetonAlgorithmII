package com.kingmanzhang.ProjectIII;

import edu.princeton.cs.algs4.StdOut;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

class BaseballEliminationTest {

    @Test
    void testConstructor() {
        String file = "src/test/resources/ProjectIII/teams4.txt";
        BaseballElimination baseballElimination = new BaseballElimination(file);

        file = "src/test/resources/ProjectIII/teams5.txt";
        baseballElimination = new BaseballElimination(file);

        file = "src/test/resources/ProjectIII/teams12.txt";
        baseballElimination = new BaseballElimination(file);
    }
    @Test
    void numberOfTeams() {
        String file1 = "src/test/resources/ProjectIII/teams4.txt";
        BaseballElimination baseballElimination = new BaseballElimination(file1);
        assertEquals(4, baseballElimination.numberOfTeams());

        String file2 = "src/test/resources/ProjectIII/teams5.txt";
        BaseballElimination baseballElimination2 = new BaseballElimination(file2);
        assertEquals(5, baseballElimination2.numberOfTeams());

        String file3 = "src/test/resources/ProjectIII/teams12.txt";
        BaseballElimination baseballElimination3 = new BaseballElimination(file3);
        assertEquals(12, baseballElimination3.numberOfTeams());
    }

    @Test
    void teams() {
        String file1 = "src/test/resources/ProjectIII/teams4.txt";
        BaseballElimination baseballElimination = new BaseballElimination(file1);
        Iterator<String> itr = baseballElimination.teams().iterator();
        while (itr.hasNext()) {
            StdOut.println(itr.next());
        }


        String file3 = "src/test/resources/ProjectIII/teams12.txt";
        BaseballElimination baseballElimination3 = new BaseballElimination(file3);
        Iterator<String> itr2 = baseballElimination3.teams().iterator();
        while (itr2.hasNext()) {
            StdOut.println(itr2.next());
        }
    }

    @Test
    void wins() {
        String file1 = "src/test/resources/ProjectIII/teams4.txt";
        BaseballElimination baseballElimination = new BaseballElimination(file1);
        assertEquals(83, baseballElimination.wins("Atlanta"));
        assertEquals(80, baseballElimination.wins("Philadelphia"));
        assertEquals(78, baseballElimination.wins("New_York"));
        assertEquals(77, baseballElimination.wins("Montreal"));
    }

    @Test
    void losses() {
        String file1 = "src/test/resources/ProjectIII/teams4.txt";
        BaseballElimination baseballElimination = new BaseballElimination(file1);
        assertEquals(71, baseballElimination.losses("Atlanta"));
        assertEquals(79, baseballElimination.losses("Philadelphia"));
        assertEquals(78, baseballElimination.losses("New_York"));
        assertEquals(82, baseballElimination.losses("Montreal"));
    }

    @Test
    void remaining() {
        String file1 = "src/test/resources/ProjectIII/teams4.txt";
        BaseballElimination baseballElimination = new BaseballElimination(file1);
        assertEquals(8, baseballElimination.remaining("Atlanta"));
        assertEquals(3, baseballElimination.remaining("Philadelphia"));
        assertEquals(6, baseballElimination.remaining("New_York"));
        assertEquals(3, baseballElimination.remaining("Montreal"));
    }

    @Test
    void against() {
        String file1 = "src/test/resources/ProjectIII/teams4.txt";
        BaseballElimination baseballElimination = new BaseballElimination(file1);
        assertEquals(1, baseballElimination.against("Atlanta", "Philadelphia"));
        assertEquals(1, baseballElimination.against("Philadelphia", "Atlanta"));
        assertEquals(6, baseballElimination.against("Atlanta", "New_York"));
        assertEquals(0, baseballElimination.against("New_York", "Montreal"));

    }

    @Test
    void isEliminated() {
    }

    @Test
    void certificateOfElimination() {
    }

    @Test
    void testHashSet() {
        HashSet<Integer> set1 = new HashSet<>();
        set1.add(1);
        set1.add(2);
        HashSet<Integer> set2 = new HashSet<>();
        set2.add(2);
        set2.add(1);
        HashSet<Integer> set3 = new HashSet<>();
        set3.add(1);
        set3.add(3);
        StdOut.println("Set1 is equal to Set2: " + set1.equals(set2));
        StdOut.println("set1 is equal to Set3: " + set1.equals(set3));
    }

}