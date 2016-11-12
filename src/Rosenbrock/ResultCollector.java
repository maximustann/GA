/*
 * Boxiong Tan (Maximus Tann)
 * Title:        Single-objective GA framework
 * Description:  Single-objective GA framework for general optimization purpose
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2016-2019, The Victoria University of Wellington
 * ResultCollector.java 
 */
package Rosenbrock;

import java.util.ArrayList;

import algorithm.Chromosome;
import dataCollector.DataCollector;
/**
 * A implementation of dataCollector 
 * @author Boxiong Tan (Maximus Tann) 
 * @since GA framework 1.0
 */
public class ResultCollector extends DataCollector {
	/**
	 * resultData contains fitness values,
	 * data contains chromosomes
	 */
	private ArrayList<double[]> resultData;
	private ArrayList<Chromosome[]> data;

	/**
	 * constructor, initialize fields
	 */
	public ResultCollector(){
		resultData = new ArrayList<double[]>();
		data = new ArrayList<Chromosome[]>();
	}
	@Override
	/**
	 * collect() will collect the fitness values in default
	 */
	public void collect(Object data) {
		resultData.add((double[]) data);
	}

	public ArrayList<double[]> getResult(){
		return resultData;
	}
	

	public void printResult(){
		for(int i = 0; i < resultData.size(); i++){
			System.out.println("fitness: " + resultData.get(i)[0]);
		}
		System.out.println();
	}
	
	public void printPop(){
		for(int i = 0; i < data.size(); i++) {
			for(int j = 0; j < data.get(0).length; j++) {
				data.get(i)[j].print();
				System.out.println();
			}
			System.out.println();
		}
	}
	
	public void collectChromosome(Chromosome[] popVar) {
		data.add(popVar);
	}
	
	
}
