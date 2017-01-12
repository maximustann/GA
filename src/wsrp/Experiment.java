package wsrp;
import java.io.IOException;
import java.util.ArrayList;

import ProblemDefine.*;
import algorithms.*;
import commonRepresentation.IntValueChromosome;
import dataCollector.DataCollector;
import gaFactory.*;
public class Experiment {
	public static void main(String[] arg) throws IOException {
		ArrayList<FitnessFunc> funcList = new ArrayList<FitnessFunc>();
		double[] weights = new double[2];
		
		
		// dummy variables
		double lbound = 0;
		double ubound = 2;
		// dummy variables
		
		
		double crossoverRate = 0.6;
		double mutationRate = 0.2;
		double consolidationCoefficient = 0.7;
		int optimization = 0; //minimize
		int tournamentSize = 10;
		int eliteSize = 20;
		int popSize = 100;
		int maxGen = 250;
		double k = 0.7;
		weights[0] = weights[1] = 0.5;

		int testCase = 1;
		String base = "/Users/maximustann/Documents/workspace/WSRData/testCase" + testCase;
		String ProblemConfig = base + "/ProblemConfig.csv";
		String PMConfig = base + "/PMConfig.csv";
		String VMConfig = base + "/VMConfig.csv";
		String VMCostAddr = base + "/VMCost.csv";
		String taskFreqAddr = base + "/taskFreq.csv";
		String taskCpuAddr = base + "/taskCpu.csv";
		String taskMemAddr = base + "/taskMem.csv";
		String taskSRAddr = base + "/taskSR.csv";
		String utilizationThresholdAddr = base + "/utilizationThreshold.csv";

		String resultBase = "/Users/maximustann/Documents/workspace/WSRData/GA/testCase" + testCase;
		String fitnessAddr = resultBase + "/fitness.csv";
		String timeResultAddr = resultBase + "/time.csv";
		ReadFileWSRP readFiles = new ReadFileWSRP(
												ProblemConfig,
												PMConfig, 
												VMConfig, 
												VMCostAddr, 
												taskCpuAddr,
												taskFreqAddr,
												taskMemAddr,
												taskSRAddr,
												utilizationThresholdAddr
												);

		int vmTypes = (int) readFiles.getVMTypes();
		int taskNum = (int) readFiles.getTaskNum();
		double pmCpu = readFiles.getPMCpu();
		double pmMem = readFiles.getPMMem();
		double pmEnergy = readFiles.getPMEnergy();
		double utilizationThreshold = readFiles.getUtilizationThreshold();
		double[] vmMem = readFiles.getVMMem();
		double[] vmCpu = readFiles.getVMCpu();
		double[] vmCost = readFiles.getVMCost();
		double[] taskCpu = readFiles.getTaskCpu();
		double[] taskMem = readFiles.getTaskMem();
		double[] taskFreq = readFiles.getTaskFreq();
		double[] taskSuccRate = readFiles.getTaskSuccRate();
		
//		WriteFileWSPR writeFiles = new WriteFileWSRP(
//				fitnessAddr,
//				timeResultAddr
//					);	

//		// Initialization !!!!
		InitPop initMethod = new InitWSRP_FirstFit(
											taskNum, vmTypes, pmMem,
											pmCpu, vmMem, vmCpu, 
											taskCpu, taskMem);
		initMethod.init(popSize, 3 * taskNum, lbound, ubound);
		Mutation mutation = new WSRPMutation(
											taskNum, vmTypes, pmMem,
											pmCpu, vmMem, vmCpu, 
											taskCpu, taskMem, consolidationCoefficient);
		
		UnNormalizedFit cost = new WSRPCostFitness(vmCost);
		UnNormalizedFit energy = new WSRPEnergyFitness(
											taskNum, k, pmCpu,
											pmMem, pmEnergy, vmCpu,
											vmMem, taskCpu, taskFreq);
		
		FitnessFunc costFit = new FitnessFunc(cost.getClass());
		FitnessFunc energyFit = new FitnessFunc(energy.getClass());
		
		
		
//		Normalize costLinear = new LinearScaling(Cmax, Cmin);
//		Normalize timeLinear = new LinearScaling(Tmax, Tmin);
//		UnNormalizedFit cost = new GAHaiCostFitness(costMatrix);
//		UnNormalizedFit time = new GAHaiTimeFitness(latencyMatrix, freqMatrix, 
//													noService, noLocation);
//		FitnessFunc costFit = new FitnessFunc(cost.getClass());
//		FitnessFunc timeFit = new FitnessFunc(time.getClass());
//		Normalize[] normalizer = new Normalize[] {costLinear, timeLinear};
//		Constraint[] constraints = new Constraint[] {costCon, timeCon};
//		funcList.add(costFit);
//		funcList.add(timeFit);
//		Evaluate evaluate = new GAHaiEvaluate(funcList, normalizer, constraints, weights);
//		DataCollector collector = new ResultCollector();
//
//		ProblemParameterSettings proSet = new AllocationParameterSettings(evaluate, costMatrix, 
//																	freqMatrix, latencyMatrix);
//		ParameterSettings pars = new ParameterSettings(
//									mutationRate, crossoverRate, lbound, ubound, tournamentSize,
//									eliteSize, optimization, popSize, maxGen, noService * noLocation);
//		GeneticAlgorithm myAlg = new BinaryGA(pars, proSet, new BinaryGAFactory(collector));
////		myAlg.run(1);
//		myAlg.runNtimes(2333, 30);
//		((ResultCollector) collector).printResult();
//		((ResultCollector) collector).mean(30);
//		((ResultCollector) collector).printMeanTime();
//		writeFiles.writeResults(((ResultCollector) collector).getLastResult(30, maxGen), 
//				((ResultCollector) collector).getTime());
//		System.out.println("Done!");
	}
}
