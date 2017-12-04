package com.kingmanzhang.ProjectIII;

import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;


public class BaseballElimination {

    private int numTeams;
    private LinkedHashMap<String, Integer> teamVertices; //teamVertices vertices
    //private LinkedHashMap<Integer, HashSet<Integer>> gameVertices; //game vertices TODO: may be unnecessary
    private int [] wins;
    private int [] losses;
    private int [] remaining;
    //private int[][] remainingGames; //gameVertices between two teamVertices TODO: is this really necessary?
    private HashMap<HashSet<Integer>, Integer> remainingGames;
    private int verticesFlownetwork; //total vertices in the flownetwork
    private FordFulkerson fordFulkerson; //perform fordFulkerson analysis of the flowNetwork
    private double MAXPOINTSREMAINING = 0.0; //points for unplayed games among non-interested teams
    private ArrayList<String> R = new ArrayList<>(); //subset of teams that eliminate a query team
    private boolean[] isChecked;

    public BaseballElimination(String filename) { // create a baseball division from given filename in format specified below
        if (filename == null) {
            throw new IllegalArgumentException();
        }

        In in = new In(filename);
        if (in.hasNextLine()) {
            this.numTeams = Integer.parseInt(in.readLine()); //read first line: num of teamVertices
            this.wins = new int[numTeams];
            this.losses = new int[numTeams];
            this.remaining = new int[numTeams];
            //this.remainingGames = new int[numTeams][numTeams];
            this.remainingGames = new HashMap<>();
            this.teamVertices = new LinkedHashMap<>();
            //this.gameVertices = new LinkedHashMap<>();
            this.isChecked = new boolean[numTeams];
            Arrays.fill(isChecked, Boolean.FALSE);
        }

        Scanner scnr;
        int i = 0; //which line
        while (in.hasNextLine()) {
            String line = in.readLine();
            scnr = new Scanner(line); // parse each line
            String name = scnr.next();
            this.teamVertices.put(name, i); //put <name, i> to teamVertices
            this.wins[i] = scnr.nextInt();
            this.losses[i] = scnr.nextInt();
            this.remaining[i] = scnr.nextInt();
            int j = 0;
            while(scnr.hasNextInt()) {
                //this.remainingGames[i][j++] = scnr.nextInt(); //TODO: no need to do this for every line
                int toPlay = scnr.nextInt();
                HashSet<Integer> set = new HashSet<>();
                set.add(i);
                set.add(j++);
                if (!remainingGames.containsKey(set)) {
                    remainingGames.put(set, toPlay);
                }
            }
            i++;
        }


        /**
//print out input file
        StdOut.printf("numTeams: %d \n ", this.numTeams);
        StdOut.println("vertice\tname\twins\tloss\tleft\tremaining games");
        for (String team : teamVertices.keySet()) {
            StdOut.printf("%d\t%s\t%d\t%d\t%d\t", teamVertices.get(team), team, wins[teamVertices.get(team)], losses[teamVertices.get(team)], remaining[teamVertices.get(team)]);
            for (i = 0; i < this.numTeams; i++) {
                StdOut.print(remainingGames[this.teamVertices.get(team)][i]);
                StdOut.print("\t");
            }
            StdOut.println("");
        }
 //print out game vertices
        StdOut.println("verticeNum\tteam1\tteam2\t");
        for (HashSet<Integer> set : gameVertices.keySet()) {
            StdOut.print(gameVertices.get(set) + "\t");
            Iterator<Integer> itr = set.iterator();
            while(itr.hasNext()) {
                StdOut.print(itr.next() + "\t");
            }
            StdOut.println("");
        }
         **/

    }

    /**
     * Return number of teams
     */
    public int numberOfTeams() {// number of teamVertices

        return this.numTeams;
    }

    /**
     * Return all teams
     * @return
     */
    public Iterable<String> teams() {// all teamVertices

        return teamVertices.keySet();
    }

    /**
     * A private method to check whether argument is a valid team.
     */
    private void validateTeam(String team) {

        if (!teamVertices.containsKey(team)) {
            throw new IllegalArgumentException();
        }

    }

    /**
     * Return number of wins by query team
     */
    public int wins(String team) {// number of wins for given team

        validateTeam(team);
        return wins[teamVertices.get(team)];
    }

    /**
     * Return number of losses by query team
     */
    public int losses(String team) {// number of losses for given team

        validateTeam(team);
        return losses[teamVertices.get(team)];
    }

    /**
     * Return number of games to play by query team
     */
    public int remaining(String team) {// number of remaining gameVertices for given team

        validateTeam(team);
        return remaining[teamVertices.get(team)];
    }

    /**
     * Return number of games to be played between two teams
     */

    public int against(String team1, String team2) {// number of remaining gameVertices between team1 and team2

        validateTeam(team1);
        validateTeam(team2);
        HashSet<Integer> set = new HashSet<>();
        set.add(teamVertices.get(team1));
        set.add(teamVertices.get(team2));
        return this.remainingGames.get(set);
        //return this.remainingGames[this.teamVertices.get(team1)][this.teamVertices.get(team2)];
    }

    /**
     * Test whether query team can be trivially eliminated (there exists at least one team that has more wins than query
     * team can theorically win (by winning all unplayed games)
     */
    private void trivialElimination(String team) {

        int max = wins(team) + remaining(team);
        for (String other : teams()) { //if any team already has more wins, then query team is eliminated
            if (!other.equals(team) && wins(other) > max) {
                R.add(other);
            }
        }
    }

    /**
     * If a query team cannot be trivally eliminated, then use Ford-Fulkerson method to determine whether it is
     * eliminated.
     */
    private void nontrivialElimination(String team) {

        //if there are 5 teams, the number of vertices in the flow network should be
        //5 (1 is not used) + 4 * 3 / 2 (game vertices) + 1 (source) + 1 (target)
        this.verticesFlownetwork = this.numTeams + (this.numTeams - 1) * (this.numTeams - 2) / 2 + 2;
        int v = teamVertices.get(team); //this vertice will not be used in the flow network
        //create a flow network. Last vertice is target (verticeFlownetwork - 1), second last is source (verticeFlownetwork - 2)
        FlowNetwork flowNetwork = new FlowNetwork(this.verticesFlownetwork);

        //create edges from team vertices to target
        for (int i = 0; i < numTeams; i++) {
            if (i != v) {
                FlowEdge flowEdge = new FlowEdge(i, verticesFlownetwork - 1, wins[v] + remaining[v] - wins[i]); //target vertice set to last of flow network
                flowNetwork.addEdge(flowEdge);
            }
        }

        //handle game vertices
        int iGameVertices = this.numTeams;
        for (int i = 0; i < this.numTeams - 1; i++) {
            for (int j = i + 1; j < this.numTeams; j++) {
                if (i != v && j != v) {
                    HashSet<Integer> pair = new HashSet<>();
                    pair.add(i);
                    pair.add(j);
                    //MAXPOINTSREMAINING += remainingGames[i][j];
                    MAXPOINTSREMAINING += remainingGames.get(pair);
                    //FlowEdge source_game_edge = new FlowEdge(verticesFlownetwork - 2, iGameVertices, remainingGames[i][j]);
                    FlowEdge source_game_edge = new FlowEdge(verticesFlownetwork - 2, iGameVertices, remainingGames.get(pair));
                    FlowEdge game_team1_edge = new FlowEdge(iGameVertices, i, Double.MAX_VALUE);
                    FlowEdge game_team2_edge = new FlowEdge(iGameVertices, j, Double.MAX_VALUE);
                    flowNetwork.addEdge(source_game_edge);
                    flowNetwork.addEdge(game_team1_edge);
                    flowNetwork.addEdge(game_team2_edge);
                    iGameVertices++;
                }
            }
        }
        this.fordFulkerson = new FordFulkerson(flowNetwork, verticesFlownetwork - 2, verticesFlownetwork - 1);
        for (String other : teams()) {
            if (!other.equals(team) && this.fordFulkerson.inCut(teamVertices.get(other))) {
                this.R.add(other);
            }
        }
    }

    /**
     * Test whether a query team is mathematically eliminated.
     */
    public boolean isEliminated(String team) { // is given team eliminated?

        validateTeam(team);
        if (!this.R.isEmpty()) { //initialize the subset of R
            this.R = new ArrayList<>();
        }
        this.MAXPOINTSREMAINING = 0.0;
        this.isChecked[teamVertices.get(team)] = true;
        //first check whether it is a trivial elimination
        trivialElimination(team);
        if (!this.R.isEmpty()) {
            return true;
        }
        //if not trivially eliminated, test nontrivial elimination
        nontrivialElimination(team);
        //if max flow is the same as unplayed points, then x is not eliminated
        return !(Math.abs(this.fordFulkerson.value() - MAXPOINTSREMAINING) < 0.001);
    }

    /**
     * Return subset of teams that eliminate query team
     */
    public Iterable<String> certificateOfElimination(String team) {// subset R of teamVertices that eliminates given team; null if not eliminated

        validateTeam(team);
        if (!isChecked[teamVertices.get(team)]) {
            isEliminated(team);
        }
        if (this.R.isEmpty()) {
            return null;
        } else {
            return new ArrayList<>(this.R);
        }
    }

    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            }
            else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }

}
