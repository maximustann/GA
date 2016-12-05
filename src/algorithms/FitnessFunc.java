/*
 * Boxiong Tan (Maximus Tann)
 * Title:        Single-objective GA framework
 * Description:  Single-objective GA framework for general optimization purpose
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2016-2019, The Victoria University of Wellington
 * FitnessFunc.java - An abstract of common fitness function.
 */

package algorithms;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


/**
 *  FitnessFunc is a wrapper of your unNormalized fitness function, it mainly utilizes
 *  java multi-threading framework in order to speed up the evolutionary process
 *  Essentially, it is still follow the factory pattern. It requires the class type of 
 *  your unNormalizedFit in order to new evaluation instances so that they can be run in parallel.
 */
public class FitnessFunc{
	
	@SuppressWarnings("rawtypes")
	private Class childType;
	@SuppressWarnings("rawtypes")
	/**
	 * If your unNormalized fitness function does not implement the UnNormalizedFit interface,
	 * an exception will be raised. 
	 * @param unNorFit the class type of your unNormalized function
	 */
	public FitnessFunc(Class unNorFit){
		if(!UnNormalizedFit.class.isAssignableFrom(unNorFit)){
			throw new IllegalArgumentException("Class: " + unNorFit.getName() + " must "
					+ "implement UnNormalizedFit interface");
		}
		childType = unNorFit;
	}
	
    /**
     * execute method is a function that calls you defined unNormalized fitness function and
     * then return a fitness value list 
     * Steps:
     * <ul>
     * 	<li>Initialize a thread pool with the number of cpus of this machine </li>
     * 	<li>Use reflection to create popSize of the unNormalized fitness evaluation tasks.</li>
     * 	<li>Add these tasks into the execution pool </li>
     *  <li>execute all tasks and collect fitness values</li>
     * </ul>
     * @param popVar population variables
     * @return an ArrayList<double[]> type of fitness values, where each list item is a double[2]
     * 		double[0] is the fitness value,
     * 		double[1] is the rank of this fitness value in the population, this rank is initialized
     * 		with the current chromosome's position in the population. Because it will be used in the 
     * 		sorting process.
     */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ArrayList<double[]> execute(Chromosome [] popVar) {
		int popSize = popVar.length;
		ExecutorService exec = Executors.newFixedThreadPool(
								Runtime.getRuntime().availableProcessors());
		ArrayList tasks = new ArrayList();
		for(int i = 0; i < popSize; i++){
			try {
				tasks.add(childType.getConstructor(Chromosome.class)
						 .newInstance(popVar[i])
						 );
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			}
		}
		ArrayList<Future> results = null;
		try {
			results = (ArrayList<Future>) exec.invokeAll(tasks);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		exec.shutdown();
		
		ArrayList<double[]> fitness = new ArrayList<double[]>();
//		for(int i = 0; i < popSize; i++){
//			try {
//				System.out.println(results.get(i).get());
//				double[] tempResults = (double[]) results.get(i).get();
//				fitness.add(tempResults);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			} catch (ExecutionException e) {
//					e.printStackTrace();
//			}
//			// initialize the ranking with the position of chromosomes
//			fitness.get(i)[1] = i;
//		}
		int counter = 0;
		for(Future f: results){
			try {
				fitness.add((double[]) f.get());
				fitness.get(counter)[1] = counter;
				counter++;
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		}
		return fitness;
	}
}
