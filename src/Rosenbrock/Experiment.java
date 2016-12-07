/*
 * Boxiong Tan (Maximus Tann)
 * Title:        Single-objective Genetic algorithm framework
 * Description:  Single-objective Genetic algorithm framework for general optimization purpose
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2016-2019, The Victoria University of Wellington
 * Experiment.java 	The main function.
 * 
 */
package Rosenbrock;
import java.util.ArrayList;
import ProblemDefine.*;
import algorithms.Evaluate;
import algorithms.FitnessFunc;
import algorithms.GeneticAlgorithm;
import dataCollector.DataCollector;
import gaFactory.RealGA;
import gaFactory.RealGAFactory;
/**
 * An example of the main function
 * 
 * @author Boxiong Tan (Maximus Tann) 
 * @since PSO framework 1.0
 */
public class Experiment {
	/**
	 * Initialize all parameters, set up evaluation function.
	 * Run algorithm.
	 * Print results.
	 * 
	 */
	public static void main(String[] arg) {
		/** store your objective functions */
		ArrayList<FitnessFunc> funcList = new ArrayList<FitnessFunc>();
		/** perturbation is used in Polynomial Mutation which is a real-value mutation (suggest
		 * value is between 20 and 100.), 
		 * if your chromosome is INT value, then do not worry about that. */
		double perturbation = 20;
		/** mutation rate */
		double mutationRate = 0.1;
		/** crossover rate */
		double crossoverRate = 0.7;
		
		/** lower and upper bound of X */
		double lbound = -30; // ranging in [-30, 30]
		double ubound = 30;
		
		/** tournament selection size */
		int tournamentSize = 10;
		
		/** the size of elitism */
		int elitSize = 10;
		
		/** optimization direction 0 denotes minimization, 1 denotes maximization */
		int optimization = 0; //minimize
		
		/** population size */
		int popSize = 50;
		
		/** maximum generation number */
		int maxGen = 100;
		int d = 20; // number of dimensions

		/** Initialize fitness function */
		FitnessFunc fitnessFunction = new FitnessFunc(RosenbrockUnNormalizedFit.class);
		funcList.add(fitnessFunction);

		/** register fitness function into your evaluation function */
		Evaluate evaluate = new RosenbrockEvaluate(funcList);

		/** register evaluation in problem parameter settings*/
		ProblemParameterSettings proSet = new RosenbrockParameterSettings(evaluate);
		
		/** Initialize algorithm parameter settings */
		ParameterSettings pars = new ParameterSettings(
									mutationRate, crossoverRate, lbound,
									ubound, tournamentSize, elitSize, optimization,
									popSize, maxGen, d);
		
		/** set up DataCollector */
		DataCollector collector = new ResultCollector();

		/** select a type of genetic algorithm, initialize it with a factory */
		GeneticAlgorithm myAlg = new RealGA(pars, proSet, new RealGAFactory(
																collector, lbound,
																ubound, perturbation));



		/** start to run */
//		myAlg.run(233333); // parameter is a random seed
		myAlg.runNtimes(23333, 30);
		
		/** print results */
		((ResultCollector) collector).printBestInRuns(maxGen);
		((ResultCollector) collector).printMeanTime();

//		((ResultCollector) collector).printPop();
		System.out.println("Done!");
	}
}
