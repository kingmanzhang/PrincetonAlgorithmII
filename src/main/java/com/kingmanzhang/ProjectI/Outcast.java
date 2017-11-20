package com.kingmanzhang.ProjectI;

public class Outcast {
    private WordNet wordnet;

    public Outcast (WordNet wordnet) {
        this.wordnet = wordnet;
    }

    public String outcast(String[] nouns) {
        int [] d = new int[nouns.length];
        int[][] dist = new int[][];
        for (int i = 0; i < nouns.length; i++) {
            dist[i][i] = 0;
            for (int j = 0; j < i; j++) {
                dist[i][j] = dist[j][i] = this.wordnet.distance(nouns[i], nouns[j]);
            }
        }
        for (int i = 0; i < nouns.length; i++) {
            d[i] =
        }
    }

    public static void main(String[] args){

    }
}
