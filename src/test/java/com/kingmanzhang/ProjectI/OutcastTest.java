package com.kingmanzhang.ProjectI;

import edu.princeton.cs.algs4.Out;
import edu.princeton.cs.algs4.StdOut;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OutcastTest {
    @Test
    void outcast() {

        String syn6_path = "src/test/resources/ProjectI/synsets6.txt";
        String hyper6_path = "src/test/resources/ProjectI/hypernyms6TwoAncestors.txt";
        WordNet synset6 = new WordNet(syn6_path, hyper6_path);
        Outcast outcast6 = new Outcast(synset6);
        String[] test = new String[] {"b", "c", "f"};
        assertEquals("f", outcast6.outcast(test));

        String synset = "src/test/resources/ProjectI/synsets.txt";
        String hypernym = "src/test/resources/ProjectI/hypernyms.txt";
        WordNet wordnet = new WordNet(synset, hypernym);
        Outcast outcast = new Outcast(wordnet);
        String[] teststr = {"horse", "zebra", "cat", "bear", "table"};
        for (String str : teststr) {
            StdOut.println(str + ": " + wordnet.isNoun(str));
        }

        assertEquals(true, wordnet.isNoun("horse"));
        assertEquals(true, wordnet.isNoun("zebra"));
        assertEquals(true, wordnet.isNoun("cat"));
        assertEquals(true, wordnet.isNoun("bear"));
        assertEquals(true, wordnet.isNoun("table"));
        String[] strs = new String[] {"horse", "zebra", "cat", "bear", "table"};
        assertEquals("table", outcast.outcast(strs));



    }

}