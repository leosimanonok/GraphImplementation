package Project2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.zip.GZIPInputStream;

/*
 * This graph implementation was written by Leo Simanonok
 * The graph object houses an array of vertices. The vertices
 * have store a number of different attributes, primarily all of their edges
 * and their key.
 */
public class Graph {
	Vertex vertices[];		
	
	/*
	 * The Vertex class stores all of the info of the graph and provides most of the functionality
	 */
	static class Vertex {
		int key;
		int dist;
		int prev;
		int edges[][];
		int numEdges;
		
		/*
		 * Contructor method of the Vertex class which takes in a key as a paramter and initializes the number of 
		 * edges the vertex has to 0.
		 */
		public Vertex(int key) {
			this.key = key;
			numEdges = 0;
			
		}
		
		/*
		 * Method which addes an edge to a vertex. The function takes in the end vertex and the
		 * weight of the path and adds it to the array of arrays which houses all the edges the 
		 * vertex has.
		 * @param endVertex - the key of the endVertex
		 * @param weight - the weight of the path
		 */
		public void addEdge(int endVertex, int weight) {
			
			int newEdge[] = new int[] {endVertex, weight};
			if (this.numEdges == 0) {
				
				/*
				 * If the vertex has no edges, create the initial array of arrays and add the new edge
				 */
				this.edges = new int[1][2];
				this.edges[0] = newEdge;
				
			} else if (this.edges[numEdges-1] != null) {
				/*
				 * Check if the array holding the edges is full, if it is, double the size of the array, copy the old elements, and add the new edge to the new array
				 */
				int temp[][] = new int[numEdges * 2][2];
				for(int i = 0; i < numEdges; i++) { // copying the data from the old array to a new array
					for(int j = 0; j < 2; j++) {
						temp[i][j] = edges[i][j];
					}
				}
				temp[numEdges] = newEdge;
				this.edges = temp;
				
			} else {
				/*
				 * If the array holding the edges is not full, just add the edge to the end of the array
				 */
				this.edges[numEdges] = newEdge;
				
			}
			numEdges++;
			
		}
	}
	
	/*
	 * Method which takes in a .gz file and uses the data to map all
	 * the vertices and edges in the graph
	 * @param fileName - the name of the .gz file
	 */
	public Graph populateGraph(String fileName) throws IOException {
		
		Graph G = new Graph();
		String lineSplit[];
		File file = new File(fileName);
		BufferedReader br = new BufferedReader(new InputStreamReader(new GZIPInputStream(new FileInputStream(file)))); // used a buffered reader to read from the .gz file
		String line = br.readLine();
		
		while(line != null) {
			
			if (line.substring(0, 1).equals("p")) {
				/*
				 * The line starting with "p" contains information about the number of vertices in the graph
				 */
				lineSplit = line.split(" ");
				String numVerticesStr = lineSplit[2];
				int numVertices = Integer.parseInt(numVerticesStr);
				G.vertices = new Vertex[numVertices]; //initializing the size of the array of vertices to the number of vertices in the graph
				
			} else if (line.substring(0, 1).equals("a")) {
				/*
				 * Lines staring with "a" contain the information of an edge
				 */
				lineSplit = line.split(" ");
				
				String startNodeStr = lineSplit[1];
				String endNodeStr = lineSplit[2];
				String weightStr = lineSplit[3];
				int startNode = Integer.parseInt(startNodeStr) - 1; // array starts at 0, but keys of vertices start at 1
				int endNode = Integer.parseInt(endNodeStr) - 1;
				int weight = Integer.parseInt(weightStr);
				
				if (G.vertices[startNode] == null) {
					/*
					 * the startNode is always going to be the key of the vertex we want. We check that index in the 
					 * graph's list of vertices, if it doesnt exist, we create a new vertex with that key and place it in the
					 * array at that index
					 */
					Vertex newVertex = new Vertex(startNode);
					G.vertices[startNode] = newVertex;
				}
				G.vertices[startNode].addEdge(endNode, weight);
				
				/* Testing
				System.out.println("Line: " + line);
				System.out.print("Start: " + G.vertices[startNode].key);
				int length = G.vertices[startNode].edges.length;
				System.out.print("  End: " + G.vertices[startNode].edges[length-1][0]);
				System.out.println("  Weight: " + G.vertices[startNode].edges[length-1][1]);
				*/
			}
			line = br.readLine();
			
		}
		br.close();
		return G;
	}
	
	public static void main(String[] args) throws IOException {
		String fileName;
		fileName = "src/Project2/USA-road-t.NY.gr.gz";
		
		Graph test = new Graph();
		test.populateGraph(fileName);
	}
	

}
