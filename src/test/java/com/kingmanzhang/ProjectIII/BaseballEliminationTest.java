package com.kingmanzhang.ProjectIII;

import com.sun.xml.internal.rngom.parse.host.Base;
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
        assertThrows(IllegalArgumentException.class, ()-> {
            baseballElimination.wins("Wisconsin");
        });
    }

    @Test
    void losses() {
        String file1 = "src/test/resources/ProjectIII/teams4.txt";
        BaseballElimination baseballElimination = new BaseballElimination(file1);
        assertEquals(71, baseballElimination.losses("Atlanta"));
        assertEquals(79, baseballElimination.losses("Philadelphia"));
        assertEquals(78, baseballElimination.losses("New_York"));
        assertEquals(82, baseballElimination.losses("Montreal"));
        assertThrows(IllegalArgumentException.class, ()-> {
            baseballElimination.losses("Wisconsin");
        });
    }

    @Test
    void remaining() {
        String file1 = "src/test/resources/ProjectIII/teams4.txt";
        BaseballElimination baseballElimination = new BaseballElimination(file1);
        assertEquals(8, baseballElimination.remaining("Atlanta"));
        assertEquals(3, baseballElimination.remaining("Philadelphia"));
        assertEquals(6, baseballElimination.remaining("New_York"));
        assertEquals(3, baseballElimination.remaining("Montreal"));
        assertThrows(IllegalArgumentException.class, ()-> {
            baseballElimination.remaining("Wisconsin");
        });
    }

    @Test
    void against() {
        String file1 = "src/test/resources/ProjectIII/teams4.txt";
        BaseballElimination baseballElimination = new BaseballElimination(file1);
        assertEquals(1, baseballElimination.against("Atlanta", "Philadelphia"));
        assertEquals(1, baseballElimination.against("Philadelphia", "Atlanta"));
        assertEquals(6, baseballElimination.against("Atlanta", "New_York"));
        assertEquals(0, baseballElimination.against("New_York", "Montreal"));
        assertThrows(IllegalArgumentException.class, ()-> {
            baseballElimination.against("Wisconsin", "Atlanta");
        });
        assertThrows(IllegalArgumentException.class, ()-> {
            baseballElimination.against("Atlanta", "Wisconsin");
        });
        assertThrows(IllegalArgumentException.class, ()-> {
            baseballElimination.against("Wisconsin", "Minnesota");
        });

    }

    @Test
    void isEliminated() {
        /**
        String file1 = "src/test/resources/ProjectIII/teams4.txt";
        BaseballElimination baseballElimination = new BaseballElimination(file1);
        assertEquals(true, baseballElimination.isEliminated("Montreal"));
        StdOut.println("test Philadelphia");
        assertEquals(true, baseballElimination.isEliminated("Philadelphia"));
        StdOut.println("test New York");
        assertEquals(false, baseballElimination.isEliminated("New_York"));
**/

        String file2 = "src/test/resources/ProjectIII/teams5.txt";
        BaseballElimination baseballElimination2 = new BaseballElimination(file2);
        StdOut.println("test New York");
        assertEquals(false, baseballElimination2.isEliminated("New_York"));
        StdOut.println("\n\ntest Baltimore");
        assertEquals(false, baseballElimination2.isEliminated("Baltimore"));
        StdOut.println("\n\ntest Detroit");
        assertEquals(true, baseballElimination2.isEliminated("Detroit"));

    }

    @Test
    void certificateOfElimination() {
        StdOut.println("team4");
        String file1 = "src/test/resources/ProjectIII/teams4.txt";
        checkall(file1);
        StdOut.println("\n\nteam5");
        String file2 = "src/test/resources/ProjectIII/teams5.txt";
        checkall(file2);
        StdOut.println("\n\nteam12");
        String file3 = "src/test/resources/ProjectIII/teams12.txt";
        checkall(file3);

    }

    private void checkall(String file) {
        BaseballElimination division = new BaseballElimination(file);
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            } else {
                StdOut.println(team + " is not eliminated");
            }
        }
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