import ProblemDefine.ParameterSettings;
import ProblemDefine.ProblemParameterSettings;
import algorithms.*;
import dataCollector.DataCollector;
import gaFactory.GAFactory;


import java.util.ArrayList;

public class Experiment {

    public static void main(String[] args){
        int run = 30;

        experimentRunner(
                run,
                NumOfVmTypes.Five,
                LVM.NEQ,// the largest VM not equals PM
                TestCaseSizes.One
                        );

    }

    public static void experimentRunner(
            int run,
            NumOfVmTypes numOfVmTypes,
            LVM largestVM,
            TestCaseSizes testCase){
        int seed = 2333;

        // fitness function list
        ArrayList<FitnessFunc> funcList = new ArrayList<FitnessFunc>();

        // dummy variables
        double lbound = 0;
        double ubound = 2;
        // dummy variables


        // Algorithm parameters
        double crossoverRate = 0.7;
        double mutationRate = 0.8;
        int optimization = 0; // minimize
        int tournamentSize = 7;
        int eliteSize = 10;
        int popSize = 100;
        int maxGen = 1000;
        double k = 0.7;
        double consolidationFactor = 0.1;

        // experiment related parameters
        double vmCpuOverheadRate = 0.1;
        double vmMemOverhead = 200;
        int vmTypes = numOfVmTypes.getNumOfVmTypes();
        int testCaseSize = testCase.getTestSize();


        // These are the addresses of test cases
        String base = "/home/tanboxi/IdeaProjects/MaxTan/data/";
        String ConfigPath = base + "/baseConfig/";
        String testCasePath = base + "/containerData/static/" + testCase.getName() + ".csv";
        String VMConfigPath = ConfigPath + "VMConfig/" + largestVM.getName() + "/VMConfig_" + numOfVmTypes.getName() + ".csv";
        String PMConfigPath = ConfigPath + "PMConfig/" + "PMConfig_xSmall.csv";
//        String osPath = base + "OSData/" + "static/" + os.getName() + testCase.getName() + ".csv";

        // These are the addresses of result
        String resultBase = "/local/scratch/tanboxi/staticContainerAllocation/";
        String algorithmBase = resultBase + "cecGA/";
        String testCaseResultPath = algorithmBase + numOfVmTypes.getName() + "/" + testCase.getName() + "/";
        String energyPath = testCaseResultPath + "energy.csv";
        String aveEnergyPath = testCaseResultPath + "aveEnergy.csv";
        String aveCpuMemUtilPath = testCaseResultPath + "aveCpuMemUtil.csv";
        String aveNumOfPmPath = testCaseResultPath + "aveNumOfPm.csv";
        String aveNumOfVmPath = testCaseResultPath + "aveNumOfVm.csv";
        String convergenceCurvePath = testCaseResultPath + "convergenceCurve.csv";
        String aveTimePath = testCaseResultPath + "aveTime.csv";


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
//        double[] taskOs = readFile.getTaskOS();


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

        // Crossover operator
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

        for(int i = 0; i < run; i++) {
            myAlg.run(seed + i);
        }

        PostProcessingUnit postProcessing = new PostProcessingUnit(((ResultsCollector)collector).getResultData(),
                maxGen, run, pmCpu, pmMem);

        System.out.println("Average Energy = " + postProcessing.averageEnergy());
        System.out.println("Average Num of PM = " + postProcessing.averageNoOfPm());
        System.out.println("Average Num of VM = " + postProcessing.averageNoOfVm());
        System.out.println("Average Cpu = " + postProcessing.averageUtil()[0] +
                "Average Mem = " + postProcessing.averageUtil()[1]);

        // Use builder pattern to create a WriteFile object
        WriteFile writeFile = new WriteFile
                .Builder(postProcessing.energy(), energyPath,
                        postProcessing.averageEnergy(), postProcessing.sdEnergy(), aveEnergyPath)
                .setAveCpuMemUtil(postProcessing.averageUtil(), postProcessing.sdUtil(), aveCpuMemUtilPath)
                .setAveNumOfPm(postProcessing.averageNoOfPm(), postProcessing.sdNoOfPm(), aveNumOfPmPath)
                .setAveNumOfVm(postProcessing.averageNoOfVm(), postProcessing.sdNoOfVm(), aveNumOfVmPath)
                .setConvergenceCurve(postProcessing.convergenceCurve(), convergenceCurvePath)
                .setAveTime(collector.meanTime(), collector.sdTime(), aveTimePath)
                .build();

        try {
            writeFile.writeEnergy()
                    .writeAveSdEnergy()
                    .writeAveSdCpuMemUtil()
                    .writeAveSdNumOfPm()
                    .writeAveSdNumOfVm()
                    .writeConvergenceCurve()
                    .writeAveSdTime();
        } catch (Exception e){
            e.printStackTrace();
        }


    }





}
