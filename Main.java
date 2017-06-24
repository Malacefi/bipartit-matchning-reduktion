/* 
	@author: Emelie Eriksson
	Indata: En bipartit graf

	Uppgift: Att producera en maximal matchning.
	För att lösa uppgiften ska vi reducera maximal matchning till flödesproblemet. Vi ska lösa 
	maximal matchning med flödesproblemet.

	Steg 1: Reducera problemet till flödesproblemet
	Steg 2: Gör din egna 'svarta låda' (lös flödesproblemet)
	Steg 3: Kombinera reduceringen med din lösning på flödesproblemet 
*/


import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {

	static Reduction reduction;
	static MaxFlow maxFlow;

	public static void main(String args[]) throws IOException {
		reduction = new Reduction();

		reduction.readMatchGraph();
		reduction.matchToFlow();
		//Min egna svarta låda
		maxFlow = new MaxFlow(reduction.nrOfVertecies(), reduction.nrOfEdges(), 
			reduction.returnStart(), reduction.returnEnd(), reduction.listOfEdges());
		//Läser resultatet från 'svarta lådan'
		reduction.flowSolutionToMatch(maxFlow.edgesWithFlow(), maxFlow.returnMaxFlow());
		reduction.writeMatchSolution();
	}
}