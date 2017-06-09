package main;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import exception.EdgeNotFoundException;
import exception.NodeNotFoundException;
import graph.Graf;
import graph.GrafE;
import graph.GrafV;
import list.ListGraf;
import matrix.MatrixGraf;
import scanner.GrafScanner;

public class Main {
	
	public static class Path {
		LinkedList<Integer> sciezka;
		LinkedList<Integer> waga;
		
		public Path() {
			sciezka = null;
			waga = null;
		}
		
		public Path(LinkedList<Integer> sciezka, LinkedList<Integer> waga) {
			this.sciezka = sciezka;
			this.waga = waga;
		}
		
		public Path(Path p) {
			this.sciezka = (LinkedList<Integer>) p.sciezka.clone();
			this.waga = (LinkedList<Integer>) p.waga.clone();
		}
	}


    public static void main(String args[]) {
        try {
//            System.out.println("Testing Matrix Graf");
//            Graf matrixGraph = new MatrixGraf();
//            testPrimaryFunctions(matrixGraph);
//
//            System.out.println("Testing List Graf");
//            Graf listGraph = new ListGraf();
//            testPrimaryFunctions(listGraph);
        	
        	
//            System.out.println("Warshal-Floyd Matrix Graf");
//            Graf matrixGraph = new MatrixGraf();
//            int tm = WarshalFloydAlgorithm(matrixGraph);
//
//            System.out.println("Warshal-Floyd List Graf");
//            Graf listGraph = new ListGraf();
//            int tl = WarshalFloydAlgorithm(listGraph);
//            
//            System.out.println(tl);
//            System.out.println(tm);
//            System.out.println((float)tl/(float)tm);

        	
        	
          System.out.println("Ford-Fulkerson Matrix Graf");
          Graf matrixGraph = new MatrixGraf();
          int tm = FordFulkersonAlgorithm(matrixGraph);

          System.out.println("Ford-Fulkerson List Graf");
          Graf listGraph = new ListGraf();
          int tl = FordFulkersonAlgorithm(listGraph);
          
          System.out.println(tl);
          System.out.println(tm);
          System.out.println((float)tl/(float)tm);
            
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static int FordFulkersonAlgorithm(Graf graf) throws FileNotFoundException, NodeNotFoundException, EdgeNotFoundException, InterruptedException {
    	int source = 109;
    	int sink = 609;

        //Loading graph
        GrafScanner.importGraphFromFile(graf, "duzy_duzy_graf.txt");

        int eCount = graf.getECount();
        int vCount = graf.getVCount();
        System.out.println(graf.getVCount());
    	int [][] f = new int[vCount+1][vCount+1];
        
        long startTime = System.currentTimeMillis();
    	
    	int przeplyw = 0;
    	
    	for (GrafE e: graf.getE()) {
    		int v1 = e.getFirstV().getVId();
    		int v2 = e.getSecondV().getVId();
    		
    		f[v1][v2] = 0;
    		f[v2][v1] = 0;
    	}
    	
    	GrafV [] lookTable = new GrafV[vCount+1];
		for (GrafV v: graf.getV()) {
			lookTable[v.getVId()] = v;
		}
    	Path p = new Path();
    	while ((p = getPathBFS(graf, source, sink, new LinkedList<Integer>(),
    			new Path(new LinkedList<Integer>(), new LinkedList<Integer>()), lookTable, new LinkedList<Path>())) != null) {
    		
    		int min = Integer.MAX_VALUE;
    		int counter = 0; 
    		int minCounter = 0;
    		for (int w: p.waga) {
    			if (w < min) {
    				min = w;
    				minCounter = counter;
    			}
    			counter++;
    		}
    		
//    		System.out.println("\n" + p.sciezka.get(minCounter));
//    		System.out.println(p.sciezka.get(minCounter+1));
//    		System.out.println(p.sciezka.toString());
//    		System.out.println(p.waga.get(minCounter));
    		int cf = min - f[p.sciezka.get(minCounter)][p.sciezka.get(minCounter + 1)];
    		System.out.println(p.waga.size());
    		System.out.println("przeplyw: " + przeplyw + " -> +" + cf);
    		przeplyw += cf;
    		
    		counter = 0;
    		for (int s : p.sciezka) {
    			if (counter == 0){
    				counter++;
    				continue;
    			}
    			
    			//f[s][p.sciezka.get(counter-1)] += cf;
    			//f[p.sciezka.get(counter-1)][s] -= cf;
    			
    			int vid1 = 0;
    			int vid2 = 0;
    			int w = 0;
    			int vid12 = 0;
    			int vid22 = 0;
    			int w2 = 0;
    			boolean isReversed = false;

    			for (GrafE e: graf.getOutE(lookTable[p.sciezka.get(counter-1)])) {
    				if (e.getFirstV().getVId() == p.sciezka.get(counter-1) 
    						&& e.getSecondV().getVId() == s
    						&& e.getWeight() == p.waga.get(counter-1)
    						) {
    					vid1 = e.getFirstV().getVId();
    					vid2 = e.getSecondV().getVId();
    					w = e.getWeight();
    				}
    			}
    			for (GrafE e: graf.getOutE(lookTable[s])) {
    				if (e.getFirstV().getVId() == s
    						&& e.getSecondV().getVId() == p.sciezka.get(counter-1)
    						) {
    					vid12 = e.getFirstV().getVId();
    					vid22 = e.getSecondV().getVId();
    					w2 = e.getWeight();
    					isReversed = true;
    				}
    			}
//    			System.out.println("wejscie: " +wejscie);
				graf.deleteE(new GrafE(
						lookTable[vid1],
						lookTable[vid2],
						w
						));
				if (w != cf) {
	    			graf.addE(new GrafE(
	    					lookTable[vid1],
	    					lookTable[vid2],
	    					w - cf
	    					));
				}
				if (isReversed) {
					graf.deleteE(new GrafE(
	    					lookTable[vid2],
	    					lookTable[vid1],
							w2
							));
				}
    			graf.addE(new GrafE(
    					lookTable[vid2],
    					lookTable[vid1],
    					w2 + cf
    					));
    			counter++;
    		}    		    		
    	}
    	
    	System.out.println("\nPrzeplyw: " + przeplyw);
        
        return (int) (System.currentTimeMillis() - startTime);
    }
    
    public static Path getPathBFS(Graf graf, int source, int sink, List<Integer> temp, Path tempPath, GrafV [] lookTable, LinkedList<Path> queue) throws FileNotFoundException, NodeNotFoundException, EdgeNotFoundException, InterruptedException  {
    	if (temp.isEmpty()) {
    		temp.add(source);
    		LinkedList<Integer> beginList = new LinkedList<Integer>();
    		beginList.add(source);
    		queue.add(new Path(beginList, new LinkedList<Integer>()));
    	}
    	
    	if (source == sink) { 
			tempPath.sciezka.add(sink);
    	    return tempPath;
    	} else {
    		while (queue.size() > 0) {
    			Path path = queue.removeFirst();
    			if (path.sciezka.getLast() == sink)
    				return path;
	    		for (GrafE e : graf.getOutE(lookTable[path.sciezka.getLast()])){
	    			if (e.getWeight() != 0) {
	    				GrafV v = e.getSecondV();
		    			if (!temp.contains(v.getVId())) {
		    				temp.add(v.getVId());
		    				Path newPath = new Path(path);
		    				newPath.sciezka.add(v.getVId());
		    				newPath.waga.add(e.getWeight());
		    				queue.addLast(newPath);
		    			}
		    		}
	    		}
    		}
    	}
    	return null;
    }
    
    public static Path getPathDFS(Graf graf, int source, int sink, List<Integer> temp, Path tempPath, GrafV [] lookTable, LinkedList<Path> queue) throws FileNotFoundException, NodeNotFoundException, EdgeNotFoundException, InterruptedException  {

    	Path tt = null;
    	if (source == sink) { 
			tempPath.sciezka.add(sink);
    	    return tempPath;
    	} else {
    		temp.add(source);
    		tempPath.sciezka.add(source);
    		for (GrafE e : graf.getOutE(lookTable[source])){
    			if (e.getWeight() != 0) {
    				GrafV v = e.getSecondV();
	    			if (temp.contains(v.getVId())) {
	    				continue;
	    			}
	        		tempPath.waga.add(e.getWeight());
	    			tt = getPathDFS(graf, v.getVId(), sink, temp, tempPath, lookTable, queue);
	    			if (tt == null) {
	    				tempPath.waga.removeLast();
	    				tempPath.sciezka.removeLast();
	    				continue;
	    			} else {
	    				return tt;
	    			}
	    		}
    		}
    	}
    	return null;
    }
    
    public static int WarshalFloydAlgorithm(Graf graf) throws FileNotFoundException, NodeNotFoundException, EdgeNotFoundException {


        //Loading graph
        GrafScanner.importGraphFromFile(graf, "duzy_duzy_graf.txt");

        int vCount = graf.getVCount();
        
        int [][] d = new int[vCount+1][vCount+1];
        int [][] poprzednik = new int[vCount+1][vCount+1];
        
        long startTime = System.currentTimeMillis();
        
        for (GrafV v1: graf.getV()) {
    		int d1 = v1.getVId();
        	for (GrafV v2: graf.getV()){
        		int d2 = v2.getVId();
        		d[d1][d2] = Integer.MAX_VALUE;
        	}
        }
        for (GrafE e: graf.getE()) {
        	int d1 = e.getFirstV().getVId();
        	int d2 = e.getSecondV().getVId();
        	d[d1][d2] = e.getWeight();
        	poprzednik[d1][d2] = e.getFirstV().getVId();
        }
        for (GrafV u: graf.getV()){
        	int du = u.getVId();
        	for (GrafV v1: graf.getV()){
        		int d1 = v1.getVId();
        		for (GrafV v2: graf.getV()){
            		int d2 = v2.getVId();
            		int sum;
            		if (d[d1][du] + d[du][d2] < 0){
            			sum = Integer.MAX_VALUE;
            		} else {
            			sum = d[d1][du] + d[du][d2];
            		}
        			if (d[d1][d2] > sum){
        				d[d1][d2] = sum;
        				poprzednik[d1][d2] = poprzednik[du][d2];
        			}
        		}
        	}
        }
        
        int [] sciezka = new int[200];
        int i = 0;
        int dlugosc = 0;
        sciezka[0] = 609;
        while (true){
        	int poprzednikV = poprzednik[109][sciezka[i]];
        	if (i>0) {
        		dlugosc += d[sciezka[i]][sciezka[i-1]];
        	}
        	i++;
        	sciezka[i] = poprzednikV;
        	if (sciezka[i] == 109){
        		dlugosc += d[sciezka[i]][sciezka[i-1]];
        		break;
        	}
        }
        for(int k = 0; k < sciezka.length / 2; k++)
        {
            int temp = sciezka[k];
            sciezka[k] = sciezka[sciezka.length - k - 1];
            sciezka[sciezka.length - k - 1] = temp;
        }
        int j = 0;
        for( int k=0;  k<sciezka.length; k++ )
        {
            if (sciezka[k] != 0)
            	sciezka[j++] = sciezka[k];
        }
        int [] newSciezka = new int[j];
        System.arraycopy( sciezka, 0, newSciezka, 0, j );
        System.out.println("Sciezka: " + Arrays.toString(newSciezka));
        System.out.println("Dlugosc: " + dlugosc);
        //System.out.println(poprzednik[1][20]);
        
        return (int) (System.currentTimeMillis() - startTime);
    }

    public static void testPrimaryFunctions(Graf graph) throws FileNotFoundException, NodeNotFoundException, EdgeNotFoundException {
        long startTime = System.currentTimeMillis();

        //Loading graph
        GrafScanner.importGraphFromFile(graph, "duzy_graf.txt");
        System.out.println("Graph loaded!!! Time: " + (System.currentTimeMillis() - startTime) + " ms.");

        //Nodes count
        int nodesCount1 = graph.getVCount();
        System.out.println("Ilość wierzchołków: " + nodesCount1);

        //Edges count
        int edgesCount1 = graph.getECount();
        System.out.println("Ilość krawędzi: " + edgesCount1);

        //Deleting node
        graph.deleteV(new GrafV(1));

        //Delete edge
//        graph.deleteEdge(new GraphEdge(new GraphNode(15), new GraphNode(5), 54));
        //graph.deleteE(new GrafE(new GrafV(762), new GrafV(606), 199));

        //Get neighbors
        GrafV[] neighbors = graph.getNeighborV(new GrafV(19));

        //Get incidental
        GrafE[] incidental = graph.getOutE(new GrafV(19));

        //Nodes count
        int nodesCount2 = graph.getVCount();

        //Edges count
        int edgesCount2 = graph.getECount();

        //Is neighbor
        boolean test1 = graph.isVNeighbors(new GrafV(1), new GrafV(31));
        boolean test2 = graph.isVNeighbors(new GrafV(5), new GrafV(9));

        System.out.println("Total Test Time: " + (System.currentTimeMillis() - startTime) + " ms.");
        System.out.println("");
    }
}
