/*
 * Boxiong Tan (Maximus Tann)
 * Title:        GA framework
 * Description:  GA framework for general optimization purpose
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2016-2019, The Victoria University of Wellington
 * NSGAIIFactory.java - A NSGA-II Factory for WSRP
 */
package wsrp;
import ProblemDefine.ParameterSettings;
import ProblemDefine.ProblemParameterSettings;
import algorithms.Crossover;
import algorithms.Distance;
import algorithms.Elitism;
import algorithms.InitPop;
import algorithms.Mutation;
import algorithms.Selection;
import algorithms.Sort;
import commonOperators.*;
import dataCollector.DataCollector;
import gaFactory.GAFactory;
import multi_objective_operators.BinaryTournamentSelection;
import multi_objective_operators.FastNonDominatedSorting;
/**
 * NSGAIIFactory
 *
 * @author Boxiong Tan (Maximus Tann)
 * @since PSO framework 1.0
 */
public class NSGAIIFactory implements GAFactory{

	private DataCollector collector;
	private ProblemParameterSettings proSet;
	private ParameterSettings pars;

	/**
	 * Constructor
	 * @param collector is the data collector
	 */
	public NSGAIIFactory(DataCollector collector, ProblemParameterSettings proSet, 
						ParameterSettings pars){
		this.proSet = proSet;
		this.pars = pars;
		this.collector = collector;
		
	}

	@Override
	public InitPop getInitPopMethod() {
		return ((AllocationParameterSettings) proSet).getInitPop();
	}

	public WSRP_Constraint getConstraint(){
		return ((AllocationParameterSettings) proSet).getConstraint();
	}

	@Override
	public DataCollector getDataCollector() {
		return collector;
	}

	@Override
	public Mutation getMutation() {
		return ((AllocationParameterSettings) proSet).getMutatioin();
	}


	@Override
	public Selection getSelection(int tournamentSize, int optimization) {
		return new BinaryTournamentSelection(optimization);
	}

	@Override
	public Crossover getCrossover() {
		return null;
	}
	
	public Elitism getElitism(int elitSize, int optimization){
		return new CommonElitism(elitSize, optimization);
	}

	@Override
	public Sort getSort() {
		return new FastNonDominatedSorting(pars.getOptimization());
	}

	@Override
	public Distance getDistance() {
		return ((AllocationParameterSettings) proSet).getDistance();
	}


}
