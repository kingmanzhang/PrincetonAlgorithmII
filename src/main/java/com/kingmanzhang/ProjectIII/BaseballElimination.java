package com.kingmanzhang.ProjectIII;

import edu.princeton.cs.algs4.*;

import java.util.*;

public class BaseballElimination {

    private int numTeams;
    private LinkedHashMap<String, Integer> teamVertices; //teamVertices vertices
    private LinkedHashMap<Integer, HashSet<Integer>> gameVertices; //game vertices TODO: may be unnecessary
    private int [] wins;
    private int [] losses;
    private int [] remaining;
    private int[][] remainingGames; //gameVertices between two teamVertices TODO: is this really necessary?
    private int verticesFlownetwork; //total vertices in the flownetwork
    //private FlowNetwork flowNetwork; //construct flowNetwork
    private FordFulkerson fordFulkerson; //perform fordFulkerson analysis of the flowNetwork
    private double MAXPOINTSREMAINING = 0.0; //points for unplayed games among non-interested teams
    private ArrayList<String> R = new ArrayList<>(); //subset of teams that eliminate a query team


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
            this.remainingGames = new int[numTeams][numTeams];
            this.teamVertices = new LinkedHashMap<>();
            this.gameVertices = new LinkedHashMap<>();
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
                this.remainingGames[i][j++] = scnr.nextInt(); //TODO: no need to do this for every line
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

    public int numberOfTeams() {// number of teamVertices

        return this.numTeams;
    }

    public Iterable<String> teams() {// all teamVertices

        return teamVertices.keySet();
    }

    private void validateTeam(String team) {

        if (!teamVertices.containsKey(team)) {
            throw new IllegalArgumentException();
        }

    }
    public int wins(String team) {// number of wins for given team

        validateTeam(team);
        return wins[teamVertices.get(team)];
    }

    public int losses(String team) {// number of losses for given team

        validateTeam(team);
        return losses[teamVertices.get(team)];
    }

    public int remaining(String team) {// number of remaining gameVertices for given team

        validateTeam(team);
        return remaining[teamVertices.get(team)];
    }

    public int against(String team1, String team2) {// number of remaining gameVertices between team1 and team2

        validateTeam(team1);
        validateTeam(team2);
        return this.remainingGames[this.teamVertices.get(team1)][this.teamVertices.get(team2)];

        /**
         //this will be very efficient, but maybe implement a simpler solution
        HashSet<Integer> teamPair = new HashSet<>();
        teamPair.add(teamVertices.get(team1));
        teamPair.add(teamVertices.get(team2));
        return gameVertices.get(teamPair);
         **/

    }

    private void trivialElimination(String team) {

        int max = wins(team) + remaining(team);
 //StdOut.println("Enter trivial Elimination test. max win for query team is " + max);
        for (String other : teams()) { //if any team already has more wins, then query team is eliminated
            if (!other.equals(team) && wins(other) > max) {
 //StdOut.println("Team " + other + " already has more wins than query team");
                R.add(other);
            }
        }
    }

    private void nontrivialElimination(String team) {

//StdOut.println("enter nontrivial test. this.R should be empty. Is it empty: " + this.R.isEmpty());
        //if there are 5 teams, the number of vertices in the flow network should be
        //5 (1 is not used) + 4 * 3 / 2 (game vertices) + 1 (source) + 1 (target)
        this.verticesFlownetwork = this.numTeams + (this.numTeams - 1) * (this.numTeams - 2) / 2 + 2;
        int v = teamVertices.get(team); //this vertice will not be used in the flow network
        //create a flow network. Last vertice is target (verticeFlownetwork - 1), second last is source (verticeFlownetwork - 2)
        FlowNetwork flowNetwork = new FlowNetwork(this.verticesFlownetwork);

        /**
        for (String other : teams()) { //edges from team vertices to sink (last of flowNetwork)
            if (!other.equals(team)) {
                FlowEdge flowEdge = new FlowEdge(teamVertices.get(other), verticesFlownetwork - 1, wins(team) + remaining(team) - wins(other));
                flowNetwork.addEdge(flowEdge);
            }
        }
        **/

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
  //StdOut.printf("game pair: [%d, %d]", i, j);
  //in case I need to see what is in game vertices
  this.gameVertices.put(iGameVertices, pair);
                    MAXPOINTSREMAINING += remainingGames[i][j];
                    FlowEdge source_game_edge = new FlowEdge(verticesFlownetwork - 2, iGameVertices, remainingGames[i][j]);
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
 //StdOut.println("Exiting nontrivial test. Is this.R empty now: " + this.R.isEmpty());
    }

    public boolean isEliminated(String team) { // is given team eliminated?

        validateTeam(team);
        if (!this.R.isEmpty()) { //initialize the subset of R
            this.R = new ArrayList<>();
        }
        this.MAXPOINTSREMAINING = 0.0;
        //first check whether it is a trivial elimination
        trivialElimination(team);
        if (!this.R.isEmpty()) {
            return true;
        }
        //if not trivially eliminated, test nontrivial elimination
        nontrivialElimination(team);
        //if max flow is the same as unplayed points, then x is not eliminated
 //StdOut.printf("Max flow: %f, MAX POINTS Remaining: %f \n", this.fordFulkerson.value(), MAXPOINTSREMAINING);
        return !(Math.abs(this.fordFulkerson.value() - MAXPOINTSREMAINING) < 0.001);
    }

    public Iterable<String> certificateOfElimination(String team) {// subset R of teamVertices that eliminates given team; null if not eliminated

        validateTeam(team);
        return this.R;

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
