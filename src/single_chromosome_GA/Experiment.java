package single_chromosome_GA;

import ProblemDefine.ParameterSettings;
import ProblemDefine.ProblemParameterSettings;
import dataCollector.DataCollector;
import gaFactory.GAFactory;
import util.*;
import algorithms.*;

import java.util.ArrayList;

public class Experiment {

    public static void main(String[] args) {
        String algorithmName = "singleGA";
        Configure configure = new Configure(args[0], algorithmName);
        int seed = Integer.parseInt(args[1]);
        experimentRunner(configure, seed);
    }

    public static void experimentRunner(Configure configure, int seed) {
        int run = configure.getRun();

        // fitness function list
        ArrayList<FitnessFunc> funcList = new ArrayList<>();

        // dummy variables
        double lbound = configure.getLbound();
        double ubound = configure.getUbound();
        // dummy variables


        // Algorithm parameters
        double crossoverRate = configure.getCrossoverRate();
        double mutationRate = configure.getMutationRate();
        int optimization = configure.getOptimization(); // minimize
        int tournamentSize = configure.getTournamentSize();
        int eliteSize = configure.getEliteSize();
        int popSize = configure.getPopSize();
        int maxGen = configure.getMaxGen();

        // experiment related parameters
        double vmCpuOverheadRate = configure.getVmCpuOverheadRate();
        double vmMemOverhead = configure.getVmMemOverhead();
        double k = configure.getK();
        int vmTypes = configure.getNumOfVmTypes();
        int testCaseSize = configure.getTestCaseSize();


        // These are the addresses of test cases
        String testCasePath = configure.getTestCasePath();
        String VMConfigPath = configure.getVMConfigPath();
        String PMConfigPath = configure.getPMConfigPath();

        // These are the addresses of result
        String energyPath = configure.getEnergyPath();
        String wastedResourcePath = configure.getWastedResourcePath();
        String cpuMemUtilPath = configure.getCpuMemUtilPath();
        String numOfPmPath = configure.getNumOfPmPath();
        String numOfVmPath = configure.getNumOfVmPath();
        String convergenceCurvePath = configure.getConvergenceCurvePath();
        String timePath = configure.getTimePath();


        ReadFile readFile = new ReadFile(vmTypes, testCaseSize,
                testCasePath, PMConfigPath,
                VMConfigPath);

        double pmCpu = readFile.getPMCpu();
        double pmMem = readFile.getPMMem();
        double pmEnergy = readFile.getPMEnergy();
        double[] vmMem = readFile.getVMMem();
        double[] vmCpu = readFile.getVMCpu();

        double[] taskCpu = readFile.getTaskCpu();
        double[] taskMem = readFile.getTaskMem();

        // we assume the number of VM equals the size of containers.
        int maxNumOfVm = testCaseSize;

        // Initialization

        InitPop initMethod = new SingleGAInitialization(
                testCaseSize, vmTypes, seed,
                vmCpu, vmMem, taskCpu, taskMem,
                vmCpuOverheadRate, vmMemOverhead);


        // Rearrangement operator
//        GroupGARearrangement rearrangement = new GroupGARearrangement(taskCpu, taskMem,
//                vmCpu, vmMem, vmCpuOverheadRate, vmMemOverhead,
//                pmCpu, pmMem, k, pmEnergy);

        // Mutation operator
        Mutation mutation = new SingleGAMutation(
                testCaseSize, vmTypes, seed,
                vmCpu, vmMem,
                taskCpu, taskMem,
                vmCpuOverheadRate,
                vmMemOverhead);

        // Crossover operator
        Crossover crossover = new SingleGACrossover(
                testCaseSize, vmTypes, seed,
                vmCpu, vmMem,
                taskCpu, taskMem,
                vmCpuOverheadRate,
                vmMemOverhead);

        // fitness function
        UnNormalizedFit energyFitness = new EnergyFitness(testCaseSize,
                vmTypes, k, pmCpu, pmMem,
                pmEnergy, vmCpu, vmMem,
                taskCpu, taskMem, vmCpuOverheadRate,
                vmMemOverhead);

        // the framework wrapper
        FitnessFunc energyFit = new FitnessFunc(energyFitness.getClass());

        // add to the fitness function list
        funcList.add(energyFit);

        // Evaluation method
        Evaluate evaluate = new EnergyEvaluation(funcList);

        // Collector
        DataCollector collector = new ResultsCollector();

        ProblemParameterSettings proSet = new SingleGAParameterSettings(
                evaluate, initMethod, mutation, crossover,
                vmTypes, testCaseSize, maxNumOfVm, pmCpu, pmMem, pmEnergy,
                vmCpu, vmMem, taskCpu, taskMem
        );

        // Algorithm related parameters
        ParameterSettings pars = new ParameterSettings(mutationRate, crossoverRate, lbound, ubound,
                tournamentSize, eliteSize, optimization, popSize, maxGen, testCaseSize, seed);

        // factory
        GAFactory factory = new SingleGAFactory(collector, proSet, pars);

        GeneticAlgorithm myAlg = new SingleGA(pars, proSet, factory);

//        for(int i = 0; i < run; i++) {
        myAlg.run(seed);
//            System.out.println("finished run " + i);
//        }

        PostProcessingUnit postProcessing = new PostProcessingUnit(((ResultsCollector)collector).getResultData(),
                ((ResultsCollector)collector).getGenTime(), collector.getTime(),
                maxGen, run);

        System.out.println("Average Energy = " + postProcessing.energy());
        System.out.println("Average Num of PM = " + postProcessing.noOfPm());
        System.out.println("Average Num of VM = " + postProcessing.noOfVm());
        System.out.println("Average Cpu = " + postProcessing.averageUtil()[0] +
                " ,Average Mem = " + postProcessing.averageUtil()[1]);

        WriteFile writeFile = new WriteFile
                .Builder()
                .setWastedResource(postProcessing.waste(), wastedResourcePath)
                .setCpuMemUtil(postProcessing.averageUtil(), cpuMemUtilPath)
                .setNumOfPm(postProcessing.noOfPm(), numOfPmPath)
                .setNumOfVm(postProcessing.noOfVm(), numOfVmPath)
                .setConvergenceCurve(postProcessing.convergenceCurve(), convergenceCurvePath)
                .setTime(postProcessing.getTime(), timePath)
                .build();

        try {
            writeFile.writeEnergy()
                    .writeWastedResource()
                    .writeCpuMemUtil()
                    .writeNumOfPm()
                    .writeNumOfVm()
                    .writeConvergenceCurve()
                    .writeTime();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}

