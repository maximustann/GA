/*
 * Boxiong Tan (Maximus Tann)
 * Title:        GA framework
 * Description:  GA framework for general optimization purpose
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2016-2019, The Victoria University of Wellington
 * RealGAFactory.java - A real version of GA factory to assemble different parts
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
 * RealGAFactory
 *
 * @author Boxiong Tan (Maximus Tann)
 * @since PSO framework 1.0
 */
public class RealGAFactory implements GAFactory{
	private DataCollector collector;
	private double lbound, ubound, perturbation;

	/**
	 * Constructor
	 * @param collector is the data collector
	 * @param lbound lower bound of variable 
	 * @param ubound upper bound of variable
	 * @param perturbation the parameter used in polynomial mutation
	 */
	public RealGAFactory(
				DataCollector collector, 
				double lbound, 
				double ubound, 
				double perturbation
				){
		this.collector = collector;
		this.lbound = lbound;
		this.ubound = ubound;
		this.perturbation = perturbation;
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
		return  new PolyMutation(lbound, ubound, perturbation);
	}

	@Override
	public Selection getSelection(int tournamentSize, int optimization) {
		return new TournamentSelection(tournamentSize, optimization);
	}

	@Override
	public Crossover getCrossover() {
		return new SimulatedBinaryCrossover();
	}
	
	public Sort getSort(){
		return new sortPop();
	}

	@Override
	public Elitism getElitism(int elitSize, int optimization) {
		return new CommonElitism(elitSize, optimization);
	}

	@Override
	public Constraint getConstraint() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Distance getDistance() {
		// TODO Auto-generated method stub
		return null;
	}
}
