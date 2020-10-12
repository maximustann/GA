package NSGAII_NSGGA_Manual;

import ProblemDefine.ParameterSettings;
import ProblemDefine.ProblemParameterSettings;
import algorithms.*;
import gaFactory.GAFactory;
import multi_objective_operators.CrowdingDistance;
import multiobjectiveMicroService.*;
import util.WriteFile;

import java.io.IOException;
import java.util.ArrayList;

public class Exp_NSGGA {

    private static NSGAIIGroup nsgga;
    public static void main(String[] args) throws IOException {
        String algorithmName = "NSGAII_NSGGA_Manual";
        Configure configure = new Configure(args[0], algorithmName);
        int seed = Integer.parseInt(args[1]);
        experimentRunner(configure, seed);
    }

    public static void experimentRunner(Configure configure, int seed) throws IOException {


        GASettings nsggaSettings  = new GASettings(
                "nsgga",
                configure.getNsgga_ubound(),
                configure.getNsgga_lbound(),
                configure.getNsgga_crossoverRate(),
                configure.getNsgga_mutationRate(),
                configure.getNsgga_optimization(),
                configure.getNsgga_tournamentSize(),
                configure.getNsgga_eliteSize(),
                configure.getNsgga_popSize(),
                configure.getNsgga_maxGen(),
                0
        );


        int[] selectedReplicaIndex = new int[]{1, 12, 0};
        int[] replicaSize = new int[]{1214, 1202, 1303};
        DataPack dataPack = new DataPack(
                seed,
                configure
        );



        // the addresses of replica nums from the first step GA
//        String replicaNumPath = configure.getReplicaNumPath();
//        ReplicaNumWriteFile replicaNumWriteFile = new ReplicaNumWriteFile(replicaNumPath);
//
//        TwoStepGAProcedure twoStepGAProcedure = new TwoStepGAProcedure(
//                            replicaNumWriteFile,
//                            nsgaiiSettings,
//                            nsggaSettings,
//                            dataPack);






        // Start to run the algorithm here.
//        NSGGAPostProcessingUnit postProcessingUnit = twoStepGAProcedure.run();
        for(int i = 0; i < selectedReplicaIndex.length; i++) {
            // These are the addresses of results
//            String paretoFrontPath = configure.getParetoFrontPath();
//            String energyPath = configure.getEnergyPath();
//            String wastedResourcePath = configure.getWastedResourcePath();
//            String cpuMemUtilPath = configure.getCpuMemUtilPath();
//            String numOfPmPath = configure.getNumOfPmPath();
//            String numOfVmPath = configure.getNumOfVmPath();
//            String convergenceCurvePath = configure.getConvergenceCurvePath();
//            String timePath = configure.getTimePath();

            String base = "/home/tanboxi/workspace/GA/replicaNum" + selectedReplicaIndex[i] + "/";
            String paretoFrontPath = base + "paretoFront.csv";
            String energyPath = base + "energy.csv";
            String wastedResourcePath = base + "wasted.csv";
            String cpuMemUtilPath = base + "util.csv";
            String numOfPmPath = base + "pmNum.csv";
            String numOfVmPath = base + "vmNum.csv";
            String convergenceCurvePath = base + "convergence.csv";
            String timePath = base + "time.csv";
            String replicaNum = "/home/tanboxi/workspace/GA/replicaNum";

            ReplicaNumDecisionPack decisionPack = readDecisionPack(replicaNum,
                                                                selectedReplicaIndex[i],
                                                                replicaSize[i]);

            NSGGAPostProcessingUnit postProcessingUnit = runSecondGA(dataPack, decisionPack, nsggaSettings);

            WriteFile writeFile = new WriteFile
                    .Builder()
                    .setParetoFront(postProcessingUnit.allParetoFront(), paretoFrontPath)
                    .setWastedResource(postProcessingUnit.waste(), wastedResourcePath)
                    .setCpuMemUtil(postProcessingUnit.averageUtil(), cpuMemUtilPath)
                    .setNumOfPm(postProcessingUnit.noOfPm(), numOfPmPath)
                    .setNumOfVm(postProcessingUnit.noOfVm(), numOfVmPath)
                    .setConvergenceCurve(postProcessingUnit.convergenceCurve(), convergenceCurvePath)
//                    .setTime(postProcessingUnit.getAggregatedTime(), timePath)
                    .build();

            try {
                writeFile.writeParetoFront()
                        .writeWastedResource()
                        .writeCpuMemUtil()
                        .writeNumOfPm()
                        .writeNumOfVm()
                        .writeConvergenceCurve();
//                        .writeTime();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static ReplicaNumDecisionPack readDecisionPack(String address, int selectReplicaIndex, int replicaSize){
        String path = address + selectReplicaIndex + ".csv";
        ReadDecisionFile readDecisionFile = new ReadDecisionFile(replicaSize, path);
        double[] containerCpu = readDecisionFile.getServiceCpu();
        double[] containerMem = readDecisionFile.getServiceMem();
        double[] containerAppId = readDecisionFile.getApplicationId();
        double[] containerMicroServiceId = readDecisionFile.getServiceId();
        double[] containerReplicaId = readDecisionFile.getReplicaId();
        ReplicaNumDecisionPack replicaNumDecisionPack = new ReplicaNumDecisionPack(
                containerCpu,
                containerMem,
                containerAppId,
                containerMicroServiceId,
                containerReplicaId
        );
        return replicaNumDecisionPack;
    }


    public static NSGGACollector configNSGGA(DataPack dataPack, ReplicaNumDecisionPack decisionPack, GASettings nsggaSettings){

        // seed
        int seed = dataPack.getSeed();

        // fitness function list
        ArrayList<FitnessFunc> funcList = new ArrayList<>();

        // dummy variables
        double lbound = nsggaSettings.getLbound();
        double ubound = nsggaSettings.getUbound();
        // dummy variables


        // Algorithm parameters
        double crossoverRate = nsggaSettings.getCrossoverRate();
        double mutationRate = nsggaSettings.getMutationRate();
        int optimization = nsggaSettings.getOptimization(); // minimize
        int tournamentSize = nsggaSettings.getTournamentSize();
        int eliteSize = nsggaSettings.getEliteSize();
        int popSize = nsggaSettings.getPopSize();
        int maxGen = nsggaSettings.getMaxGen();

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

        double[] containerCpu = decisionPack.getContainerCpu();
        double[] containerMem = decisionPack.getContainerMem();
        double[] containerAppId = decisionPack.getContainerAppId();
        double[] containerMicroServiceId = decisionPack.getContainerMicroServiceId();
        double[] containerReplicaId = decisionPack.getContainerReplicaId();

        int containerSize = decisionPack.getContainerSize();

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
        UnNormalizedFit energyFitness = new multiobjectiveMicroService.EnergyFitness();
        UnNormalizedFit availabilityFitness = new multiobjectiveMicroService.AvailabilityFitness();
//
//        // the framework wrapper
        FitnessFunc energyFit = new FitnessFunc(energyFitness.getClass());
        FitnessFunc availabilityFit = new FitnessFunc(availabilityFitness.getClass());

        // add to the fitness function list
        funcList.add(energyFit);
        funcList.add(availabilityFit);

        // Evaluation method
        Evaluate evaluate = new multiobjectiveMicroService.Evaluation(funcList);

        //Distance measure
        Distance crowding = new CrowdingDistance(optimization);

        // Collector
        NSGGACollector collector = new NSGGACollector();

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

        nsgga = new NSGAIIGroup(pars, proSet, factory);
        return collector;
    }

    private static NSGGAPostProcessingUnit runSecondGA(DataPack dataPack, ReplicaNumDecisionPack decisionPack, GASettings nsggaSettings){
        // initiate NS-GGA with decision of variables of replicas
        NSGGACollector collector = configNSGGA(dataPack, decisionPack, nsggaSettings);
        int maxGen = nsggaSettings.getMaxGen();
        nsgga.run(dataPack.getSeed());
        NSGGAPostProcessingUnit postProcessingUnit = new NSGGAPostProcessingUnit(
                collector.getResult(),
                collector.getGenTime(),
                decisionPack.getFirstStepTime(),
                collector.getTime(),
                maxGen);
        return postProcessingUnit;
    }
}
