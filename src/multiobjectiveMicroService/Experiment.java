package multiobjectiveMicroService;

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
        int containerSize = configure.getTestCaseSize();
        int applicationSize = configure.getApplicationSize();
        int maximumServiceNum = configure.getMaximumServiceNum();


        // These are the addresses of test cases
        String testCasePath = configure.getTestCasePath();
        String VMConfigPath = configure.getVMConfigPath();
        String PMConfigPath = configure.getPMConfigPath();
        String applicationPath = configure.getApplicationPath();

        // These are the addresses of result
        String paretoFrontPath = configure.getParetoFrontPath();
        String energyPath = configure.getEnergyPath();
        String aveEnergyPath = configure.getAveEnergyPath();
        String wastedResourcePath = configure.getWastedResourcePath();
        String aveCpuMemUtilPath = configure.getAveCpuMemUtilPath();
        String aveNumOfPmPath = configure.getAveNumOfPmPath();
        String aveNumOfVmPath = configure.getAveNumOfVmPath();
        String convergenceCurvePath = configure.getConvergenceCurvePath();
        String aveTimePath = configure.getAveTimePath();


        ReadFile readFile = new ReadFile(vmTypes, containerSize,
                                        testCasePath, PMConfigPath,
                                        VMConfigPath);

//        ReadMicroServices readMicroServices = new ReadMicroServices(
//                applicationSize,
//                maximumServiceNum,
//                applicationPath);

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
//        int [][] microService = readMicroServices.getMicroserviceMatrix();

        // we assume the number of VM equals the size of containers.
        int maxNumOfVm = containerSize;

        // Initialization
        InitPop initMethod = new MultiGroupGAInitialization(containerSize, pmCpu, pmMem, k, pmEnergy, crushPro,
                                                        vmCpu, vmMem, containerCpu, containerMem,
                                                        containerAppId, containerMicroServiceId, containerReplicaId,
                                                        vmCpuOverheadRate, vmMemOverhead);


        // Rearrangement operator
        MultiGroupGARearrangement rearrangement = new MultiGroupGARearrangement(
                                                                    containerCpu, containerMem,
                                                                    containerAppId, containerMicroServiceId, containerReplicaId,
                                                                    vmCpu, vmMem, vmCpuOverheadRate, vmMemOverhead,
                                                                    pmCpu, pmMem, k, pmEnergy, crushPro);
        // Mutation operator
        Mutation mutation = new MultiGroupGAMutation(rearrangement,
                                                containerCpu, containerMem,
                                                containerAppId, containerMicroServiceId, containerReplicaId,
                                                vmCpu, vmMem,
                                                pmCpu, pmMem,
                                                vmCpuOverheadRate,
                                                vmMemOverhead);

        // Crossover operator
        Crossover crossover = new MultiGroupGACrossover(rearrangement, (MultiGroupGAInitialization) initMethod);
//        Crossover crossover = new GroupGACrossoverDiversity(rearrangement, (GroupGAInitialization) initMethod);
//
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

        ProblemParameterSettings proSet = new MultiGroupGAParameterSettings(
                evaluate, initMethod, mutation, crossover, crowding,
                vmTypes, containerSize, maxNumOfVm, pmCpu, pmMem, pmEnergy,
                vmCpu, vmMem, containerCpu, containerMem
        );

        // Algorithm related parameters
        ParameterSettings pars = new ParameterSettings(mutationRate, crossoverRate, lbound, ubound,
                tournamentSize, eliteSize, optimization, popSize, maxGen, containerSize, seed);

        // factory
        GAFactory factory = new MultiGroupFactory(collector, proSet, pars);

        GeneticAlgorithm myAlg = new NSGAIIGroup(pars, proSet, factory);

//        for(int i = 0; i < run; i++) {
        myAlg.run(seed);
//            System.out.println("finished run " + i);
//        }
//
        PostProcessingUnit postProcessing = new PostProcessingUnit(
                ((ResultsCollector) collector).getResultData(),
                ((ResultsCollector)collector).getGenTime(),
                maxGen, run);
//
//        System.out.println("Average Energy = " + postProcessing.averageEnergy());
//        System.out.println("Average Energy = " + postProcessing.energy());
        System.out.println("Average Num of PM = " + postProcessing.averageNoOfPm());
        System.out.println("Average Num of VM = " + postProcessing.averageNoOfVm());
        System.out.println("Average Cpu = " + postProcessing.averageUtil()[0] +
                " ,Average Mem = " + postProcessing.averageUtil()[1]);
//
        // Use builder pattern to create a WriteFile object
        WriteFile writeFile = new WriteFile
                .Builder(postProcessing.allParetoFront(), paretoFrontPath)
                .setWastedResource(postProcessing.waste(), wastedResourcePath)
                .setAveCpuMemUtil(postProcessing.averageUtil(), postProcessing.sdUtil(), aveCpuMemUtilPath)
                .setAveNumOfPm(postProcessing.averageNoOfPm(), postProcessing.sdNoOfPm(), aveNumOfPmPath)
                .setAveNumOfVm(postProcessing.averageNoOfVm(), postProcessing.sdNoOfVm(), aveNumOfVmPath)
//                .setConvergenceCurve(postProcessing.convergenceCurve(), convergenceCurvePath)
                .setAveTime(collector.meanTime(), collector.sdTime(), aveTimePath)
                .build();

        try {
            writeFile.writeParetoFront()
                    .writeWastedResource()
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
