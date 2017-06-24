// @author: Emelie Eriksson

import java.lang.StringBuilder;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.LinkedList;
import java.util.Queue;

class Vertex extends Object {
	public int v;
	public List<Edge> neighbors;
	public boolean visited = false;
	public Edge parentEdge;

	public void addNeighbor(int y, int capacity){
		Edge neighbor = new Edge(v, y, 0, capacity);
		neighbors.add(neighbor);
	}

	public Edge addInvertedNeighbor(int y, int capacity){
		Edge neighbor = new Edge(v, y, 0, capacity);
		neighbors.add(neighbor);
		return neighbor;
	}

	public Edge hasNeighbor(int n){
		for(Edge e: neighbors){
			if(n == e.y){
				return e;
			}
		}
		return null;
	}

	public Vertex(int v){
		this.v = v;
		neighbors = new ArrayList<Edge>();
	}
}

public class MaxFlow {
	Kattio io;

	int v;
	int e;

	List<Vertex> verteces;
	List<Edge> path;

	int minRestCapacity;

	int maxFlow;

	int s;
	int t;

	public int returnMaxFlow(){
		return maxFlow;
	}

	private void readFlowGraphFromString(){
		v = io.getInt();
		s = io.getInt();
		t = io.getInt();
		e = io.getInt();

		verteces = new ArrayList<Vertex>(v + 1);

		// Lägger till ett första element så att man enkelt kan nå de rätta hörnen
		verteces.add(new Vertex(0));

		for(int i = 1; i <= v; i++){
			verteces.add(new Vertex(i));
		}

		for(int j = 0; j < e; j++){
			int x = io.getInt();
			int y = io.getInt();
			int c = io.getInt();
			
			// Lägger till granne
			// Om det redan finns en kant som går åt samma håll lägger man till ytterligare kapacitet
			if(verteces.get(x).hasNeighbor(y) == null){
				verteces.get(x).addNeighbor(y, c);
			}else{
				verteces.get(x).hasNeighbor(y).capacity += c;
			}
			
		}

		// TODO
		for(Vertex v: verteces){
			for (Edge e: v.neighbors){
				int neighbor = e.y;
				if(e.invertedEdge == null){
					if(verteces.get(neighbor).hasNeighbor(v.v) != null){
						e.invertedEdge = verteces.get(neighbor).hasNeighbor(v.v);
					}else{
						Edge inverted = verteces.get(neighbor).addInvertedNeighbor(e.x, 0); // (FIXAT)TIDIAGRE: LÄGGER ALDRIG TILL INVERTED EDGE TILL DEN HÄR
						inverted.invertedEdge = e;
						e.invertedEdge = inverted;
					}
				}
			}
		}
	}

	private void readFlowGraph(){
		v = io.getInt();
		s = io.getInt();
		t = io.getInt();
		e = io.getInt();

		verteces = new ArrayList<Vertex>(v + 1);

		// Lägger till ett första element så att man enkelt kan nå de rätta hörnen
		verteces.add(new Vertex(0));

		for(int i = 1; i <= v; i++){
			verteces.add(new Vertex(i));
		}

		for(int j = 0; j < e; j++){
			int x = io.getInt();
			int y = io.getInt();
			int c = io.getInt();
			
			// Lägger till granne
			// Om det redan finns en kant som går åt samma håll lägger man till ytterligare kapacitet
			if(verteces.get(x).hasNeighbor(y) == null){
				verteces.get(x).addNeighbor(y, c);
			}else{
				//System.out.println("Hej " + test);
				verteces.get(x).hasNeighbor(y).capacity += c;
			}
			
		}

		// TODO
		for(Vertex v: verteces){
			for (Edge e: v.neighbors){
				int neighbor = e.y;
				if(e.invertedEdge == null){
					if(verteces.get(neighbor).hasNeighbor(v.v) != null){
						e.invertedEdge = verteces.get(neighbor).hasNeighbor(v.v);
					}else{
						Edge inverted = verteces.get(neighbor).addInvertedNeighbor(e.x, 0); // (FIXAT)TIDIAGRE: LÄGGER ALDRIG TILL INVERTED EDGE TILL DEN HÄR
						inverted.invertedEdge = e;
						e.invertedEdge = inverted;
					}
				}
			}
		}
	}

	private boolean bfs(){
		LinkedList<Vertex> queue = new LinkedList<Vertex>();
		Vertex root = verteces.get(s);
		queue.add(verteces.get(s));

		for(int i = 0; i < v; i++){
			verteces.get(i+1).visited = false;
		}

		verteces.get(s).visited = true;
		verteces.get(s).parentEdge = new Edge(s, s, 0, 1000);

		while(!queue.isEmpty()){
			//Vertex currentVertex = queue.remove();
			for(Edge e: queue.remove().neighbors){
				//Grannoden
				if(!verteces.get(e.y).visited && e.restCapacity > 0){
					queue.add(verteces.get(e.y));
					verteces.get(e.y).visited = true;
					// Sätter den första noden som hittade n som parent
					verteces.get(e.y).parentEdge = e;
					// När vi har hittat slutet kommer vi ha den kortaste vägen
					// eftersom det är bredden-först sökning.
					if(verteces.get(e.y).v == t){
						return true;
					}		
				}
			}
		}
		// Om man inte hittar sänkan
		return false;
	}

	private void getPath(){
		path = new ArrayList<Edge>();		
		boolean sourceFound = false;
		// Börjar på sänkan, man gör första manuellt för att få ett startvärde på minRestCapacity
		Vertex currentVertex = verteces.get(t);
		path.add(verteces.get(t).parentEdge);
		minRestCapacity = verteces.get(t).parentEdge.restCapacity;
		while(!sourceFound){
			currentVertex = verteces.get(currentVertex.parentEdge.x);
			if(currentVertex == verteces.get(s)){
				sourceFound = true;
				return;
			}
			if(minRestCapacity > currentVertex.parentEdge.restCapacity){
				minRestCapacity = currentVertex.parentEdge.restCapacity;
			}
			path.add(currentVertex.parentEdge);
		}
	}

	private void edmondsKarp(){
		// Hittar den kortaste stigen från källa till sänka
		// Den kommer att ligga i listan path.
		boolean pathExists = bfs();

		while(pathExists){
			getPath();
			maxFlow = maxFlow + minRestCapacity; 
			for(Edge e: path) {
				e.flow = e.flow + minRestCapacity;
				e.invertedEdge.flow = -e.flow;

				e.restCapacity = e.capacity - e.flow;
				e.invertedEdge.restCapacity =  e.invertedEdge.capacity - e.invertedEdge.flow;
			}
			pathExists = bfs();
		}
		
	}

	public List<Edge> edgesWithFlow(){
		List<Edge> edgesWithFlow = new ArrayList<Edge>();
		for(Vertex v: verteces){
			for (Edge e: v.neighbors){
				if(e.flow > 0){
					edgesWithFlow.add(e);
				}
			}
		}
		return edgesWithFlow;
	}

	private void printFlowGraph(){
		io.println(v + "");
		io.println(s + " " + t + " " + maxFlow);

		StringBuilder stringBuilder = new StringBuilder();
		int nrOfEdgesWithFlow = 0;

		for(Vertex v: verteces){
			for (Edge e: v.neighbors){
				if(e.flow > 0){
					stringBuilder.append(e.x + " " + e.y + " " + e.flow + "\n");
					nrOfEdgesWithFlow++;
				}
			}
		}

		io.println(nrOfEdgesWithFlow + "");
		String edgesWithFlow = stringBuilder.toString();
		io.printf(edgesWithFlow);

		io.flush();
	}

	public MaxFlow(int v, int e, int s, int t, List<Edge> edges){
		this.v = v;
		this.s = s;
		this.t = t;
		this.e = e;

		verteces = new ArrayList<Vertex>(v + 1);

		// Lägger till ett första element så att man enkelt kan nå de rätta hörnen
		verteces.add(new Vertex(0));

		for(int i = 1; i <= v; i++){
			verteces.add(new Vertex(i));
		}

		for(int j = 0; j < e; j++){
			int x = edges.get(j).x;
			int y = edges.get(j).y;
			int c = edges.get(j).capacity;
			
			// Lägger till granne
			// Om det redan finns en kant som går åt samma håll lägger man till ytterligare kapacitet
			if(verteces.get(x).hasNeighbor(y) == null){
				verteces.get(x).addNeighbor(y, c);
			}else{
				//System.out.println("Hej " + test);
				verteces.get(x).hasNeighbor(y).capacity += c;
			}
			
		}

		for(Vertex vertex: verteces){
			for (Edge edge: vertex.neighbors){
				int neighbor = edge.y;
				if(edge.invertedEdge == null){
					if(verteces.get(neighbor).hasNeighbor(vertex.v) != null){
						edge.invertedEdge = verteces.get(neighbor).hasNeighbor(vertex.v);
					}else{
						Edge inverted = verteces.get(neighbor).addInvertedNeighbor(edge.x, 0); // (FIXAT)TIDIAGRE: LÄGGER ALDRIG TILL INVERTED EDGE TILL DEN HÄR
						inverted.invertedEdge = edge;
						edge.invertedEdge = inverted;
					}
				}
			}
		}

		/* Testutskrift
		for(Vertex vertex: verteces){
			for (Edge edge: vertex.neighbors){
				System.out.println("vertex: " + vertex.v + "neighbor: " + edge.x + " " + edge.y + " c: " + edge.capacity);
			}
		}*/

		edmondsKarp();
	}

	/* Gamla konstruktorn för steg 2
	public MaxFlow(){
		io = new Kattio(System.in, System.out);

		readFlowGraph();
		edmondsKarp();
		printFlowGraph();

		io.flush();

		io.close();
	}*/
	
}