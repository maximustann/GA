package wsrp;
import java.io.IOException;
import java.util.ArrayList;

import ProblemDefine.*;
import algorithms.*;
import dataCollector.DataCollector;
import multi_objective_operators.CrowdingDistance;
public class Experiment {
	public static void main(String[] arg) throws IOException {
		ArrayList<FitnessFunc> funcList = new ArrayList<FitnessFunc>();
		double[] weights = new double[2];
		
		
		// dummy variables
		double lbound = 0;
		double ubound = 2;
		// dummy variables
		
		
		double crossoverRate = 0.6;
		double mutationRate = 0.5;
		double consolidationCoefficient = 0.7;
		int optimization = 0; //minimize
		int tournamentSize = 10;
		int eliteSize = 20;
		int popSize = 50;
		int maxGen = 200;
		double k = 0.7;
		weights[0] = weights[1] = 0.5;

		int testCase = 5;
		String base = "/Users/maximustann/Documents/workspace/WSRData/testCase" + testCase;
		String ProblemConfig = base + "/ProblemConfig.csv";
		String PMConfig = base + "/PMConfig.csv";
		String VMConfig = base + "/VMConfig.csv";
		String VMCostAddr = base + "/VMCost.csv";
		String taskFreqAddr = base + "/taskFreq.csv";
		String taskCpuAddr = base + "/taskCpu.csv";
		String taskMemAddr = base + "/taskMem.csv";
		String taskSRAddr = base + "/taskSR.csv";
//		String utilizationThresholdAddr = base + "/utilizationThreshold.csv";

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
												taskSRAddr
												);

		int vmTypes = (int) readFiles.getVMTypes();
		int taskNum = (int) readFiles.getTaskNum();
		double pmCpu = readFiles.getPMCpu();
		double pmMem = readFiles.getPMMem();
		double pmEnergy = readFiles.getPMEnergy();
		double[] vmMem = readFiles.getVMMem();
		double[] vmCpu = readFiles.getVMCpu();
		double[] vmCost = readFiles.getVMCost();
		double[] taskCpu = readFiles.getTaskCpu();
		double[] taskMem = readFiles.getTaskMem();
		double[] taskFreq = readFiles.getTaskFreq();
		
//		WriteFileWSPR writeFiles = new WriteFileWSRP(
//				fitnessAddr,
//				timeResultAddr
//					);	

//		// Initialization !!!!
		InitPop initMethod = new InitWSRP_FirstFit(
											taskNum, vmTypes, pmMem,
											pmCpu, vmMem, vmCpu, 
											taskCpu, taskMem, taskFreq);
		
		Mutation mutation = new WSRPMutation(
											taskNum, vmTypes, pmMem,
											pmCpu, vmMem, vmCpu, 
											taskCpu, taskMem, taskFreq, 
											consolidationCoefficient);
		
		UnNormalizedFit cost = new WSRPCostFitness(vmCost);
		UnNormalizedFit energy = new WSRPEnergyFitness(
											taskNum, vmTypes, k, pmCpu,
											pmMem, pmEnergy, vmCpu,
											vmMem, taskCpu, taskFreq);
		
		FitnessFunc costFit = new FitnessFunc(cost.getClass());
		FitnessFunc energyFit = new FitnessFunc(energy.getClass());
		
		WSRP_Constraint constraint = new WSRP_Constraint(
								taskNum, vmTypes, pmCpu,
								pmMem, pmEnergy, vmCpu,
								vmMem, taskCpu, taskMem, taskFreq);
		
		
		funcList.add(costFit);
		funcList.add(energyFit);
		// debug
//		WSRP_IntChromosome[] popVar = (WSRP_IntChromosome[]) initMethod.init(popSize, 3 * taskNum, lbound, ubound);
//		int[] popViolation = new int[popSize];
//		for(int i = 0; i < popSize; i++){
//			mutation.update(popVar[i], mutationRate);
//		}
//		constraint.punish(popVar, popViolation);
//
//		ArrayList<double[]> energyFitness = energyFit.execute(popVar);
//		ArrayList<double[]> costFitness = costFit.execute(popVar);
//		double totalEnergy = 0;
//		for(int i = 0; i < popSize; i++) totalEnergy += energyFitness.get(i)[0];
//		System.out.println("Mean Energy = " + totalEnergy / popSize);
//		for(int i = 0; i < popSize; i++){
//			System.out.println("cost fitness = " + costFitness.get(i)[0]);
//			System.out.println("Energy fitness = " + energyFitness.get(i)[0]);
//			System.out.println("violationNum = " + popViolation[i]);
//			popVar[i].print();
//			System.out.println();
//		}
		// debug

		Evaluate evaluate = new WSRPEvaluate(funcList);
		DataCollector collector = new ResultCollector();
		Distance crowd = new CrowdingDistance(optimization);
//
		ProblemParameterSettings proSet = new AllocationParameterSettings(
												evaluate, initMethod, mutation, constraint, crowd,
												vmTypes,taskNum,
												pmCpu, pmMem, pmEnergy,
												vmCpu, vmMem, vmCost, 
												taskCpu, taskMem, taskFreq);
		ParameterSettings pars = new ParameterSettings(
									mutationRate, crossoverRate, lbound, ubound, tournamentSize,
									eliteSize, optimization, popSize, maxGen, taskNum * 3);
		GeneticAlgorithm myAlg = new WSRPNSGAII(pars, proSet, new NSGAIIFactory(collector, proSet, pars));
		myAlg.run(1);
//		myAlg.runNtimes(2333, 30);
		((ResultCollector) collector).printResult();
//		((ResultCollector) collector).mean(30);
//		((ResultCollector) collector).printMeanTime();
//		writeFiles.writeResults(((ResultCollector) collector).getLastResult(30, maxGen), 
//				((ResultCollector) collector).getTime());
//		System.out.println("Done!");
	}
}
