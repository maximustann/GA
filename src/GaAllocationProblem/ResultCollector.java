/*
 * Boxiong Tan (Maximus Tann)
 * Title:        PSO algorithm framework
 * Description:  PSO algorithm framework for general optimization purpose
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2016-2019, The Victoria University of Wellington
 * ResultCollector.java - collect the results for Hai's paper
 */
package GaAllocationProblem;

import java.util.ArrayList;

import algorithms.Chromosome;
import dataCollector.DataCollector;
/**
 * 
 * @author Boxiong Tan (Maximus Tann)
 * @since PSO framework 1.0
 */
public class ResultCollector extends DataCollector {
	private ArrayList<double[]> resultData;

	public ResultCollector(){
		super();
		resultData = new ArrayList<double[]>();
	}
	
	/**
	 * add fitness value
	 */
	public void collect(Object data) {
		resultData.add((double[]) data);
	}

	public ArrayList<double[]> getResult(){
		return resultData;
	}

	/**
	 * print all fitness results
	 */
	public void printResult(){
		for(int i = 0; i < resultData.size(); i++){
			System.out.println(resultData.get(i)[0]);
		}
		System.out.println();
	}
	
	/**
	 * get the last fitness value of many runs
	 */
	public ArrayList<Double> getLastResult(int runs, int maxGen){
		ArrayList<Double> lastResults = new ArrayList<Double>();
		for(int i = 1; i <= runs; i++){
			lastResults.add(resultData.get(maxGen * i - 1)[0]);
		}
		return lastResults;
	}

	public void mean(int runs){
		int size = resultData.size();
		int gen = size / runs;
		double best = 0;
		for(int i = 1; i <= runs; i++) {
			best += resultData.get(i * gen - 1)[0];
		}
		System.out.println(best / runs);
	}
	


}
