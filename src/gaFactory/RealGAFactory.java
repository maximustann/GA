/*
 * Boxiong Tan (Maximus Tann)
 * Title:        PSO algorithm framework
 * Description:  PSO algorithm framework for general optimization purpose
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2016-2019, The Victoria University of Wellington
 * DoubleGAFactory.java - A double type GA factory to assemble different parts
 */
package gaFactory;

import algorithm.Crossover;
import algorithm.InitPop;
import algorithm.Mutation;
import algorithm.Selection;
import commonOperators.*;
import dataCollector.DataCollector;
/**
 * RealGAFactory
 *
 * @author Boxiong Tan (Maximus Tann)
 * @since PSO framework 1.0
 */
public class RealGAFactory implements GAFactory{
	private DataCollector collector;
	private double lbound, ubound;

	/**
	 * Constructor
	 * @param collector is the data collector
	 */
	public RealGAFactory(DataCollector collector, double lbound, double ubound){
		this.collector = collector;
		this.lbound = lbound;
		this.ubound = ubound;
	}

	@Override
	public InitPop getInitPopMethod() {
		return new InitRealChromosomes();
	}

	@Override
	public DataCollector getDataCollector() {
		return collector;
	}

	@Override
	public Mutation getMutation() {
		return  new PolyMutation(lbound, ubound);
	}

	@Override
	public Selection getSelection(int tournamentSize, int optimization) {
		return new TournamentSelection(tournamentSize, optimization);
	}

	@Override
	public Crossover getCrossover() {
		return new SimulatedBinaryCrossover();
	}
}
