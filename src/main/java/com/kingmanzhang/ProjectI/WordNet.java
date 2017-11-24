package com.kingmanzhang.ProjectI;
import edu.princeton.cs.algs4.*;
import java.util.HashMap;
import java.util.HashSet;


public class WordNet {

    private Digraph wordnet;
    private final HashMap<String, HashSet<Integer>> synset_names;
    private final String[] synset_names_array;
    private final String[] synset_glosses;
    private final SAP sap;

    /**
     * Constructor a wordnet with two files, a file for synsets and a file for hypernyms
     */

    public WordNet(String synsets, String hypernyms) {

        In in_synsets = new In(synsets);
        String[] lines = in_synsets.readAllLines();
        int num_synsets = lines.length;
        this.wordnet = new Digraph(num_synsets);
        this.synset_names = new HashMap<>();
        this.synset_names_array = new String[num_synsets];
        this.synset_glosses = new String[num_synsets];
        for (int i = 0; i < num_synsets; i++) {
            String[] line_elements = lines[i].split(",");

            for (String noun : line_elements[1].split(" ")) {
                if(this.synset_names.containsKey(noun)) {
                    this.synset_names.get(noun).add(Integer.parseInt(line_elements[0]));
                } else {
                    HashSet<Integer> set = new HashSet<>();
                    set.add(Integer.parseInt(line_elements[0]));
                    this.synset_names.put(noun, set);
                }
            }
            this.synset_names_array[Integer.parseInt(line_elements[0])] = line_elements[1];
            this.synset_glosses[Integer.parseInt(line_elements[0])] = line_elements[2];
        }

        In in_hypernyms = new In(hypernyms);
        while (in_hypernyms.hasNextLine()) {
            String line = in_hypernyms.readLine();

            if (!line.isEmpty()) {
                String[] line_elems = line.split(",");
                if (line_elems.length > 1) {
                    int v = Integer.parseInt(line_elems[0]);
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

        this.sap = new SAP(this.wordnet);

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

    /**
     * Returns all WordNet nouns
     */
    public Iterable<String> nouns() {

        return new HashSet<>(this.synset_names.keySet());

    }

    /**
     * Test whether a  word a WordNet noun.
     */
    public boolean isNoun(String word){

        if (word == null) {
            throw new IllegalArgumentException();
        }

        return this.synset_names.containsKey(word);

    }

    /**
     * Return the distance between nounA and nounB
     */
    public int distance(String nounA, String nounB){
        if (nounA == null || nounB == null || !isNoun(nounA) || !(isNoun(nounB))) {
            throw new IllegalArgumentException();
        }

        return sap.length(synset_names.get(nounA), synset_names.get(nounB));

    }

    /**
     * Return a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
     */
    public String sap(String nounA, String nounB){
        if (nounA == null || nounB == null || !isNoun(nounA) || !(isNoun(nounB))) {
            throw new IllegalArgumentException();
        }

        int value = (sap.ancestor(synset_names.get(nounA), synset_names.get(nounB)));
        return this.synset_names_array[value];

    }

}
