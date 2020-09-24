package variableReplicasFirstStepGA;

import ProblemDefine.ParameterSettings;
import ProblemDefine.ProblemParameterSettings;
import algorithms.*;
import dataCollector.DataCollector;
import gaFactory.GAFactory;
import multi_objective_operators.CrowdingDistance;

import java.util.ArrayList;

public class Experiment {

    public static void main(String[] args){
        String algorithmName = "nsgaII";
        Configure configure = new Configure(args[0], algorithmName);
        int seed = Integer.parseInt(args[1]);
        experimentRunner(configure, seed);
    }

    public static void experimentRunner(Configure configure, int seed){


        // Permutation number decides the number of permutation of a solution we generate during
        // evaluation of energy consumption
        int permutationNum = configure.getPermutationNum();

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
        int applicationSize = configure.getApplicationSize();
        int microServiceSize = configure.getMicroServiceSize();
        int maximumServiceNum = configure.getMaximumServiceNum();


        // These are the addresses of test cases
        String testCasePath = configure.getTestCasePath();
        String VMConfigPath = configure.getVmConfigPath();
        String PMConfigPath = configure.getPmConfigPath();
        String applicationPath = configure.getApplicationPath();

        // These are the addresses of result
        String paretoFrontPath = configure.getParetoFrontPath();
        String energyPath = configure.getEnergyPath();
        String aveEnergyPath = configure.getAveEnergyPath();
        String wastedResourcePath = configure.getWastedResourcePath();
        String aveCpuMemUtilPath = configure.getAveCpuMemUtilPath();
        String numOfPmPath = configure.getNumOfPmPath();
        String numOfVmPath = configure.getNumOfVmPath();
        String convergenceCurvePath = configure.getConvergenceCurvePath();
        String aveTimePath = configure.getAveTimePath();


        ReadFile readFile = new ReadFile(
                vmTypes, microServiceSize,applicationSize,
                testCasePath, PMConfigPath,
                VMConfigPath, applicationPath);

        ReadMicroServices readMicroServices = new ReadMicroServices(
                applicationSize,
                maximumServiceNum,
                applicationPath);

        double pmCpu = readFile.getPMCpu();
        double pmMem = readFile.getPMMem();
        double pmEnergy = readFile.getPMEnergy();
        double[] vmMem = readFile.getVMMem();
        double[] vmCpu = readFile.getVMCpu();

        double[] containerCpu = readFile.getTaskCpu();
        double[] containerMem = readFile.getTaskMem();
        double[] containerAppId = readFile.getTaskAppId();
        double[] containerMicroServiceId = readFile.getTaskMicroServiceId();
        double[] containerReplicaId = readFile.getTaskReplicaId();
        int [][] microService = readMicroServices.getMicroserviceMatrix();

        // Initialization
        InitPop initMethod = new FirstStepGAInitialization(
                permutationNum, applicationSize, microServiceSize,
                lbound, ubound,
                pmCpu, pmMem, k, pmEnergy, crushPro,
                vmCpuOverheadRate, vmMemOverhead,
                containerCpu, containerMem, containerAppId,
                containerMicroServiceId,
                vmCpu, vmMem
        );


        // Mutation operator
        Mutation mutation = new FirstStepGAMutation((int)lbound, (int)ubound);

        // Crossover operator
        Crossover crossover = new FirstStepGACrossover();

        // fitness functions
        UnNormalizedFit energyFitness = new EnergyFitness();
        UnNormalizedFit availabilityFitness = new AvailabilityFitness();
//
//        // the framework wrapper
        FitnessFunc energyFit = new FitnessFunc(energyFitness.getClass());
        FitnessFunc availabilityFit = new FitnessFunc(availabilityFitness.getClass());

        // add to the fitness function list
        funcList.add(energyFit);
        funcList.add(availabilityFit);

        // Evaluation method
        Evaluate evaluate = new Evaluation(funcList);

        //Distance measure
        Distance crowding = new CrowdingDistance(optimization);

        // Collector
        DataCollector collector = new ResultsCollector();

        ProblemParameterSettings proSet = new FirstStepGAParameterSettings(
                evaluate, initMethod, mutation, crossover, crowding,
                vmTypes, microServiceSize, pmCpu, pmMem, pmEnergy,
                vmCpu, vmMem, containerCpu, containerMem
        );

        // Algorithm related parameters
        ParameterSettings pars = new ParameterSettings(mutationRate, crossoverRate, lbound, ubound,
                tournamentSize, eliteSize, optimization, popSize, maxGen, microServiceSize, seed);

        // factory
        GAFactory factory = new FirstStepGAFactory(collector, proSet, pars);

        GeneticAlgorithm myAlg = new NSGAIIFirstStepGA(pars, proSet, factory);

        myAlg.run(seed);



        PostProcessingUnit postProcessing = new PostProcessingUnit(
                ((ResultsCollector) collector).getResultData(),
                ((ResultsCollector)collector).getGenTime(),
                maxGen);
////
////        System.out.println("Average Energy = " + postProcessing.averageEnergy());
        System.out.println("Average Energy = " + postProcessing.energy());
        System.out.println("The upper bound of availability = " + postProcessing.availability());
//        System.out.println("Average Num of PM = " + postProcessing.averageNoOfPm());
//        System.out.println("Average Num of VM = " + postProcessing.averageNoOfVm());
//        System.out.println("Average Cpu = " + postProcessing.averageUtil()[0] +
//                " ,Average Mem = " + postProcessing.averageUtil()[1]);
//
        // Use builder pattern to create a WriteFile object
//        WriteFile writeFile = new WriteFile
//                .Builder(postProcessing.allParetoFront(), paretoFrontPath)
//                .setWastedResource(postProcessing.waste(), wastedResourcePath)
//                .setAveCpuMemUtil(postProcessing.averageUtil(), postProcessing.sdUtil(), aveCpuMemUtilPath)
//                .setAveNumOfPm(postProcessing.averageNoOfPm(), postProcessing.sdNoOfPm(), numOfPmPath)
//                .setAveNumOfVm(postProcessing.averageNoOfVm(), postProcessing.sdNoOfVm(), numOfVmPath)
//                .setConvergenceCurve(postProcessing.convergenceCurve(), convergenceCurvePath)
//                .setAveTime(collector.meanTime(), collector.sdTime(), aveTimePath)
//                .build();
//
//        try {
//            writeFile.writeParetoFront()
//                    .writeWastedResource()
//                    .writeAveSdCpuMemUtil()
//                    .writeAveSdNumOfPm()
//                    .writeAveSdNumOfVm()
//                    .writeConvergenceCurve()
//                    .writeAveSdTime();
//        } catch (Exception e){
//            e.printStackTrace();
//        }
    }
}
