package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ParallelMultiplication {
	
	public static class MnozenieMacierzy implements Callable<ArrayList<ArrayList<java.math.BigDecimal>>> {
		int first, last;
		ArrayList<ArrayList<ArrayList<java.math.BigDecimal>>> matrixes;
		
		public MnozenieMacierzy(int f, int l, ArrayList<ArrayList<ArrayList<java.math.BigDecimal>>> m) {
			first = f;
			last = l;
			matrixes = m;
		}
		
		 @Override
		 public ArrayList<ArrayList<java.math.BigDecimal>> call() throws Exception {
			 ArrayList<ArrayList<java.math.BigDecimal>> matrix = matrixes.get(first);
			for (int i = first+1; i <= last; i++) {
				matrix = multiplicate(matrix, matrixes.get(i));
			}
			 //System.out.println(first + " " + last);
		     return matrix;
		 }
	}


	public static void main(String[] args)  throws FileNotFoundException, InterruptedException, ExecutionException {
		
		ArrayList<ArrayList<ArrayList<java.math.BigDecimal>>> matrixes = new ArrayList<ArrayList<ArrayList<java.math.BigDecimal>>>();
        String test = new String();
        Scanner scanner = new Scanner(new File("sample-matrices.txt"));
        int currMatrixLine = 1; //to get line of matrix in the file
        int counter = 0;
        while (scanner.hasNextLine()){
            test = scanner.nextLine();
            if (test.isEmpty()) {
            	currMatrixLine = 1;
            	continue;
            }
            if (currMatrixLine == 1) {
            	matrixes.add(new ArrayList<ArrayList<java.math.BigDecimal>>());
            }
            
            int l1 = matrixes.size()-1;
            matrixes.get(l1).add(new ArrayList<java.math.BigDecimal>());

            for (String s : test.split("\\s*;\\s*")) {
            	
                int l2 = matrixes.get(l1).size()-1;
            	matrixes.get(l1).get(l2).add(java.math.BigDecimal.valueOf(Double.parseDouble(s))); 
            }
            
            {
            	currMatrixLine++;
            }
        }
        scanner.close();
        
//      counter = 1;
//		for (ArrayList<ArrayList<java.math.BigDecimal>> matrix : matrixes) {
//			System.out.println(counter++);
//			System.out.println(matrix.size() + "x" + matrix.get(0).size());
//		}
        
//        TEST TEST TEST TEST TEST TEST TEST TEST TEST TEST TEST TEST        
        
//        ArrayList<ArrayList<java.math.BigDecimal>> testMatrix = new ArrayList<ArrayList<java.math.BigDecimal>>();
//        testMatrix.add(new ArrayList<java.math.BigDecimal>());
//        testMatrix.get(0).add(new java.math.BigDecimal(1));
//        testMatrix.get(0).add(new java.math.BigDecimal(2));
//
//        ArrayList<ArrayList<java.math.BigDecimal>> testMatrix2 = new ArrayList<ArrayList<java.math.BigDecimal>>();
//        testMatrix2.add(new ArrayList<java.math.BigDecimal>());
//        testMatrix2.add(new ArrayList<java.math.BigDecimal>());
//        testMatrix2.get(0).add(new java.math.BigDecimal(1));
//        testMatrix2.get(0).add(new java.math.BigDecimal(2));
//        testMatrix2.get(0).add(new java.math.BigDecimal(3));
//        testMatrix2.get(1).add(new java.math.BigDecimal(4));
//        testMatrix2.get(1).add(new java.math.BigDecimal(5));
//        testMatrix2.get(1).add(new java.math.BigDecimal(6));
//        
//		for (int i = 0; i < testMatrix.size(); i++){
//			for (int j = 0; j < testMatrix.get(i).size(); j++) {
//				System.out.print(testMatrix.get(i).get(j) + " ");
//			}
//			System.out.println("");
//		}
//		System.out.println("");
//		for (int i = 0; i < testMatrix2.size(); i++){
//			for (int j = 0; j < testMatrix2.get(i).size(); j++) {
//				System.out.print(testMatrix2.get(i).get(j) + " ");
//			}
//			System.out.println("");
//		}
//		System.out.println("");
//        
//        ArrayList<ArrayList<java.math.BigDecimal>> testMatrixResult = multiplicate(testMatrix, testMatrix2);
//
//
//		System.out.println("");
//		for (int i = 0; i < testMatrixResult.size(); i++){
//			for (int j = 0; j < testMatrixResult.get(i).size(); j++) {
//				System.out.print(testMatrixResult.get(i).get(j) + " ");
//			}
//			System.out.println("");
//		}
        
//      END TEST END TEST END TEST END TEST END TEST END TEST END TEST END TEST        

		ArrayList<ArrayList<java.math.BigDecimal>> matrix = matrixes.get(0);
		counter = 1000;

        long startTime = System.currentTimeMillis();
		for (int i = 1; i < counter; i++) {
			matrix = multiplicate(matrix, matrixes.get(i));
		}
        long stopTime = System.currentTimeMillis();
        System.out.println("Czas: " + (stopTime - startTime));
        

		
		ExecutorService pool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

		ArrayList<Future<ArrayList<ArrayList<java.math.BigDecimal>>>> futureMatrixes = new ArrayList<Future<ArrayList<ArrayList<java.math.BigDecimal>>>>();

        startTime = System.currentTimeMillis();

        ArrayList<Callable<ArrayList<ArrayList<java.math.BigDecimal>>>> callables =
            new ArrayList<Callable<ArrayList<ArrayList<java.math.BigDecimal>>>>();
        for(int i=0; i<Runtime.getRuntime().availableProcessors(); i++) {
            callables.add(new MnozenieMacierzy(250*i, 250*i+249, matrixes));
        }
        List<Future<ArrayList<ArrayList<java.math.BigDecimal>>>> results =
            pool.invokeAll(callables);
        
//	    for (int i = 0; i < 10; i++) {
//	      Callable<ArrayList<ArrayList<java.math.BigDecimal>>> callable = new MnozenieMacierzy(10*i, 10*i+9, matrixes);
//	      Future<ArrayList<ArrayList<java.math.BigDecimal>>> future = pool.submit(callable);
//	      futureMatrixes.add(i, future);
//	    }

	    counter = Runtime.getRuntime().availableProcessors();
		matrix = results.get(0).get();
		for (int i = 1; i < Runtime.getRuntime().availableProcessors(); i++) {
			matrix = multiplicate(matrix, results.get(i).get());
		}
        stopTime = System.currentTimeMillis();
        System.out.println("Czas rownolegle: " + (stopTime - startTime));
        System.out.println("Czas rownolegly razy liczba rdzeni: " + ((stopTime - startTime) * Runtime.getRuntime().availableProcessors()));
		
//		for (int i = 0; i < matrix.size(); i++){
//			for (int j = 0; j < matrix.get(i).size(); j++) {
//				System.out.print(matrix.get(i).get(j) + " ");
//			}
//			System.out.println("");
//		}
		
		
	}
	
	public static ArrayList<ArrayList<java.math.BigDecimal>> multiplicate(
			ArrayList<ArrayList<java.math.BigDecimal>> matrix1,
			ArrayList<ArrayList<java.math.BigDecimal>> matrix2
			) {
		int height = matrix1.size();
		int width = matrix2.get(0).size();
		int length = matrix1.get(0).size();
		//System.out.println(height + " " + width + " " + length);
		
		/*           matrix2
		 * 
		 *              xxxx 
		 *              xxxx
		 *              xxxx
		 *   
		 *          xxx xxxx
		 * matrix1  xxx xxxx  matrix
		 * 
		 * 
		 * 
		 * 
		 * height = 2; width = 4; length = 3;
		 * 
		 */
		
		ArrayList<ArrayList<java.math.BigDecimal>> matrix = new ArrayList<ArrayList<java.math.BigDecimal>>();
		for (int i = 0; i < height; i++) {
			//System.out.println("i: " + i);
			matrix.add(new ArrayList<java.math.BigDecimal>());
			for (int j = 0; j < width; j++) {
				//System.out.println("j: " + j);
				matrix.get(i).add(new java.math.BigDecimal(0));
				for (int k = 0; k < length; k++) {
					//System.out.println("k: " + k);
					java.math.BigDecimal old = matrix.get(i).get(j);
					matrix.get(i).get(j);
					matrix1.get(i).get(k);
//					if(matrix2.size() == k){
//						for (int w = 0; w < matrix2.size(); w++){
//							for (int x = 0; x < matrix2.get(w).size(); x++) {
//								System.out.print(matrix2.get(w).get(x).toBigInteger() + " ");
//							}
//							System.out.println("");
//						}
//					}
					matrix2.get(k).get(j);
					matrix.get(i).set(j, old.add(matrix1.get(i).get(k).multiply(matrix2.get(k).get(j))));
				}
			}
		}
		
		return matrix;
		
	}
	

	
	public static ArrayList<ArrayList<java.math.BigDecimal>> futureMultiplicate(
			Future<ArrayList<ArrayList<java.math.BigDecimal>>> matrix1,
			Future<ArrayList<ArrayList<java.math.BigDecimal>>> matrix2
			) throws InterruptedException, ExecutionException {
		int height = matrix1.get().size();
		int width = matrix2.get().get(0).size();
		int length = matrix1.get().get(0).size();
		//System.out.println(height + " " + width + " " + length);
		
		/*           matrix2
		 * 
		 *              xxxx 
		 *              xxxx
		 *              xxxx
		 *   
		 *          xxx xxxx
		 * matrix1  xxx xxxx  matrix
		 * 
		 * 
		 * 
		 * 
		 * height = 2; width = 4; length = 3;
		 * 
		 */
		
		ArrayList<ArrayList<BigDecimal>> matrix =  new ArrayList<ArrayList<java.math.BigDecimal>>();
		for (int i = 0; i < height; i++) {
			//System.out.println("i: " + i);
			matrix.add(new ArrayList<java.math.BigDecimal>());
			for (int j = 0; j < width; j++) {
				//System.out.println("j: " + j);
				matrix.get(i).add(new java.math.BigDecimal(0));
				for (int k = 0; k < length; k++) {
					//System.out.println("k: " + k);
					java.math.BigDecimal old = matrix.get(i).get(j);
					matrix.get(i).set(j, old.add(matrix1.get().get(i).get(k).multiply(matrix2.get().get(k).get(j))));
				}
			}
		}
		
		return matrix;
		
	}
	
}
