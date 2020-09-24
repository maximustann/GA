package multiDGA;

import ProblemDefine.ParameterSettings;
import ProblemDefine.ProblemParameterSettings;
import algorithms.*;
import dataCollector.DataCollector;
import gaFactory.GAFactory;
import multi_objective_operators.CrowdingDistance;


import java.util.ArrayList;

public class Experiment {

    public static void main(String[] args){

        String algorithmName = "permutationBasedGA";
        Configure configure = new Configure(args[0], algorithmName);
        int seed = Integer.parseInt(args[1]);
        experimentRunner(configure, seed);
    }

    public static void experimentRunner(Configure configure, int seed){
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
        double crushPro = configure.getCrushPro();
        int vmTypes = configure.getNumOfVmTypes();
        int testCaseSize = configure.getTestCaseSize();


        // These are the addresses of test cases
        String testCasePath = configure.getTestCasePath();
        String VMConfigPath = configure.getVMConfigPath();
        String PMConfigPath = configure.getPMConfigPath();

        // These are the addresses of result
        String allResultsPath = configure.getParetoFrontPath();
        String energyPath = configure.getEnergyPath();
        String wastedResourcePath = configure.getWastedResourcePath();
        String aveEnergyPath = configure.getAveEnergyPath();
        String aveCpuMemUtilPath = configure.getAveCpuMemUtilPath();
        String aveNumOfPmPath = configure.getAveNumOfPmPath();
        String aveNumOfVmPath = configure.getAveNumOfVmPath();
        String convergenceCurvePath = configure.getConvergenceCurvePath();
        String aveTimePath = configure.getAveTimePath();

        ReadFile readFile = new ReadFile(vmTypes, testCaseSize,
                                        testCasePath, PMConfigPath,
                                        VMConfigPath);

        double pmCpu = readFile.getPMCpu();
        double pmMem = readFile.getPMMem();
        double pmEnergy = readFile.getPMEnergy();
        double[] vmMem = readFile.getVMMem();
        double[] vmCpu = readFile.getVMCpu();
        double[] containerCpu = readFile.getTaskCpu();
        double[] containerMem = readFile.getTaskMem();
        double[] containerAppId = readFile.getTaskAppId();
        double[] containerMicroId = readFile.getTaskMicroServiceId();
        double[] containerReplicaId = readFile.getTaskReplicaId();


        // we assume the number of VM equals the size of containers.
        int maxNumOfVm = testCaseSize;

        // Initialization
//        InitPop initMethod = new DualPermutationInitializationFF(testCaseSize, maxNumOfVm,
//                                                                vmTypes, seed, pmCpu, pmMem,
//                                                                k, pmEnergy, vmCpu, vmMem,
//                                                                containerCpu, containerMem,
//                                                                vmCpuOverheadRate, vmMemOverhead);
        InitPop initMethod = new DualPermutationInitialization(testCaseSize, maxNumOfVm,
                vmTypes, seed, vmCpu, vmMem,
                containerCpu, containerMem,
                vmCpuOverheadRate, vmMemOverhead);
        // Generate the population
//        Chromosome[] popVar = initMethod.init(popSize, 0, lbound, ubound);

        // Mutation operator
        Mutation mutation = new DualPermutationMutation(vmTypes, maxNumOfVm, testCaseSize, vmCpu, vmMem, containerCpu, containerMem,
                                                        vmCpuOverheadRate, vmMemOverhead, seed);

        // Crossover operator
        Crossover crossover = new DualPermutationCrossover(seed);

        // fitness function, NF-based
//        UnNormalizedFit energyFitness = new EnergyFitness(
//                testCaseSize,
//                maxNumOfVm,
//                k,
//                pmCpu,
//                pmMem,
//                pmEnergy,
//                crushPro,
//                vmCpuOverheadRate,
//                vmMemOverhead,
//                vmCpu, vmMem,
//                containerCpu, containerMem,
//                containerAppId, containerMicroId,
//                containerReplicaId
//        );



//        // fitness function, FF-based
        UnNormalizedFit energyFitness = new EnergyFitnessFFDecoding(
                testCaseSize,
                maxNumOfVm,
                k,
                pmCpu,
                pmMem,
                pmEnergy,
                crushPro,
                vmCpuOverheadRate,
                vmMemOverhead,
                vmCpu, vmMem,
                containerCpu, containerMem,
                containerAppId, containerMicroId,
                containerReplicaId
        );

        UnNormalizedFit availabilityFitness = new AvailabilityFitness();

        // the framework wrapper
        FitnessFunc energyFit = new FitnessFunc(energyFitness.getClass());
        FitnessFunc availabilityFit = new FitnessFunc(availabilityFitness.getClass());

        // add to the fitness function list
        funcList.add(energyFit);
        funcList.add(availabilityFit);

        // Evaluation method
//      Evaluate evaluate = new EnergyEvaluation(funcList);
        Evaluation evaluate = new Evaluation(funcList);

        //Distance measure
        Distance crowding = new CrowdingDistance(optimization);
        // Collector
        DataCollector collector = new ResultsCollector();

        ProblemParameterSettings proSet = new DualPermutationParameterSettings(
                evaluate, initMethod, mutation, crossover, crowding,
                vmTypes, testCaseSize, maxNumOfVm, pmCpu, pmMem, pmEnergy,
                vmCpu, vmMem, containerCpu, containerMem
        );

        // Algorithm related parameters
        ParameterSettings pars = new ParameterSettings(mutationRate, crossoverRate, lbound, ubound,
                tournamentSize, eliteSize, optimization, popSize, maxGen, testCaseSize, seed);

        // factory
        GAFactory factory = new DualPermutationFactory(collector, proSet, pars);

//        GeneticAlgorithm myAlg = new DualPermutationGA(pars, proSet, factory);
        GeneticAlgorithm myAlg = new NSGAIIDGA(pars, proSet, factory);

//        for(int i = 0; i < run; i++) {
            myAlg.run(seed);
//            System.out.println("finished run " + i);
//        }

        PostProcessingUnit postProcessing = new PostProcessingUnit(((ResultsCollector) collector).getResultData(),
                ((ResultsCollector) collector).getGenTime(),
                maxGen, popSize, run);

//        System.out.println("Average Energy = " + postProcessing.averageEnergy());
        System.out.println("Average Num of PM = " + postProcessing.averageNoOfPm());
        System.out.println("Average Num of VM = " + postProcessing.averageNoOfVm());
        System.out.println("Average waste = " + postProcessing.waste());
        System.out.println("Average Cpu = " + postProcessing.averageUtil()[0] +
                "Average Mem = " + postProcessing.averageUtil()[1]);

        // Use builder pattern to create a WriteFile object
        WriteFile writeFile = new WriteFile
                .Builder(postProcessing.allResults(), allResultsPath)
                .setWastedResource(postProcessing.waste(), wastedResourcePath)
                .setAveCpuMemUtil(postProcessing.averageUtil(), postProcessing.sdUtil(), aveCpuMemUtilPath)
                .setAveNumOfPm(postProcessing.averageNoOfPm(), postProcessing.sdNoOfPm(), aveNumOfPmPath)
                .setAveNumOfVm(postProcessing.averageNoOfVm(), postProcessing.sdNoOfVm(), aveNumOfVmPath)
                .setConvergenceCurve(postProcessing.convergenceCurve(), convergenceCurvePath)
                .setAveTime(collector.meanTime(), collector.sdTime(), aveTimePath)
                .build();

        try {
            writeFile.writeParetoFront()
                    .writeWastedResource()
//                    .writeAveSdEnergy()
                    .writeAveSdCpuMemUtil()
                    .writeAveSdNumOfPm()
                    .writeAveSdNumOfVm()
//                    .writeConvergenceCurve()
                    .writeAveSdTime();
        } catch (Exception e){
            e.printStackTrace();
        }
    }


}
