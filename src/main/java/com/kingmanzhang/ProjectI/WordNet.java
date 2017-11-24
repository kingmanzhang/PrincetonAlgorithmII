package com.kingmanzhang.ProjectI;
import edu.princeton.cs.algs4.*;
import java.util.TreeMap;


public class WordNet {

    private Digraph wordnet;
    private final TreeMap<String, Integer> synset_names;
    private final String[] synset_names_array;
    private final String[] synset_glosses;


    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {

        In in_synsets = new In(synsets);
        String[] lines = in_synsets.readAllLines();
        int num_synsets = lines.length;
        this.wordnet = new Digraph(num_synsets);
        this.synset_names = new TreeMap<>();
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
                String[] line_elems = line.split(",");
                if (line_elems.length > 1) {
                    int v = Integer.parseInt(line_elems[0]);
                    //StdOut.println("vertice is : " + v);
                    for (int i = 1; i < line_elems.length; i++) { //next could be a non-int
                        int w = Integer.parseInt(line_elems[i]);
                        this.wordnet.addEdge(v, w);
                    }
                }

            }
        }

        if (new DirectedCycle(wordnet).hasCycle() || !oneRootTest()) {
            throw new IllegalArgumentException();
        }

    }

    /**
     * A helper method to validate whether the graph has only one root (outdegree is 0).
     * @return
     */
    private boolean oneRootTest() {
        int rootCount = 0;
        for (int v = 0; v < this.wordnet.V(); v++) {
            if (wordnet.outdegree(v) == 0) {
                rootCount++;
            }
        }
        return rootCount == 1;
    }
    // returns all WordNet nouns
    public Iterable<String> nouns() {

        return this.synset_names.keySet();

    }

    // is the word a WordNet noun?
    public boolean isNoun(String word){

        if (word == null) {
            throw new IllegalArgumentException();
        }

        return this.synset_names.containsKey(word);

    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB){
        if (nounA == null || nounB == null || !isNoun(nounA) || !(isNoun(nounB))) {
            throw new IllegalArgumentException();
        }
        int A = synset_names.get(nounA);
        int B = synset_names.get(nounB);
        SAP sap = new SAP(wordnet);
        return sap.length(A, B);

    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB){
        if (nounA == null || nounB == null || !isNoun(nounA) || !(isNoun(nounB))) {
            throw new IllegalArgumentException();
        }
        int A = synset_names.get(nounA);
        int B = synset_names.get(nounB);
        SAP sap = new SAP(wordnet);
        int value = (sap.ancestor(A, B));
        return this.synset_names_array[value];

    }

    // do unit testing of this class
    public static void main(String[] args){



    }

}
