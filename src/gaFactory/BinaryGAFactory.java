/*
 * Boxiong Tan (Maximus Tann)
 * Title:        GA algorithm framework
 * Description:  GA algorithm framework for general optimization purpose
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2016-2019, The Victoria University of Wellington
 * BinaryGAFactory.java - A binary implementation of GA's factory
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
import commonOperators.BinaryFlipCoinMutation;
import commonOperators.CommonElitism;
import commonOperators.InitBinaryChromosome;
import commonOperators.SinglePointCrossover;
import commonOperators.TournamentSelection;
import commonOperators.sortPop;
import dataCollector.DataCollector;
/**
 * BinaryGAFactory
 *
 * @author Boxiong Tan (Maximus Tann)
 * @since PSO framework 1.0
 */
public class BinaryGAFactory implements GAFactory {
	private DataCollector collector;
	
	public BinaryGAFactory(DataCollector collector){
		this.collector = collector;
	}
	@Override
	public InitPop getInitPopMethod() {
		return new InitBinaryChromosome();
	}

	@Override
	public Mutation getMutation() {
		return new BinaryFlipCoinMutation();
	}

	@Override
	public Selection getSelection(int tournamentSize, int optimization) {
		return new TournamentSelection(tournamentSize, optimization);
	}

	@Override
	public Crossover getCrossover() {
		return new SinglePointCrossover();
	}

	@Override
	public Sort getSort() {
		return new sortPop();
	}

	@Override
	public DataCollector getDataCollector() {
		return collector;
	}

	@Override
	public Elitism getElitism(int elitSize, int optimization) {
		return new CommonElitism(elitSize, optimization);
	}
//	@Override
//	public Constraint getConstraint() {
//		// TODO Auto-generated method stub
//		return null;
//	}
	@Override
	public Distance getDistance() {
		// TODO Auto-generated method stub
		return null;
	}

}
