package main;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Pi {
	
	public static class CzescPi implements Callable<Double> {
		int first, last;
		double n;
		
		public CzescPi(Double f, Double l, double n) {
			first = f.intValue();
			last = l.intValue();
			this.n = n;
		}
		
		 @Override
		 public Double call() throws Exception {
				
				Double n = this.n;
				
				Double sum = 0.0;

		        long startTime = System.currentTimeMillis();
				for (int i=first; i<=last; i++){
					sum += 1.0/(1+Math.pow((2.0*i+1)/(2.0*n), 2));
				}
				sum *= 4.0/n;
				return sum;
		 }
	}


	public static void main(String[] args) throws InterruptedException, ExecutionException {
		// TODO Auto-generated method stub
		Double N = 100000000.0;
		
		Double n = N;
		
		Double sum = 0.0;

        long startTime = System.currentTimeMillis();
		for (int i=0; i<N.intValue(); i++){
			sum += 1.0/(1+Math.pow((2.0*i+1)/(2.0*n), 2));
		}
		sum *= 4.0/n;
        long stopTime = System.currentTimeMillis();
		
		System.out.println(sum);
		System.out.println("Sekwencyjnie: " + (stopTime - startTime));
		

		ExecutorService pool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
		sum=0.0;

        startTime = System.currentTimeMillis();
	    Set<Future<Double>> set = new HashSet<Future<Double>>();
	    for (int i =0 ; i<10; i++) {
	      Callable<Double> callable = new CzescPi((N/10.0)*i, (N/10.0)*i+(N/10.0)-1, N);
	      Future<Double> future = pool.submit(callable);
	      set.add(future);
	    }
	    for (Future<Double> future : set) {
	      sum += future.get();
	    }
	    stopTime = System.currentTimeMillis();

		
		System.out.println(sum);
		System.out.println("Rownolegle: " + (stopTime - startTime));
		
	}

}
