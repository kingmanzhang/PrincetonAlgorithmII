package com.kingmanzhang.ProjectI;

import edu.princeton.cs.algs4.StdOut;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

class WordNetTest {
/**
    @BeforeAll
    static void initAll() {

        String syn6_path = "src/test/resources/ProjectI/synsets6.txt";
        String syn8_path = "src/test/resources/ProjectI/synsets8.txt";
        String syn11_path = "src/test/resources/ProjectI/synsets11.txt";
        String syn15_path = "src/test/resources/ProjectI/synsets15.txt";
        String hyper3_path =



        WordNet synsets1 = new WordNet("src/test/resources/ProjectI/digraph1.txt", "src/test/resources/ProjectI/digraph1.txt");
        WordNet synsets1 = new WordNet("", "");
        WordNet synsets1 = new WordNet("", "");
        WordNet synsets1 = new WordNet("", "");
        WordNet synsets1 = new WordNet("", "");


    }
**/
    @Test
    void constructorTest() {

        assertThrows(IllegalArgumentException.class, () -> {
            String syn3_path = "src/test/resources/ProjectI/synsets3.txt";
            String hyper3_path = "src/test/resources/ProjectI/hypernyms3InvalidCycle.txt";
            WordNet synset1 = new WordNet(syn3_path, hyper3_path);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            String syn3_path = "src/test/resources/ProjectI/synsets3.txt";
            String hyper3_path = "src/test/resources/ProjectI/hypernyms3InvalidTwoRoots.txt";
            WordNet synset1 = new WordNet(syn3_path, hyper3_path);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            String syn6_path = "src/test/resources/ProjectI/synsets6.txt";
            String hyper6_path = "src/test/resources/ProjectI/hypernyms6InvalidCycle+Path.txt";
            WordNet synset1 = new WordNet(syn6_path, hyper6_path);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            String syn6_path = "src/test/resources/ProjectI/synsets6.txt";
            String hyper6_path = "src/test/resources/ProjectI/hypernyms6InvalidCycle.txt";
            WordNet synset1 = new WordNet(syn6_path, hyper6_path);
        });


        String synsets = "src/test/resources/ProjectI/synsets.txt";
        String hyppernyms = "src/test/resources/ProjectI/hypernyms.txt";
        WordNet wordnet = new WordNet(synsets, hyppernyms);
        StdOut.println(wordnet.isNoun("ABO_antibodies"));
    }
    @Test
    void nounsTest() {
        String syn6_path = "src/test/resources/ProjectI/synsets6.txt";
        String hyper6_path = "src/test/resources/ProjectI/hypernyms6TwoAncestors.txt";
        WordNet synset6 = new WordNet(syn6_path, hyper6_path);
        Iterator itr = synset6.nouns().iterator();
        assertEquals("a", itr.next());
        assertEquals("b", itr.next());


    }

    @Test
    void isNounTest() {

        String syn6_path = "src/test/resources/ProjectI/synsets6.txt";
        String hyper6_path = "src/test/resources/ProjectI/hypernyms6TwoAncestors.txt";
        WordNet synset6 = new WordNet(syn6_path, hyper6_path);
        assertEquals(true, synset6.isNoun("a"));
        assertEquals(false, synset6.isNoun("g"));
        assertThrows(IllegalArgumentException.class, () -> {
            synset6.isNoun(null);
        });

    }

    @Test
    void distanceTest() {

        String syn6_path = "src/test/resources/ProjectI/synsets6.txt";
        String hyper6_path = "src/test/resources/ProjectI/hypernyms6TwoAncestors.txt";
        WordNet synset6 = new WordNet(syn6_path, hyper6_path);
        assertEquals(3, synset6.distance("a", "d"));
        assertEquals(3, synset6.distance("b", "e"));
        assertThrows(IllegalArgumentException.class, () -> {
            synset6.distance("a", "g");
        });

    }

    @Test
    void sapTest() {

        String syn6_path = "src/test/resources/ProjectI/synsets6.txt";
        String hyper6_path = "src/test/resources/ProjectI/hypernyms6TwoAncestors.txt";
        WordNet synset6 = new WordNet(syn6_path, hyper6_path);
        assertEquals("a", synset6.sap("a", "d"));
        assertEquals("a", synset6.sap("b", "f"));
        assertThrows(IllegalArgumentException.class, () -> {
            synset6.sap("a", "g");
        });


    }

}