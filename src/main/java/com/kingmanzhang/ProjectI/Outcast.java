package com.kingmanzhang.ProjectI;

public class Outcast {
    private final WordNet wordnet;

    public Outcast (WordNet wordnet) {

        this.wordnet = wordnet;
    }

    /**
     * Return the outcast among a set of nouns in a wordset
     */
    public String outcast(String[] nouns) {

        int[] d = new int[nouns.length];
        int[][] dist = new int[nouns.length][nouns.length];
        for (int i = 0; i < nouns.length; i++) {
            d[i] = 0;
            for (int j = 0; j < nouns.length; j++) {
                dist[i][j] = 0;
            }
        }

        for (int i = 0; i < nouns.length; i++) {
            for (int j = 0; j < i; j++) {
                dist[i][j] = dist[j][i] = this.wordnet.distance(nouns[i], nouns[j]);
            }
        }

        int outcast_i = 0;
        int outcast_dist = Integer.MIN_VALUE;
        for (int i = 0; i < nouns.length; i++) {
            for (int j = 0; j < nouns.length; j++) {
                d[i] += dist[i][j];
            }
            if (d[i] > outcast_dist) {
                outcast_i = i;
                outcast_dist = d[i];
            }
        }
        return nouns[outcast_i];
    }
    
}
