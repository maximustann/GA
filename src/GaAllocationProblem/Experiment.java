package GaAllocationProblem;
import java.io.IOException;
import java.util.ArrayList;

import ProblemDefine.*;
import algorithm.*;
import dataCollector.DataCollector;
import gaFactory.*;
public class Experiment {
	public static void main(String[] arg) throws IOException {
		ArrayList<FitnessFunc> funcList = new ArrayList<FitnessFunc>();
		double[] weights = new double[2];
		double lbound = 0;
		double ubound = 2;
		double crossoverRate = 0.6;
		double mutationRate = 0.2;
		int optimization = 0; //minimize
		int tournamentSize = 10;
		int eliteSize = 20;
		int popSize = 100;
		int maxGen = 250;
		weights[0] = weights[1] = 0.5;

		double[] costMatrix;
		double[] freqMatrix;
		double[] latencyMatrix;

		int testCase = 2;
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
		String resultBase = "/Users/maximustann/Documents/workspace/HaiProjResult/GA/testCase" + testCase;
		String fitnessAddr = resultBase + "/fitness.csv";
		String timeResultAddr = resultBase + "/time.csv";
		ReadFileHai readFiles = new ReadFileHai(
												configAddr, 
												costAddr, 
												latencyAddr, 
												freqAddr, 
												costRangeAddr, 
												timeRangeAddr
												);
		WriteFileHai writeFiles = new WriteFileHai(
				fitnessAddr,
				timeResultAddr
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
		UnNormalizedFit cost = new GAHaiCostFitness(costMatrix);
		UnNormalizedFit time = new GAHaiTimeFitness(latencyMatrix, freqMatrix, 
													noService, noLocation);
		FitnessFunc costFit = new FitnessFunc(cost.getClass());
		FitnessFunc timeFit = new FitnessFunc(time.getClass());
		Normalize[] normalizer = new Normalize[] {costLinear, timeLinear};
		Constraint[] constraints = new Constraint[] {costCon, timeCon};
		funcList.add(costFit);
		funcList.add(timeFit);
		Evaluate evaluate = new GAHaiEvaluate(funcList, normalizer, constraints, weights);
		DataCollector collector = new ResultCollector();

		ProblemParameterSettings proSet = new AllocationParameterSettings(evaluate, costMatrix, 
																	freqMatrix, latencyMatrix);
		ParameterSettings pars = new ParameterSettings(
									mutationRate, crossoverRate, lbound, ubound, tournamentSize,
									eliteSize, optimization, popSize, maxGen, noService * noLocation);
		GeneticAlgorithm myAlg = new BinaryGA(pars, proSet, new BinaryGAFactory(collector));
//		myAlg.run(1);
		myAlg.runNtimes(2333, 30);
		((ResultCollector) collector).printResult();
		((ResultCollector) collector).mean(30);
		((ResultCollector) collector).printMeanTime();
		writeFiles.writeResults(((ResultCollector) collector).getLastResult(30, maxGen), 
				((ResultCollector) collector).getTime());
		System.out.println("Done!");
	}
}
