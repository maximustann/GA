package Rosenbrock;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import algorithm.*;
import commonOperators.RealValueChromosome;
public class TestFunctionFitness implements FitnessFunction{

//	public ArrayList<double[]> unNormalizedFit(Chromosome[] popVar){
//		ArrayList<double[]> fitness = new ArrayList<double[]>();
//		
//		for(int i = 0; i < popVar.length; i++){
//			double fit = 0.0;
//			for(int j = 0; j < popVar[0].size() - 1; j++){
//				double firstTerm, secondTerm;
//				firstTerm = ((RealValueChromosome) popVar[i]).individual[j + 1] - 
//						Math.pow(((RealValueChromosome) popVar[i]).individual[j], 2);
//				secondTerm = Math.pow(((RealValueChromosome) popVar[i]).individual[j] - 1, 2);
//				fit += 100 * (firstTerm * firstTerm) + secondTerm;
//			}
//
//			fitness.add(new double[] {fit, i});
//		}
//		return fitness;
//	}

	public ArrayList<double[]> normalizedFit(Chromosome[] popVar){
		int popSize = popVar.length;
		ExecutorService exec = Executors.newFixedThreadPool(2);
		ArrayList tasks = new ArrayList();
		for(int i = 0; i < popSize; i++){
			tasks.add(new unNormalizedFit(popVar[i]));
		}
		ArrayList<Future> results = null;
		try {
			results = (ArrayList<Future>) exec.invokeAll(tasks);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		exec.shutdown();
		ArrayList<double[]> fitness = new ArrayList<double[]>();
		for(int i = 0; i < popSize; i++){
			try {
				fitness.add((double[]) results.get(i).get());
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
			fitness.get(i)[1] = i;
		}
		return fitness;
//		return unNormalizedFit(popVar);
	}

	@Override
	public ArrayList<double[]> unNormalizedFit(Chromosome[] popVar) {
		// TODO Auto-generated method stub
		return null;
	}

}
