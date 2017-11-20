package com.kingmanzhang.ProjectI;

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.BST;
import java.util.Scanner;

public class WordNet {

    private Digraph wordnet;
    private final BST<String, Integer> synset_names;     //TODO: final?
    private final String[] synset_names_array;
    private final String[] synset_glosses;   //TODO: final?
/**
    private class Synset {
        int synset_id;
        String synset_name;
        String synset_gloss;

        public Synset(int synset_id, String synset_name, String synset_gloss) {
            this.synset_id = synset_id;
            this.synset_name = synset_name;
            this.synset_gloss = synset_gloss;
        }
    }
 **/

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {

        In in_synsets = new In(synsets);
        String[] lines = in_synsets.readAllLines();
        int num_synsets = lines.length;
        this.synset_names = new BST<>();
        this.synset_names_array = new String[num_synsets];
        this.synset_glosses = new String[num_synsets];
        for (int i = 0; i < num_synsets; i++) {
            String[] line_elements = lines[i].split(",");
            this.synset_names.put(line_elements[1], Integer.parseInt(line_elements[0]));
            this.synset_names_array[i] = line_elements[1];
            this.synset_glosses[i] = line_elements[2];
        }

        In in_hypernyms = new In(hypernyms);
        while (in_hypernyms.hasNextLine()) {
            String line = in_hypernyms.readLine();
            if (!line.isEmpty()) {
                Scanner scnr = new Scanner(line);
                int v = scnr.nextInt();
                while (scnr.hasNextInt()) { //next could be a non-int
                    this.wordnet.addEdge(v, scnr.nextInt());
                }
            }
        }

    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {

        return this.synset_names.keys();

    }

    // is the word a WordNet noun?
    public boolean isNoun(String word){

        return this.synset_names.contains(word);

    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB){
        int A = synset_names.get(nounA);
        int B = synset_names.get(nounB);
        SAP sap = new SAP(wordnet);
        return sap.length(A, B);

    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB){
        int A = synset_names.get(nounA);
        int B = synset_names.get(nounB);
        SAP sap = new SAP(wordnet);
        int value = (sap.ancestor(A, B));
        return this.synset_names_array[value];

    }

    // do unit testing of this class
    public static void main(String[] args){

        System.out.println("Hello, wordnet");

    }

}
