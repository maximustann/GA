package NSGAII_Spread;

import ProblemDefine.ParameterSettings;
import ProblemDefine.ProblemParameterSettings;
import algorithms.*;
import gaFactory.GAFactory;
import multi_objective_operators.CrowdingDistance;
import variableReplicasFirstStepGA.AvailabilityFitness;
import variableReplicasFirstStepGA.EnergyFitness;
import variableReplicasFirstStepGA.Evaluation;
import variableReplicasFirstStepGA.*;

import java.util.ArrayList;

public class NSGAIISpreadProcedure {

    private NSGAIIFirstStepGA nsgaii;
    private SpreadStrategy spreadStrategy;
    private GASettings nsgaiiSettings;
    private DataPack dataPack;
    private ReplicaNumWriteFile replicaNumWriteFile;

    public NSGAIISpreadProcedure(
            ReplicaNumWriteFile replicaNumWriteFile,
            GASettings nsgaiiSettings,
            DataPack dataPack){
        this.replicaNumWriteFile = replicaNumWriteFile;
        this.nsgaiiSettings = nsgaiiSettings;
        this.dataPack = dataPack;
    }

    // The main procedure,
    // 1.NSGA-II
    // write the result of NSGA-II
    // 2.Spread
    public SpreadPostProcessingUnit run(){
        ReplicaNumDecisionPack decisionPack = runFirstStepGA();
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

        return runSpread(decisionPack);
    }

    public NSGAIICollector configNSGAII(){

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

    public SpreadCollector configSpread(ReplicaNumDecisionPack decisionPack){

        // seed
        int seed = dataPack.getSeed();

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


        // Collector
        SpreadCollector spreadCollector = new SpreadCollector();

        spreadStrategy = new SpreadStrategy(
                spreadCollector,
                pmCpu, pmMem, k, pmEnergy, crushPro,
                vmCpuOverheadRate, vmMemOverhead,
                containerCpu, containerMem, containerAppId,
                containerMicroServiceId, containerReplicaId,
                vmCpu, vmMem);

        return spreadCollector;
    }



    private ReplicaNumDecisionPack runFirstStepGA(){
        // initiate NSGA-II
        NSGAIICollector collector = configNSGAII();

        int maxGen = nsgaiiSettings.getMaxGen();
        int microServiceNum = dataPack.getMicroServiceSize();
        int maximumMicroService = dataPack.getMaximumServiceNum();
        int[][] microService = dataPack.getMicroService();



        nsgaii.run(dataPack.getSeed());

        NSGAIIPostProcessingUnit postProcessingUnit = new NSGAIIPostProcessingUnit(
                collector.getResultData(),
                collector.getTime(),
                maxGen);

        return postProcessingUnit.getDecisionPack();

    }


    private SpreadPostProcessingUnit runSpread(ReplicaNumDecisionPack decisionPack){
        SpreadCollector collector = configSpread(decisionPack);
        int maxGen = nsgaiiSettings.getMaxGen();
        spreadStrategy.allocate();
        SpreadPostProcessingUnit postProcessingUnit = new SpreadPostProcessingUnit(collector.getResult(), decisionPack.getFirstStepTime(), collector.getTime());
        return postProcessingUnit;
    }

}
