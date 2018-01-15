package BilevelContainerAllocation;

import ProblemDefine.CoGAParameterSettings;
import ProblemDefine.CoGAProblemParameterSettings;
import ProblemDefine.ProblemParameterSettings;
import algorithms.*;
import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;
import commonOperators.*;
import dataCollector.DataCollector;
import gaFactory.IntCoGAFactory;
import gaFactory.IntCoGA;


import java.io.IOException;
import java.util.ArrayList;

public class Experiment {
    public static void main(String[] args) throws IOException {

        // bound for vm types
        double[] vmLbound = {0, 0, 0};
        double[] vmUbound = {1, 1, 4};

        // For all three sub-pops, we use the same crossover rate
        double[] crossoverRate = {0.7, 0.7, 0.7};
        double[] mutationRate = {0.1, 0.1, 0.1};

        // minimization
        int optimization = 0;

        int[] tournamentSize = {3, 3, 3};
        int[] eliteSize = {20, 20, 20};
        int[] popSize = {100, 100, 100};
        int maxGen = 200;
        double k = 0.7;

        int testCase = 1;
        String base = "/home/tanboxi/workspace/BilevelData/testCase" + testCase;
        String ProblemConfig = base + "/ProblemConfig.csv";
        String PMConfig = base + "/PMConfig.csv";
        String VMConfig = base + "/VMConfig.csv";
        String taskCpuAddr = base + "/taskCpu.csv";
        String taskMemAddr = base + "/taskMem.csv";
        String taskOSAddr = base + "/taskOS.csv";

        String resultBase = "/home/tanboxi/workspace/BilevelResult/GA/testCase" + testCase;
        ReadFileBilevel readFiles = new ReadFileBilevel(
                ProblemConfig,
                PMConfig,
                VMConfig,
                taskCpuAddr,
                taskMemAddr,
                taskOSAddr
        );

        int vmTypes = (int) readFiles.getVMTypes();
        int taskNum = (int) readFiles.getTaskNum();
        double pmCpu = readFiles.getPMCpu();
        double pmMem = readFiles.getPMMem();
        double pmEnergy = readFiles.getPMEnergy();
        double[] vmMem = readFiles.getVMMem();
        double[] vmCpu = readFiles.getVMCpu();
        double[] taskCpu = readFiles.getTaskCpu();
        double[] taskMem = readFiles.getTaskMem();
        double[] taskOS = readFiles.getTaskOS();

        int[] maxVars = {taskNum * taskNum, taskNum * taskNum, taskNum * taskNum};
        WriteFileBilevel writeFiles = new WriteFileBilevel(resultBase);
//
//        // Initialization !!!
//
//        // Init Sub-pops
        InitPop initContainerVM = new InitAllocationChromosome();
        InitPop initVMPM = new InitAllocationChromosome();
        InitPop initVmTypes = new InitIntChromosomes();
        InitPop[] initPops = {initContainerVM, initVMPM, initVmTypes};

        // Init Mutations
        Mutation mutateContainerVm = new BinaryFlipCoinMutation();
        Mutation mutateVMPM = new BinaryFlipCoinMutation();
        Mutation mutateTypes = new IntReverseSequenceMutation();
        Mutation[] mutations = {mutateContainerVm, mutateVMPM, mutateTypes};

        // Init Crossovers
        Crossover crossoverContainerVM = new SinglePointCrossover();
        Crossover crossoverVMPM = new SinglePointCrossover();
        Crossover crossoverTypes = new SinglePointCrossover();
        Crossover[] crossovers = {crossoverContainerVM, crossoverVMPM, crossoverTypes};

        // Init Selections
        Selection selectionContainerVM = new TournamentSelection(tournamentSize[0], optimization);
        Selection selectionVMPM = new TournamentSelection(tournamentSize[1], optimization);
        Selection selectionTypes = new TournamentSelection(tournamentSize[2], optimization);
        Selection[] selections = {selectionContainerVM, selectionVMPM, selectionTypes};

        // Init Elitisms
        Elitism elitismContainerVM = new CommonElitism(eliteSize[0], optimization);
        Elitism elitismVMPM = new CommonElitism(eliteSize[1], optimization);
        Elitism elitismTypes = new CommonElitism(eliteSize[2], optimization);
        Elitism[] elitisms = {elitismContainerVM, elitismVMPM, elitismTypes};

        // Init fitness function
        // 1. create fitness function
        CoUnNormalizedFit energy = new BilevelFitness(taskNum, k, pmCpu, pmMem, pmEnergy,
                                                        vmCpu, vmMem, taskCpu, taskMem);
        // 2. add to fitness function list
        ArrayList<CoFitnessFunc> funcList = new ArrayList<CoFitnessFunc>();
        CoFitnessFunc energyFit = new CoFitnessFunc(energy.getClass());
        funcList.add(energyFit);

        // 3. Register the fitness function list to Evaluation
        CoEvaluate evaluate = new BilevelEvaluate(funcList);

        // Init Collector
        DataCollector collector = new ResultCollector();

        // Init Constraints
        Constraint resourceContainerVM = new ResourceConstraint();
        Constraint typeContainerVM = new TypeConstraint();
        Constraint resourceVMPM = new ResourceConstraint();
        Constraint[] constraints = {resourceContainerVM, resourceVMPM, typeContainerVM};

        // Init Parameter Settings
        CoGAProblemParameterSettings proSet = new BilevelParameterSettings(
                                        evaluate, initPops, mutations,
                                        crossovers, selections, elitisms,
                                        constraints, vmTypes, taskNum,
                                        pmCpu, pmMem, pmEnergy, vmCpu,
                                        vmMem, taskCpu, taskMem, taskOS);

        CoGAParameterSettings pars = new CoGAParameterSettings(
                                        mutationRate, crossoverRate, vmLbound,
                                        vmUbound, tournamentSize, eliteSize,
                                        popSize, maxVars, taskNum, optimization, maxGen);

        Coevolution myAlg = new IntCoGA(pars, proSet, new IntCoGAFactory(collector));
        System.out.println("Done!");



    }



}
