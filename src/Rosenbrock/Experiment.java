package Rosenbrock;
import java.util.ArrayList;
import ProblemDefine.*;
import algorithm.Evaluate;
import algorithm.FitnessFunction;
import algorithm.GeneticAlgorithm;
import dataCollector.DataCollector;
import gaFactory.RealGA;
import gaFactory.RealGAFactory;

public class Experiment {
	public static void main(String[] arg) {
		ArrayList<FitnessFunction> funcList = new ArrayList<FitnessFunction>();
		double mutationRate = 0.2;
		double crossoverRate = 0.7;
		double lbound = -30; // ranging in [-30, 30]
		double ubound = 30;
		int tournamentSize = 10;
		int optimization = 0; //minimize
		int popSize = 50;
		int maxGen = 10000;
		int d = 20; // number of dimensions
//		double threshold = 0.7;

		// Initialization !!!!
		FitnessFunction fitnessFunction = new TestFunctionFitness();
		funcList.add(fitnessFunction);

		// evaluation
		Evaluate evaluate = new TestFunctionEvaluate(funcList);


		ProblemParameterSettings proSet = new TestFunctionParameterSettings(evaluate);
		ParameterSettings pars = new ParameterSettings(mutationRate, crossoverRate, lbound, ubound, tournamentSize,
														optimization, popSize, maxGen, d);
		DataCollector collector = new ResultCollector();

		GeneticAlgorithm myAlg = new RealGA(pars, proSet, new RealGAFactory(collector, lbound, ubound));


		// global version
//		PSO myAlg = new BPSO(pars, proSet, new OriginalBPSOFactory(collector));


		myAlg.run(111); // parameter is a random seed
		((ResultCollector) collector).printResult();
//		((ResultCollector) collector).printParticle();
		System.out.println("Done!");
	}
}
