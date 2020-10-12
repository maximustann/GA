package NSGAII_NSGGA_Manual;

import ProblemDefine.ParameterSettings;
import ProblemDefine.ProblemParameterSettings;
import algorithms.*;
import gaFactory.GAFactory;
import multi_objective_operators.CrowdingDistance;
import variableReplicasFirstStepGA.*;

import java.io.IOException;
import java.util.ArrayList;

public class Exp_NSGAII{

    private static NSGAIIFirstStepGA nsgaii;
    public static void main(String[] args) throws IOException {
        String algorithmName = "NSGAII_NSGGA";
        Configure configure = new Configure(args[0], algorithmName);
        int seed = Integer.parseInt(args[1]);
        experimentRunner(configure, seed);
    }

    public static void experimentRunner(Configure configure, int seed) throws IOException {


        GASettings nsgaiiSettings = new GASettings(
                "nsgaii",
                configure.getNsgaii_ubound(),
                configure.getNsgaii_lbound(),
                configure.getNsgaii_crossoverRate(),
                configure.getNsgaii_mutationRate(),
                configure.getNsgaii_optimization(),
                configure.getNsgaii_tournamentSize(),
                configure.getNsgaii_eliteSize(),
                configure.getNsgaii_popSize(),
                configure.getNsgaii_maxGen(),
                configure.getPermutationNum()
        );

        DataPack dataPack = new DataPack(
                seed,
                configure
        );


        // the addresses of replica nums from the first step GA
//        String replicaNumPath = configure.getReplicaNumPath();
        String replicaNumPath = "replicaNum";
        String replicaFitnessPath = "replicaFitness";
        ReplicaNumWriteFile replicaNumWriteFile = new ReplicaNumWriteFile(replicaNumPath, replicaFitnessPath);

        NSGAIICollector collector = configNSGAII(dataPack, nsgaiiSettings);

        int maxGen = nsgaiiSettings.getMaxGen();
        nsgaii.run(dataPack.getSeed());

        NSGAIIPostProcessingUnit postProcessingUnit = new NSGAIIPostProcessingUnit(
                collector.getResultData(),
                collector.getGenTime(), collector.getTime(),
                maxGen
        );

        ArrayList<FirstStepGAChromosome> paretoFront = postProcessingUnit.getParetoFront();
        int counter = 0;
        for(FirstStepGAChromosome chromosome:paretoFront){
            double[] containerCpu = chromosome.getContainerCpu();
            double[] containerMem = chromosome.getContainerMem();
            double[] containerAppId = chromosome.getContainerAppId();
            double[] containerMicroServiceId = chromosome.getContainerMicroServiceId();
            double[] containerReplicaId = chromosome.getContainerReplicaId();
            replicaNumWriteFile.writeReplicaNum(
                    counter,
                    containerCpu,
                    containerMem,
                    containerAppId,
                    containerMicroServiceId,
                    containerReplicaId
            );
            counter++;
        }
        replicaNumWriteFile.writeFitness(paretoFront, 100);
//        replicaNumWriteFile.writeConverge(postProcessingUnit.getDominantSetList());

//        TwoStepGAProcedure twoStepGAProcedure = new TwoStepGAProcedure(
//                            replicaNumWriteFile,
//                            nsgaiiSettings,
//                            nsggaSettings,
//                            dataPack);



        // These are the addresses of result
//        String paretoFrontPath = configure.getParetoFrontPath();
//        String energyPath = configure.getEnergyPath();
//        String wastedResourcePath = configure.getWastedResourcePath();
//        String cpuMemUtilPath = configure.getCpuMemUtilPath();
//        String numOfPmPath = configure.getNumOfPmPath();
//        String numOfVmPath = configure.getNumOfVmPath();
//        String convergenceCurvePath = configure.getConvergenceCurvePath();
//        String timePath = configure.getTimePath();


        // Start to run the algorithm here.
//        NSGGAPostProcessingUnit postProcessingUnit = twoStepGAProcedure.run();
//
//        WriteFile writeFile = new WriteFile
//                .Builder()
//                .setParetoFront(postProcessingUnit.allParetoFront(), paretoFrontPath)
//                .setWastedResource(postProcessingUnit.waste(), wastedResourcePath)
//                .setCpuMemUtil(postProcessingUnit.averageUtil(), cpuMemUtilPath)
//                .setNumOfPm(postProcessingUnit.noOfPm(), numOfPmPath)
//                .setNumOfVm(postProcessingUnit.noOfVm(),numOfVmPath)
//                .setConvergenceCurve(postProcessingUnit.convergenceCurve(), convergenceCurvePath)
//                .setTime(postProcessingUnit.getAggregatedTime(), timePath)
//                .build();
//
//        try {
//            writeFile.writeParetoFront()
//                    .writeWastedResource()
//                    .writeCpuMemUtil()
//                    .writeNumOfPm()
//                    .writeNumOfVm()
//                    .writeConvergenceCurve()
//                    .writeTime();
//        } catch (Exception e){
//            e.printStackTrace();
//        }
    }

    public static NSGAIICollector configNSGAII(DataPack dataPack, GASettings nsgaiiSettings){

        int seed = dataPack.getSeed();
        // Permutation number decides the number of permutation of a solution we generate during
        // evaluation of energy consumption
        int permutationNum = nsgaiiSettings.getPermutationNum();

        // fitness function list
        ArrayList<FitnessFunc> funcList = new ArrayList<>();

        // dummy variables
        double lbound = nsgaiiSettings.getLbound();
        double ubound = nsgaiiSettings.getUbound();
        // dummy variables


        // Algorithm parameters
        double crossoverRate = nsgaiiSettings.getCrossoverRate();
        double mutationRate = nsgaiiSettings.getMutationRate();
        int optimization = nsgaiiSettings.getOptimization(); // minimize
        int tournamentSize = nsgaiiSettings.getTournamentSize();
        int eliteSize = nsgaiiSettings.getEliteSize();
        int popSize = nsgaiiSettings.getPopSize();
        int maxGen = nsgaiiSettings.getMaxGen();

        // experiment related parameters
        double vmCpuOverheadRate = dataPack.getVmCpuOverheadRate();
        double vmMemOverhead = dataPack.getVmMemOverhead();
        double k = dataPack.getK();
        double crushPro = dataPack.getCrushPro();
        int vmTypes = dataPack.getVmTypes();
        int applicationSize = dataPack.getApplicationSize();
        int microServiceSize = dataPack.getMicroServiceSize();
        int maximumServiceNum = dataPack.getMaximumServiceNum();


        double pmCpu = dataPack.getPmCpu();
        double pmMem = dataPack.getPmMem();
        double pmEnergy = dataPack.getPmEnergy();
        double[] vmMem = dataPack.getVmMem();
        double[] vmCpu = dataPack.getVmCpu();

        double[] serviceCpu = dataPack.getServiceCpu();
        double[] serviceMem = dataPack.getServiceMem();
        double[] serviceId = dataPack.getServiceId();
        double[] applicationId = dataPack.getApplicationId();

        // we assume the number of VM equals the size of containers.
//        int maxNumOfVm = containerSize;


        // Initialization
        InitPop initMethod = new FirstStepGAInitialization(
                permutationNum,
                applicationSize, microServiceSize,
                lbound, ubound,
                pmCpu, pmMem, k, pmEnergy, crushPro,
                vmCpuOverheadRate, vmMemOverhead, vmCpu, vmMem,
                serviceCpu, serviceMem, serviceId, applicationId
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
        NSGAIICollector collector = new NSGAIICollector();

        ProblemParameterSettings proSet = new FirstStepGAParameterSettings(
                evaluate, initMethod, mutation, crossover, crowding,
                vmTypes, microServiceSize, pmCpu, pmMem, pmEnergy,
                vmCpu, vmMem, serviceCpu, serviceMem
        );

        // Algorithm related parameters
        ParameterSettings pars = new ParameterSettings(mutationRate, crossoverRate, lbound, ubound,
                tournamentSize, eliteSize, optimization, popSize, maxGen, microServiceSize, seed);

        // factory
        GAFactory factory = new FirstStepGAFactory(collector, proSet, pars);

        nsgaii = new NSGAIIFirstStepGA(pars, proSet, factory);

        return collector;
    }


}
