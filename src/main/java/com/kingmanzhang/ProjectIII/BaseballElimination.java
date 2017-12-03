package com.kingmanzhang.ProjectIII;

import edu.princeton.cs.algs4.*;

import java.util.*;

public class BaseballElimination {

    private int numTeams;
    private HashMap<String, Integer> teamVertices; //teamVertices vertices
    private HashMap<HashSet<Integer>, Integer> gameVertices; //game vertices
    private int [] wins;
    private int [] losses;
    private int [] remaining;
    private int[][] remainingGames; //gameVertices between two teamVertices TODO: is this really necessary?
    private int verticesFlownetwork; //total vertices in the flownetwork
    //private FlowNetwork flowNetwork; //construct flowNetwork
    private FordFulkerson fordFulkerson; //perform fordFulkerson analysis of the flowNetwork


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
            this.teamVertices = new HashMap<>();
            this.gameVertices = new HashMap<>();
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

    public int wins(String team) {// number of wins for given team

        return wins[teamVertices.get(team)];
    }

    public int losses(String team) {// number of losses for given team

        return losses[teamVertices.get(team)];
    }

    public int remaining(String team) {// number of remaining gameVertices for given team

        return remaining[teamVertices.get(team)];
    }

    public int against(String team1, String team2) {// number of remaining gameVertices between team1 and team2

        return this.remainingGames[this.teamVertices.get(team1)][this.teamVertices.get(team2)];

        /**
         //this will be very efficient, but maybe implement a simpler solution
        HashSet<Integer> teamPair = new HashSet<>();
        teamPair.add(teamVertices.get(team1));
        teamPair.add(teamVertices.get(team2));
        return gameVertices.get(teamPair);
         **/

    }

    private ArrayList<String> trivialElimination(String team) {

        ArrayList<String> teamsWithMoreWins = new ArrayList<>();
        int max = wins(team) + remaining(team);
        for (String other : teams()) { //if any team already has more wins, then query team is eliminated
            if (!other.equals(team) && wins(other) > max) {
                teamsWithMoreWins.add(other);
            }
        }
        return teamsWithMoreWins;

    }

    private void nontrivialElimination(String team) {
        int v = teamVertices.get(team);
        int startGameVertices = this.numTeams - 1;
        for (int i = 0; i < this.numTeams - 1; i++) {
            for (int j = i + 1; j < this.numTeams; j++) {
                if (i != v && j != v) {
                    HashSet<Integer> pair = new HashSet<>();
                    pair.add(i);
                    pair.add(j);
 StdOut.printf("game pair: [%d, %d]", i, j);
                    this.gameVertices.put(pair, startGameVertices++);
                }

            }
        }
        this.verticesFlownetwork = this.teamVertices.size() - 1 + this.gameVertices.size() + 2;
 StdOut.println("num of vertices in flow network: " + this.verticesFlownetwork);

        FlowNetwork flowNetwork = new FlowNetwork(this.verticesFlownetwork);

        /**
        for (String other : teams()) { //edges from team vertices to sink (last of flowNetwork)
            if (!other.equals(team)) {
                FlowEdge flowEdge = new FlowEdge(teamVertices.get(other), verticesFlownetwork - 1, wins(team) + remaining(team) - wins(other));
                flowNetwork.addEdge(flowEdge);
            }
        }
        **/
        for (int i = 0; i < numTeams; i++) {
            if (i != v) {
                FlowEdge flowEdge = new FlowEdge(i, verticesFlownetwork - 1, wins[v] + losses[v] - wins[i]);
                flowNetwork.addEdge(flowEdge);
            }
        }
        for (HashSet<Integer> set : gameVertices.keySet()) {
            //for each game vertice, first add an edge from source to game vertice
            Iterator<Integer> itr = set.iterator(); //each set has two numbers
            int vTeam1 = itr.next();
            int vTeam2 = itr.next();
            if (vTeam1 != v && vTeam2 != v) {
                int vGame = gameVertices.get(set);
                FlowEdge source_game_edge = new FlowEdge(verticesFlownetwork - 2, vGame, remainingGames[vTeam1][vTeam2]);
                flowNetwork.addEdge(source_game_edge);
                FlowEdge game_team1_edge = new FlowEdge(vGame, vTeam1, Double.MAX_VALUE);
                FlowEdge game_team2_edge = new FlowEdge(vGame, vTeam2, Double.MAX_VALUE);
                flowNetwork.addEdge(game_team1_edge);
                flowNetwork.addEdge(game_team2_edge);
            }
        }
        this.fordFulkerson = new FordFulkerson(flowNetwork, verticesFlownetwork - 2, verticesFlownetwork - 1);
    }

    public boolean isEliminated(String team) { // is given team eliminated?

        //first check whether it is a trivial elimination
        if (!trivialElimination(team).isEmpty()) {
            return true;
        }
        //if not trivially eliminated, test nontrivial elimination
        return this.fordFulkerson.inCut(verticesFlownetwork - 2);
    }

    public Iterable<String> certificateOfElimination(String team) {// subset R of teamVertices that eliminates given team; null if not eliminated

        if (!trivialElimination(team).isEmpty()) {
            return trivialElimination(team);
        }

        ArrayList<String> targetTeams = new ArrayList<>();
        for (String other : teams()) {
            if (!other.equals(team) && fordFulkerson.inCut(teamVertices.get(other))) {
                targetTeams.add(other);
            }
        }
        return targetTeams;
    }

}
