package gaFactory;
/*
 * Boxiong Tan (Maximus Tann)
 * Title:        Single-objective GA framework
 * Description:  Single-objective GA framework for general optimization purpose
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2016-2019, The Victoria University of Wellington
 * IntGA.java - An int version GA
 */


import ProblemDefine.ParameterSettings;
import ProblemDefine.ProblemParameterSettings;
import java.util.ArrayList;

import GAProcedure.CommonGA;;
/**
 * An int GA
 *
 * @author Boxiong Tan (Maximus Tann)
 * @since PSO framework 1.0
 */
public class IntGA extends CommonGA{
	GAFactory factory;
	ParameterSettings pars;
	ProblemParameterSettings proSet;


	/**
	 * Constructor
	 * @param pars Parameter settings
	 * @param proSet Problem settings
	 * @param factory factory is used to assemble parts
	 */
	public IntGA(ParameterSettings pars, ProblemParameterSettings proSet, GAFactory factory){
		this.factory = factory;
		this.pars = pars;
		this.proSet = proSet;
		prepare();
	}

	/**
	 * All settings are prepared here
	 * This is the list of all settings, please read carefully
	 * maxGen: 		max number of generation
	 * maxVar: 		max number of variables in a particle
	 * popSize:		population Size
	 * lbound		lower bound of a variable
	 * ubound		upper bond of a variable
	 * optimization maximize (1) or minimize (0)
	 * popFit		population fitness
	 * initPop		a population initialization method
	 * mutation		a mutation method
	 * crossover 	a crossover method
	 * selection		a selection method
	 * evaluate		evaluation method
	 * collector	data collector
	 */
	protected void prepare(){
		maxGen = pars.getMaxGen();
		maxVar = pars.getMaxVar();
		popSize = pars.getPopSize();
		mutationRate = pars.getMutationRate();
		crossoverRate = pars.getCrossoverRate();
		lbound = pars.getLbound();
		ubound = pars.getUbound();
		optimization = pars.getOptimization();
		tournamentSize = pars.getTournamentSize();
		popFit = new ArrayList<double[]>();

		initPop = factory.getInitPopMethod();
		mutation = factory.getMutation();
		crossover = factory.getCrossover();
		selection = factory.getSelection(tournamentSize, optimization);
		evaluate = proSet.getEvaluate();
		collector = factory.getDataCollector();
		sort = factory.getSort();
	}

}
