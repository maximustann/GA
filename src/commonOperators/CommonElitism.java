/*
 * Boxiong Tan (Maximus Tann)
 * Title:        Single-objective GA framework
 * Description:  Single-objective GA framework for general optimization purpose
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2016-2019, The Victoria University of Wellington
 * CommonElitism.java - an implementation of elitism.
 */
package commonOperators;


import algorithms.Chromosome;
import algorithms.Elitism;
/**
 * An implementation of elitism
 *
 * @author Boxiong Tan (Maximus Tann)
 * @since GA framework 1.0
 */
public class CommonElitism extends Elitism{
	
	/**
	 * 
	 * @param elitSize the size of chromosome that carries over to the next generation
	 * @param optimization the optimization direction
	 */
	public CommonElitism(int elitSize, int optimization) {
		super(elitSize, optimization);
	}
	
	/**
	 * 
	 * @param elitPercent the percentage of chromosome that carries over to the next generation
	 * @param optimization the optimization direction
	 */
	public CommonElitism(double elitPercent, int optimization) {
		super(elitPercent, optimization);
	}
	/**
	 * copy elitSize or elitPercent of chromosomes to next generation
	 */
	@Override
	public void carryover(Chromosome[] popVar, Chromosome[] newPop){
		int popSize = popVar.length;
		if(elitPercent != -1){
			elitSize = (int) (elitPercent * popSize);
		}
		if(optimization == 0){
			for(int i = 0; i < elitSize; i++){
				newPop[i] = popVar[i].clone();
//                newPop[i] = popVar[i];
			}
		} else {
			for(int i = 0; i < elitSize; i++){
				newPop[i] = popVar[popSize - 1 - i].clone();
//                newPop[i] = popVar[popSize - 1 - i];
			}
		}
	}


}
