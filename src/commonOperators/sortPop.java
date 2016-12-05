/*
 * Boxiong Tan (Maximus Tann)
 * Title:        Single-objective GA framework
 * Description:  Single-objective GA framework for general optimization purpose
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2016-2019, The Victoria University of Wellington
 * sortPop.java - A sort method for population
 */
package commonOperators;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import algorithms.Chromosome;
import algorithms.Sort;
/**
 * population sorting
 * @author Boxiong Tan (Maximus Tann) 
 * @since GA framework 1.0
 */
public class sortPop implements Sort{
	/**
	 * Sort
	 * Steps
	 * <ul>
	 * 	<li> 1. Sort fitness list according to fitness values of chromosomes </li>
	 *  <li> 2. Rearrange chromosomes according to their ranking </li>
	 *  <li> 3. update the ranking in fitness pairs </li>
	 *  <li> 4. change original population to new population </li>
	 * </ul>
	 * @param popVar population
	 * @param popFit fitness
	 */
	public void sort(Chromosome[] popVar, ArrayList<double[]> popFit){
		Chromosome[] newPop = new Chromosome[popVar.length];
		//******* Debug ******
		//*******
		
		try {
		Collections.sort(popFit, new Comparator<double[]>() {
			
			@Override
			public int compare(double[] fitness1, double[] fitness2) {
				int condition = 0;
				if(fitness1[0] - fitness2[0] > 0.0) condition = 1;
				else if(fitness1[0] - fitness2[0] < 0.0) condition = -1;
				else condition = 0;
				return condition;
			}
	
		});
		}catch(IllegalArgumentException e){
			System.out.println("Sorting problem occurs!");
		}
		
		for(int i = 0; i < popVar.length; i++){
			newPop[i] = popVar[(int) popFit.get(i)[1]];
			popFit.get(i)[1] = i;
		}
		for(int i = 0; i < popVar.length; i++){
			popVar[i] = newPop[i];
		}
	}
}
