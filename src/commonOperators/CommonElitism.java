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

import algorithm.Chromosome;
import algorithm.Elitism;
/**
 * An implementation of elitism
 *
 * @author Boxiong Tan (Maximus Tann)
 * @since GA framework 1.0
 */
public class CommonElitism implements Elitism{
	private int elitSize;
	
	public CommonElitism(int elitSize){
		this.elitSize = elitSize;
	}
	public void carryover(Chromosome[] popVar, Chromosome[] newPop){
		for(int i = 0; i < elitSize; i++){
			newPop[i] = popVar[i];
		}
	}
	public int getSize(){
		return elitSize;
	}
}
