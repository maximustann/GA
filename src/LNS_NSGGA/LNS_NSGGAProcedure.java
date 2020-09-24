package LNS_NSGGA;

import LNS_FF.LNSCollector;
import NSGAII_NSGGA.*;
import ProblemDefine.ParameterSettings;
import ProblemDefine.ProblemParameterSettings;
import algorithms.*;
import gaFactory.GAFactory;
import multi_objective_operators.CrowdingDistance;
import multiobjectiveMicroService.*;
import variableReplicasFirstStepGA.*;

import java.util.ArrayList;

public class LNS_NSGGAProcedure {

    private LNS lns;
    private NSGAIIGroup nsgga;
    private GASettings nsggaSettings;
    private DataPack dataPack;
    private ReplicaNumWriteFile replicaNumWriteFile;

    public LNS_NSGGAProcedure(
            ReplicaNumWriteFile replicaNumWriteFile,
            GASettings nsggaSettings,
            DataPack dataPack){
        this.replicaNumWriteFile = replicaNumWriteFile;
        this.nsggaSettings = nsggaSettings;
        this.dataPack = dataPack;
    }

    // The main procedure,
    // 1.LNS
    // write the result of LNS
    // 2.NSGGA
    public NSGGAPostProcessingUnit run(){
        ReplicaNumDecisionPack decisionPack = runLNS();
        try {
            replicaNumWriteFile.writeReplicaNum(
                    decisionPack.getContainerCpu(),
                    decisionPack.getContainerMem(),
                    decisionPack.getContainerAppId(),
                    decisionPack.getContainerMicroServiceId(),
                    decisionPack.getContainerReplicaId()
            );
        } catch (Exception e){
            e.printStackTrace();
        }

//        decisionPack.print();
        return runSecondGA(decisionPack);
    }

    public LNSCollector configLNS(){

        int seed = dataPack.getSeed();

        // Algorithm parameters
        int numOfService = dataPack.getMicroServiceSize();
        int maxGen = dataPack.getLns_maxGen();
        int tabuSize = dataPack.getTabuSize();
        int improvementThreshold = dataPack.getImprovementThreshold();
        int lbound = dataPack.getLns_lbound();
        int ubound = dataPack.getLns_ubound();
        double replicaChangePercentage = dataPack.getReplicaChangePercentage();
        double allocationChangePercentage = dataPack.getAllocationChangePercentage();

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

        Initialization initialization = new Initialization(numOfService, lbound, ubound, dataPack);
        ReplicaPatternDestroy replicaPatternDestroy = new ReplicaPatternDestroy(replicaChangePercentage, lbound, ubound, dataPack);
        AllocationDestroy allocationDestroy = new AllocationDestroy(
                allocationChangePercentage,
                dataPack);
        LNSCollector LNSCollector = new LNSCollector();
        LNSSettings settings = new LNSSettings(seed, maxGen, numOfService, tabuSize, improvementThreshold,
                initialization, replicaPatternDestroy, allocationDestroy,
                LNSCollector);
        LNSEvaluation evaluation = new LNSEvaluation(dataPack);
        ReplicaNumWriteFile replicaNumWriteFile = new ReplicaNumWriteFile(dataPack.getReplicaNumPath());
        lns = new LNS(settings, evaluation);

       return LNSCollector;
    }

    public NSGGACollector configNSGGA(ReplicaNumDecisionPack decisionPack){

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



    private ReplicaNumDecisionPack runLNS(){
        // initiate LNS
        LNSCollector collector = configLNS();

        lns.run();

        LNSPostProcessingUnit postProcessingUnit = new LNSPostProcessingUnit(
                collector.getResultData(),
                collector.getTime(),
                dataPack.getLns_maxGen());

        return postProcessingUnit.getDecisionPack();
    }


    private NSGGAPostProcessingUnit runSecondGA(ReplicaNumDecisionPack decisionPack){
        // initiate NS-GGA with decision of variables of replicas
        NSGGACollector collector = configNSGGA(decisionPack);
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
