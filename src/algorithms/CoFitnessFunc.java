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
 *  FitnessFunc is a wrapper of your implementation of unNormalizedFit. 
 *  It mainly uses java multithreading framework in order to speed up the evolutionary process.
 *  Essentially, it still follows the factory pattern. It requires the class type of 
 *  your implementation of the unNormalizedFit abstract class in order to 'new' evaluation instances 
 *  so that they can be run in parallel.
 */
public class CoFitnessFunc {

	@SuppressWarnings("rawtypes")
	private Class childType;
	@SuppressWarnings("rawtypes")
	/**
	 * If your unNormalized fitness function does not implement the UnNormalizedFit abstract class,
	 * an exception will be raised.
	 * @param unNorFit the class type of your implementation of the unNormalizedFit abstract class.
	 */
	public CoFitnessFunc(Class unNorFit){
		if(!CoUnNormalizedFit.class.isAssignableFrom(unNorFit)){
			throw new IllegalArgumentException("Class: " + unNorFit.getName() + " must "
					+ "implement CoUnNormalizedFit interface");
		}
		childType = unNorFit;
	}
	
    /**
     * The execute method is a function that calls your implementation of UnNormalizedFit abstract class and
     * then it returns a fitness value list.
     * Steps:
     * <ul>
     * 	<li>Initialize a thread pool with the number of cpus of this machine </li>
     * 	<li>Use reflection to create popSize of your implementation of UnNormalizedFit abstract class.</li>
     * 	<li>Add these tasks into the execution pool. </li>
     *  <li>Execute all tasks and collect fitness values. </li>
     * </ul>
     * @param popVar population variables
     * @return an ArrayList<double[]> type of fitness values, where each list item is a double[2]
     * 		double[0] is the fitness value,
     * 		double[1] is the rank of this fitness value in the population, this rank is initialized
     * 		with the current chromosome's position in the population. Because it will be used in the 
     * 		sorting process.
     */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ArrayList<double[]> execute(int subPop, Chromosome [] popVar,
									   Chromosome[] representatives) {
		int popSize = popVar.length;
		
		// create a threading pool
		ExecutorService exec = Executors.newFixedThreadPool(
								Runtime.getRuntime().availableProcessors());
		
		// an array of tasks
		ArrayList tasks = new ArrayList();
		
		// create instance of your implementation and add them into the task list
		for(int i = 0; i < popSize; i++){
			try {
				// when you get the constructor, you will need to pass the class types of that constructor
				tasks.add(childType.getConstructor(int.class, Chromosome.class, Chromosome[].class)
						 .newInstance(subPop, popVar[i], representatives)
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
			// execute tasks
			results = (ArrayList<Future>) exec.invokeAll(tasks);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		exec.shutdown();
		
		ArrayList<double[]> fitness = new ArrayList<double[]>();
		int counter = 0;
		for(Future f: results){
			try {
				// retrieve data
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
