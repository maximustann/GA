/*
 * Boxiong Tan (Maximus Tann)
 * Title:        GA framework
 * Description:  GA framework for general optimization purpose
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2016-2019, The Victoria University of Wellington
 * IntGAFactory.java - A int type GA factory to assemble different parts
 */
package gaFactory;

import algorithms.Constraint;
import algorithms.Crossover;
import algorithms.Distance;
import algorithms.Elitism;
import algorithms.InitPop;
import algorithms.Mutation;
import algorithms.Selection;
import algorithms.Sort;
import commonOperators.*;
import dataCollector.DataCollector;
/**
 * IntGAFactory
 *
 * @author Boxiong Tan (Maximus Tann)
 * @since PSO framework 1.0
 */
public class IntGAFactory implements GAFactory{
	private DataCollector collector;
	private int seed;

	/**
	 * Constructor
	 * @param collector is the data collector
	 */
	public IntGAFactory(DataCollector collector, int seed){
		this.collector = collector;
		this.seed = seed;
	}

	@Override
	public InitPop getInitPopMethod() {
		return new InitIntChromosomes();
	}


	@Override
	public DataCollector getDataCollector() {
		return collector;
	}

	@Override
	public Mutation getMutation() {
		return  new IntReverseSequenceMutation();
	}


	@Override
	public Selection getSelection(int tournamentSize, int optimization) {
		return new TournamentSelection(tournamentSize, optimization, seed);
	}

	@Override
	public Crossover getCrossover() {
		return new SinglePointCrossover();
	}
	
	@Override
	public Sort getSort(){
		return new sortPop();
	}
	
	@Override
	public Elitism getElitism(int elitSize, int optimization){
		return new CommonElitism(elitSize, optimization);
	}

//	@Override
//	public Constraint getConstraint() {
//		return null;
//	}

	@Override
	public Distance getDistance() {
		// TODO Auto-generated method stub
		return null;
	}


}
