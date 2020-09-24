package cecGA.src;

import ProblemDefine.ParameterSettings;
import ProblemDefine.ProblemParameterSettings;
import algorithms.*;
import dataCollector.DataCollector;
import gaFactory.GAFactory;
import util.*;
import util.ReadFile;


import java.util.ArrayList;

public class Experiment {

    public static void main(String[] args){
        String algorithmName = "cecGA";
        Configure configure = new Configure(args[0], algorithmName);
        int seed = Integer.parseInt(args[1]);
        experimentRunner(configure, seed);

    }

    public static void experimentRunner(Configure configure, int seed){
        int run = configure.getRun();
//        int seed = configure.getSeed();

        // fitness function list
        ArrayList<FitnessFunc> funcList = new ArrayList<FitnessFunc>();

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

        util.ReadFile readFile = new ReadFile(vmTypes, testCaseSize,
                testCasePath, PMConfigPath,
                VMConfigPath);

        double pmCpu = readFile.getPMCpu();
        double pmMem = readFile.getPMMem();
        double pmEnergy = readFile.getPMEnergy();
        double[] vmMem = readFile.getVMMem();
        double[] vmCpu = readFile.getVMCpu();
        double[] taskCpu = readFile.getTaskCpu();
        double[] taskMem = readFile.getTaskMem();
        double consolidationFactor = 0.5;


        // we assume the number of VM equals the size of containers.
        int maxNumOfVm = testCaseSize;

        // Initialization
        InitPop initMethod = new CecGAInitialization(testCaseSize, maxNumOfVm,
                                                    vmTypes, seed, vmCpu, vmMem,
                                                    taskCpu, taskMem, vmCpuOverheadRate,
                                                    vmMemOverhead, consolidationFactor);

        // Generate the population
        Chromosome[] popVar = initMethod.init(popSize, 0, lbound, ubound);

        // Mutation operator
        Mutation mutation = new CecGAMutation(vmTypes, maxNumOfVm, testCaseSize,
                                                vmCpu, vmMem, taskCpu, taskMem,
                                                vmCpuOverheadRate, vmMemOverhead,
                                                consolidationFactor, seed);

//        Crossover crossover = new (seed);

        // fitness function
        UnNormalizedFit energyFitness = new EnergyFitness(testCaseSize,
                                                            vmTypes, k,
                                                            pmCpu, pmMem,
                                                            pmEnergy, vmCpu,
                                                            vmMem, taskCpu,
                                                            taskMem, vmCpuOverheadRate,
                                                            vmMemOverhead);

        // the framework wrapper
        FitnessFunc energyFit = new FitnessFunc(energyFitness.getClass());

        // add to the fitness function list
        funcList.add(energyFit);

        // Evaluation method
        Evaluate evaluate = new EnergyEvaluation(funcList);

        // Collector
        DataCollector collector = new ResultsCollector();

        ProblemParameterSettings proSet = new CecGAParameterSettings(
                evaluate, initMethod, mutation,
                vmTypes, testCaseSize, maxNumOfVm, pmCpu, pmMem, pmEnergy,
                vmCpu, vmMem, taskCpu, taskMem
        );

        // Algorithm related parameters
        ParameterSettings pars = new ParameterSettings(mutationRate, crossoverRate, lbound, ubound,
                tournamentSize, eliteSize, optimization, popSize, maxGen, testCaseSize, seed);

        // factory
        GAFactory factory = new CecGAFactory(collector, proSet, pars);

        GeneticAlgorithm myAlg = new CecGA(pars, proSet, factory);

        myAlg.run(seed);

        PostProcessingUnit postProcessing = new PostProcessingUnit(((ResultsCollector)collector).getResultData(),
                ((ResultsCollector)collector).getGenTime(), ((ResultsCollector) collector).getTime(),
                maxGen, run, pmCpu, pmMem);

        System.out.println("Average Energy = " + postProcessing.energy());
        System.out.println("Average Num of PM = " + postProcessing.noOfPm());
        System.out.println("Average Num of VM = " + postProcessing.noOfVm());
        System.out.println("Average Cpu = " + postProcessing.averageUtil()[0] +
                "Average Mem = " + postProcessing.averageUtil()[1]);

        // Use builder pattern to create a WriteFile object
        WriteFile writeFile = new WriteFile
                .Builder()
                .setEnergy(postProcessing.energy(), energyPath)
                .setCpuMemUtil(postProcessing.averageUtil(), cpuMemUtilPath)
                .setWastedResource(postProcessing.waste(), wastedResourcePath)
                .setNumOfPm(postProcessing.noOfPm(), numOfPmPath)
                .setNumOfVm(postProcessing.noOfVm(), numOfVmPath)
                .setConvergenceCurve(postProcessing.convergenceCurve(), convergenceCurvePath)
                .setTime(postProcessing.getTime(),timePath)
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
