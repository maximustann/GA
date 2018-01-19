import ProblemDefine.CoGAParameterSettings;
import ProblemDefine.CoGAProblemParameterSettings;
import algorithms.*;
import dataCollector.DataCollector;
import gaFactory.IntCoGAFactory;
import gaFactory.IntCoGA;


import java.io.IOException;
import java.util.ArrayList;

public class Experiment {
    public static void main(String[] args) throws IOException {

        // the number of sub-populations
        int numOfSubPop = 3;

        // bound for vm types
        double[] vmLbound = {0, 0, 0};
        double[] vmUbound = {1, 1, 5};

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
        String fitnessAddr = resultBase + "fitness.csv";
        String timeAddr = resultBase + "/time.csv";
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
        WriteFileBilevel writeFiles = new WriteFileBilevel(fitnessAddr, timeAddr);
        // Init fitness function
        // 1. create fitness function
        CoUnNormalizedFit energy = new BilevelFitness(taskNum, k, pmCpu,
                                                    pmEnergy, vmCpu, taskCpu);
        // 2. add to fitness function list
        ArrayList<CoFitnessFunc> funcList = new ArrayList<CoFitnessFunc>();
        CoFitnessFunc energyFit = new CoFitnessFunc(energy.getClass());
        funcList.add(energyFit);

        // 3. Register the fitness function list to Evaluation
        CoEvaluate evaluate = new BilevelEvaluate(funcList);

        // Init Collector
        DataCollector collector = new ResultCollector();

//        // Init Constraints
//        Constraint resourceContainerVM = new ResourceConstraint();
//        Constraint typeContainerVM = new TypeConstraint();
//        Constraint resourceVMPM = new ResourceConstraint();
//        Constraint[] constraints = {resourceContainerVM, resourceVMPM, typeContainerVM};

        // Init Problem input data
        CoGAProblemParameterSettings proSet = new BilevelParameterSettings(
                                        evaluate, vmTypes, taskNum,
                                        pmCpu, pmMem, pmEnergy, vmCpu,
                                        vmMem, taskCpu, taskMem, taskOS);

        // Init Algorithm parameters
        CoGAParameterSettings pars = new CoGAParameterSettings(
                                        mutationRate, crossoverRate, vmLbound,
                                        vmUbound, tournamentSize, eliteSize,
                                        popSize, maxVars, numOfSubPop, optimization, maxGen);

        Coevolution myAlg = new IntCoGA(pars, proSet, new BilevelFactory(collector, numOfSubPop));
        myAlg.run(2333);
        System.out.println("Done!");
    }



}
