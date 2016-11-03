package GaAllocationProblem;
import java.util.ArrayList;

import ProblemDefine.*;
import algorithm.*;
import dataCollector.DataCollector;
import gaFactory.*;
public class Experiment {
	public static void main(String[] arg) {
		ArrayList<FitnessFunction> funcList = new ArrayList<FitnessFunction>();
		double[] weights = new double[2];
		double lbound = 0;
		double ubound = 2;
		double crossoverRate = 0.7;
		double mutationRate = 0.1;
		int optimization = 0; //minimize
		int tournamentSize = 10;
		int eliteSize = 10;
		int popSize = 50;
		int maxGen = 150;
		weights[0] = weights[1] = 0.5;

		double[] costMatrix;
		double[] freqMatrix;
		double[] latencyMatrix;

		int testCase = 1;
		int noService;
		int noLocation;
		double Cmax, Cmin, Tmax, Tmin;
		String base = "/Users/maximustann/Documents/workspace/HaiProjData/testCase" + testCase;
		String configAddr = base + "/config.csv";
		String costAddr = base + "/cost.csv";
		String latencyAddr = base + "/latency.csv";
		String freqAddr = base + "/freq.csv";
		String costRangeAddr = base + "/costRange.csv";
		String timeRangeAddr = base + "/timeRange.csv";

		ReadFileHai readFiles = new ReadFileHai(
												configAddr, 
												costAddr, 
												latencyAddr, 
												freqAddr, 
												costRangeAddr, 
												timeRangeAddr
												);
		costMatrix = readFiles.getCostMatrix();
		latencyMatrix = readFiles.getLatencyMatrix();
		freqMatrix = readFiles.getFreqMatrix();
		noService = readFiles.getNoService();
		noLocation = readFiles.getNoLocation();
		Cmax = readFiles.getCmax();
		Cmin = readFiles.getCmin();
		Tmax = readFiles.getTmax();
		Tmin = readFiles.getTmin();

		// Initialization !!!!
		Constraint costCon = new Constraint(noService);
		Constraint timeCon = new Constraint(noService);
		Normalize costLinear = new LinearScaling(Cmax, Cmin);
		Normalize timeLinear = new LinearScaling(Tmax, Tmin);
		FitnessFunction cost = new GAHaiCostFitness(costLinear, costCon, costMatrix);
		FitnessFunction time = new GAHaiTimeFitness(timeLinear, timeCon, latencyMatrix, 
													freqMatrix, noService, noLocation);
		funcList.add(cost);
		funcList.add(time);
		Evaluate evaluate = new GAHaiEvaluate(funcList, weights);
		DataCollector collector = new ResultCollector();

		ProblemParameterSettings proSet = new AllocationParameterSettings(evaluate, costMatrix, freqMatrix, latencyMatrix);
		ParameterSettings pars = new ParameterSettings(
									mutationRate, crossoverRate, lbound, ubound, tournamentSize,
									eliteSize, optimization, popSize, maxGen, noService * noLocation);
		GeneticAlgorithm myAlg = new BinaryGA(pars, proSet, new BinaryGAFactory(collector));
//		myAlg.run(1);
		myAlg.runNtimes(2333, 30);
		((ResultCollector) collector).printResult();
		((ResultCollector) collector).mean(30);
		System.out.println("Done!");
	}
}
