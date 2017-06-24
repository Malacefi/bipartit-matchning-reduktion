// @author: Emelie Eriksson

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.LinkedList;
import java.util.Queue;

class Edge{
	Kattio io;

	public int x;
	public int y;
	public int capacity;
	public int restCapacity;
	public int flow;
	int c = 1;

	public Edge invertedEdge;

	public Edge(int x, int y){
		this.x = x;
		this.y = y;
		flow = 0;
	}

	public Edge(int x, int y, int flow){
		this.x = x;
		this.y = y;
		this.flow = flow;
	}

	public Edge(int x, int y, int flow, int capacity){
		this.x = x;
		this.y = y;
		this.flow = flow;
		this.restCapacity = capacity;
		this.capacity = capacity;
	}

	public int getX(){
		return x;
	}

	public int getY(){
		return y;
	}

	public void printEdge(Kattio io){
		this.io = io;
		io.println(x + " " + y);
		io.flush();
	}

	public void printEdgeWithCapacity(Kattio io){
		this.io = io;
		io.println(x + " " + y + " " + c);
		io.flush();
	}

	public boolean contains(int s, int t){
		if(s == x || s == y || t == x || t == y){
			return true;
		}
		else{
			return false;
		}
	}
}