/*
 * Boxiong Tan (Maximus Tann)
 * Title:        Single-objective GA framework
 * Description:  Single-objective GA framework for general optimization purpose
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2016-2019, The Victoria University of Wellington
 * FitnessFunc.java - An abstract of common fitness function.
 */

package algorithm;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


/**
 *  For writing any Fitness Functions this class should be extend.
 */
public abstract class FitnessFunc{
	
	@SuppressWarnings("rawtypes")
	private Class childType;
	public FitnessFunc(Class unNorFit){
		childType = unNorFit;
	}
	
    /**
     * Generate an array of normalized fitness values
     * Two steps: 1. apply fitness function on popVar
     * 			  2. apply normalization function on fitness values
     *
     * @param popVar population variables
     * @return array of normalized fitness values
     */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ArrayList<double[]> execute(Chromosome [] popVar) {
		int popSize = popVar.length;
		ExecutorService exec = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
		@SuppressWarnings("rawtypes")
		ArrayList tasks = new ArrayList();
		for(int i = 0; i < popSize; i++){
//			tasks.add(new childType(popVar[i]));
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
		@SuppressWarnings("rawtypes")
		ArrayList<Future> results = null;
		try {
			results = (ArrayList<Future>) exec.invokeAll(tasks);
		} catch (InterruptedException e) {
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
	}
}
