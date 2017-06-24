import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Reduction{
    Kattio io;
    // Totala antalet hörn
    int v;

    int v1;
    int v2;
    int e;

    int maxFlow;

    List<Edge> edges;
    List<Edge> flowEdges;
    List<Edge> matchSolutionEdges;

    int s;
    int t;

    public int nrOfVertecies(){
        return v;
    }

    public int nrOfEdges(){
        return e;
    }

    public List<Edge> listOfEdges(){
        return edges;
    }

    public int returnStart(){
        return s;
    }

    public int returnEnd(){
        return t;
    }

    public void readMatchGraph(){
        v1 = io.getInt();
        v2 = io.getInt();
        e = io.getInt();

        edges = new ArrayList<Edge>(e);

        for(int i = 0; i < e; i++){
            int x = io.getInt();
            int y = io.getInt();

            edges.add(new Edge(x, y));
        }
    }

    public void matchToFlow(){
        /*
            För att omvandla en bipartit graf till en flödesgraf så lägger 
            man till två noder utöver de för den bipartita grafen och kopplar 
            ena noden till alla hörn på ena sidan av den bipartita grafen och
            den andra noden till alla hörn på den andra sidan av den bipartita
            grafen.
        */

        v = v1 + v2 + 2;

        // Källa och utlopp
        s = v-1;
        t = v;

        // Lägger till kanter från nod v-1 till alla vänsterhörn
        for(int i = 0; i < v1; i++){
            edges.add(new Edge(v-1, i+1));
        }

        // Lägger till kanter från nod v till alla högerhörn
        for(int i = v1; i < v1+v2; i++){
            edges.add(new Edge(i+1, v));
        }       

        for(Edge e: edges){
            e.capacity = 1;
            //System.out.println("x: " + e.x + " y: " + e.y + " c: " + e.capacity);
        }

        // Ser till att antalet kanter blir rätt
        e = edges.size();
    }

    /*
    public String writeFlowGraphAsString(){
        int capacity = 1;
        StringBuilder stringBuilder = new StringBuilder();
        // Antalet hörn
        stringBuilder.append(v + "\n");
        // Källa och utlopp
        s = v-1;
        t = v;
        stringBuilder.append(s + " " + t + "\n");
        //Antalet kanter
        stringBuilder.append(e + "\n");

        // Kanterna med kantkapaciteten, som alltid är 1 enligt teoriuppgift 3b.
        for (int i = 0; i < e; i++) {
            stringBuilder.append(edges.get(i).getX() + " " + edges.get(i).getY() + " " + capacity + "\n");
        }

        return stringBuilder.toString();
    }*/

    public void writeFlowGraph(){
        int capacity = 1;
        // Antalet hörn
        io.println(v + "");
        // Källa och utlopp
        s = v-1;
        t = v;
        io.println(s + " " + t);
        // Antalet kanter
        io.println(e + "");
        // Kanterna med kantkapaciteten, som alltid är 1 enligt teoriuppgift 3b.
        for (int i = 0; i < e; i++) {
            io.println(edges.get(i).getX() + " " + edges.get(i).getY() + " " + capacity);
        }

        io.flush();
    }

    // Den svarta lådan löser flödesproblemet

    public void readFlowSolution(){
        int flowV = io.getInt();
        s = io.getInt();
        t = io.getInt();
        maxFlow = io.getInt();
        e = io.getInt();

        flowEdges = new ArrayList<Edge>(e);

        for (int i = 0; i < e; i++){
            int x = io.getInt();
            int y = io.getInt();
            int f = io.getInt();

            flowEdges.add(new Edge(x, y, f));
        }

        io.flush();
    }

    public void flowSolutionToMatch(List<Edge> path, int maxFlow){
        this.maxFlow = maxFlow;
        matchSolutionEdges = new ArrayList<Edge>();

        for(Edge edge: path){
            if(!(edge.contains(s, t))){
                matchSolutionEdges.add(edge);
            }
        }

        e = matchSolutionEdges.size();
    }

    public void flowSolutionToMatch(){
        // Vi har redan antalet hörn, så det enda vi behöver är antalet kanter
        // Vi vill ha alla kanter som inte innehåller källan och utloppet
        matchSolutionEdges = new ArrayList<Edge>();

        for(Edge edge: flowEdges){
            if(!(edge.contains(s, t))){
                matchSolutionEdges.add(edge);
            }
        }

        e = matchSolutionEdges.size();
    }

    public void writeMatchSolution(){
        // Ska vara som första raden i matchInput
        io.println(v1 + " " + v2);
        // Antalet kanter
        io.println(maxFlow + "");
        // Alla kanter i matchningen
        for (int i = 0; i < maxFlow; i++){
            matchSolutionEdges.get(i).printEdge(io);
        }

        io.flush();
        io.close();
    }

    Reduction(){
        io = new Kattio(System.in, System.out);
    }

    /* Den gamla konstruktorn för steg 1
    Reduction(){
        io = new Kattio(System.in, System.out);

        readMatchGraph();
        matchToFlow();
        writeFlowGraph();
        // Den svarta lådan löser problemet
        readFlowSolution();
        flowSolutionToMatch();
        writeMatchSolution();

        io.close();
    }*/
}