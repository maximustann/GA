package mixedGAs;
import GroupGA.src.*;
import GroupGA.src.EnergyEvaluation;
import GroupGA.src.EnergyFitness;
import GroupGA.src.ResultsCollector;
import PermutationBasedGAForContainerAllocation.*;
import ProblemDefine.ParameterSettings;
import ProblemDefine.ProblemParameterSettings;
import algorithms.*;
import dataCollector.DataCollector;
import gaFactory.GAFactory;
import util.WriteFile;

import java.util.ArrayList;

public class Experiment {

    public static void main(String[] args) {
        String algorithmName = "mixedGA";
        Configure configure = new Configure(args[0], algorithmName);
        int seed = Integer.parseInt(args[1]);
        experimentRunner(configure, seed);
    }

    public static void experimentRunner(Configure configure, int seed){
        int run = configure.getRun();

        // fitness function list
        ArrayList<FitnessFunc> group_funcList = new ArrayList<>();

        // dummy variables
        double dual_lbound = configure.getDual_lbound();
        double dual_ubound = configure.getDual_ubound();
        double group_lbound = configure.getGroup_lbound();
        double group_ubound = configure.getGroup_ubound();
        // dummy variables


        // dual permutation GA parameters
        double dual_crossoverRate = configure.getDual_crossoverRate();
        double dual_mutationRate = configure.getDual_mutationRate();
        int dual_optimization = configure.getDual_optimization(); // minimize
        int dual_tournamentSize = configure.getDual_tournamentSize();
        int dual_eliteSize = configure.getDual_eliteSize();
        int dual_popSize = configure.getDual_popSize();
        int dual_maxGen = configure.getDual_maxGen();

        // group-based GA parameters
        double group_crossoverRate = configure.getGroup_crossoverRate();
        double group_mutationRate = configure.getGroup_mutationRate();
        int group_optimization = configure.getGroup_optimization(); // minimize
        int group_tournamentSize = configure.getGroup_tournamentSize();
        int group_eliteSize = configure.getGroup_eliteSize();
        int group_popSize = configure.getGroup_popSize();
        int group_maxGen = configure.getGroup_maxGen();

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

        double[] taskCpu = readFile.getTaskCpu();
        double[] taskMem = readFile.getTaskMem();

        // we assume the number of VM equals the size of containers.
        int maxNumOfVm = testCaseSize;

        // Group-based GA Initialization
        InitPop group_initMethod = new GroupGAInitialization(testCaseSize, seed, pmCpu, pmMem, k, pmEnergy,
                vmCpu, vmMem, taskCpu, taskMem,
                vmCpuOverheadRate, vmMemOverhead);


        // Rearrangement operator
        GroupGARearrangement rearrangement = new GroupGARearrangement(taskCpu, taskMem,
                vmCpu, vmMem, vmCpuOverheadRate, vmMemOverhead,
                pmCpu, pmMem, k, pmEnergy);

        // Group-based GA Mutation operator
        Mutation group_mutation = new GroupGAMutation(rearrangement,
                taskCpu, taskMem,
                vmCpu, vmMem,
                pmCpu, pmMem,
                vmCpuOverheadRate,
                vmMemOverhead);

        // Crossover operator
        Crossover group_crossover = new GroupGACrossover(rearrangement, (GroupGAInitialization) group_initMethod);
//        Crossover group_crossover = new GroupGACrossoverDiversity(rearrangement, (GroupGAInitialization) group_initMethod);

        // fitness function
        UnNormalizedFit group_energyFitness = new EnergyFitness();

        // the framework wrapper
        FitnessFunc group_energyFit = new FitnessFunc(group_energyFitness.getClass());

        // add to the fitness function list
        group_funcList.add(group_energyFit);

        // Group-based Evaluation method
        Evaluate group_evaluate = new EnergyEvaluation(group_funcList);

        // Collector
        DataCollector group_collector = new ResultsCollector();

        ProblemParameterSettings group_proSet = new GroupGAParameterSettings(
                group_evaluate, group_initMethod, group_mutation, group_crossover,
                vmTypes, testCaseSize, maxNumOfVm, pmCpu, pmMem, pmEnergy,
                vmCpu, vmMem, taskCpu, taskMem
        );

        // Algorithm related parameters
        ParameterSettings group_pars = new ParameterSettings(group_mutationRate, group_crossoverRate, group_lbound, group_ubound,
                group_tournamentSize, group_eliteSize, group_optimization, group_popSize, group_maxGen, testCaseSize, seed);

        // group GA factory
        GAFactory group_factory = new GroupGAFactory(group_collector, group_proSet, group_pars);

        GeneticAlgorithm groupGA = new GroupGA(group_pars, group_proSet, group_factory);

        //--------------------------------------------------------------------------------
        // fitness function list
        ArrayList<FitnessFunc> dual_funcList = new ArrayList<>();

        // Initialization
        InitPop dual_initMethod = new DualPermutationInitialization(testCaseSize, maxNumOfVm,
                vmTypes, seed, vmCpu, vmMem,
                taskCpu, taskMem,
                vmCpuOverheadRate, vmMemOverhead);

        // Mutation operator
        Mutation dual_mutation = new DualPermutationMutation(vmTypes, maxNumOfVm, testCaseSize,
                                                        vmCpu, vmMem, taskCpu, taskMem,
                                                        vmCpuOverheadRate, vmMemOverhead, seed);

        // Crossover operator
        Crossover dual_crossover = new DualPermutationCrossover(seed);

        // fitness function, FF-based
        UnNormalizedFit dual_energyFitness = new EnergyFitnessFFDecoding(
                testCaseSize,
                maxNumOfVm,
                k,
                pmCpu,
                pmMem,
                pmEnergy,
                vmCpuOverheadRate,
                vmMemOverhead,
                vmCpu, vmMem,
                taskCpu, taskMem
        );

        // the framework wrapper
        FitnessFunc dual_energyFit = new FitnessFunc(dual_energyFitness.getClass());

        // add to the fitness function list
        dual_funcList.add(dual_energyFit);

        // Evaluation method
        Evaluate dual_evaluate = new PermutationBasedGAForContainerAllocation.EnergyEvaluation(dual_funcList);

        // Collector
        DataCollector dual_collector = new PermutationBasedGAForContainerAllocation.ResultsCollector();

        ProblemParameterSettings dual_proSet = new DualPermutationParameterSettings(
                dual_evaluate, dual_initMethod, dual_mutation, dual_crossover,
                vmTypes, testCaseSize, maxNumOfVm, pmCpu, pmMem, pmEnergy,
                vmCpu, vmMem, taskCpu, taskMem
        );

        // Algorithm related parameters
        ParameterSettings dual_pars = new ParameterSettings(dual_mutationRate, dual_crossoverRate, dual_lbound, dual_ubound,
                dual_tournamentSize, dual_eliteSize, dual_optimization, dual_popSize, dual_maxGen, testCaseSize, seed);

        // factory
        GAFactory dual_factory = new DualPermutationFactory(dual_collector, dual_proSet, dual_pars);

        GeneticAlgorithm dualGA = new DualPermutationGA(dual_pars, dual_proSet, dual_factory);

        Adaptor adaptor = new Adaptor(testCaseSize, maxNumOfVm, k, pmCpu, pmMem, pmEnergy, vmCpu, vmMem, taskCpu, taskMem,
                                        vmCpuOverheadRate, vmMemOverhead);

        dualGA.run(seed);
        ((PermutationBasedGAForContainerAllocation.ResultsCollector) dual_collector).printBest();
        ((GroupGA) groupGA).setPopVar(adaptor.convert(
                ((PermutationBasedGAForContainerAllocation.ResultsCollector) dual_collector).getFinalGen()));
        groupGA.run(seed);



        PostProcessingUnit postProcessing = new PostProcessingUnit(
                ((ResultsCollector)group_collector).getResultData(),
                ((PermutationBasedGAForContainerAllocation.ResultsCollector) dual_collector).getResultData(),
                group_maxGen, dual_maxGen, run);

        System.out.println("Average Energy = " + postProcessing.averageEnergy());
//        System.out.println("Average Energy = " + postProcessing.energy());
        System.out.println("Average Num of PM = " + postProcessing.averageNoOfPm());
        System.out.println("Average Num of VM = " + postProcessing.averageNoOfVm());
        System.out.println("Average Cpu = " + postProcessing.averageUtil()[0] +
                " ,Average Mem = " + postProcessing.averageUtil()[1]);
        System.out.println("Waste = " + postProcessing.waste());

        // Use builder pattern to create a WriteFile object
        WriteFile writeFile = new WriteFile
                .Builder(postProcessing.energy(), energyPath,
                postProcessing.averageEnergy(), postProcessing.sdEnergy(), aveEnergyPath)
                .setWastedResource(postProcessing.waste(), wastedResourcePath)
                .setAveCpuMemUtil(postProcessing.averageUtil(), postProcessing.sdUtil(), aveCpuMemUtilPath)
                .setAveNumOfPm(postProcessing.averageNoOfPm(), postProcessing.sdNoOfPm(), aveNumOfPmPath)
                .setAveNumOfVm(postProcessing.averageNoOfVm(), postProcessing.sdNoOfVm(), aveNumOfVmPath)
//                .setConvergenceCurve(postProcessing.convergenceCurve(), convergenceCurvePath)
                .setAveTime(group_collector.meanTime() + dual_collector.meanTime(), group_collector.sdTime() + group_collector.sdTime(), aveTimePath)
                .build();

        try {
            writeFile.writeEnergy()
                    .writeWastedResource()
                    .writeAveSdEnergy()
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
