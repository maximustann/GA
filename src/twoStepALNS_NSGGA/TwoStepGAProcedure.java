//package twoStepALNS_NSGGA;
//
//import ProblemDefine.ParameterSettings;
//import ProblemDefine.ProblemParameterSettings;
//import algorithms.*;
//import gaFactory.GAFactory;
//import multi_objective_operators.CrowdingDistance;
//import multiobjectiveMicroService.*;
//import twoStepGA.*;
//import variableReplicasFirstStepGA.AvailabilityFitness;
//import variableReplicasFirstStepGA.EnergyFitness;
//import variableReplicasFirstStepGA.Evaluation;
//import variableReplicasFirstStepGA.*;
//
//import java.util.ArrayList;
//
//public class TwoStepGAProcedure {
//
//    private NSGAIIFirstStepGA nsgaii;
//    private NSGAIIGroup nsgga;
//    private GASettings nsgaiiSettings;
//    private GASettings nsggaSettings;
//    private DataPack dataPack;
//
//    public TwoStepGAProcedure(
//            GASettings nsgaiiSettings,
//            GASettings nsggaSettings,
//            DataPack dataPack){
//        this.nsgaiiSettings = nsgaiiSettings;
//        this.nsggaSettings = nsggaSettings;
//        this.dataPack = dataPack;
//    }
//
//    public NSGGAPostProcessingUnit run(){
//        ReplicaNumDecisionPack decisionPack = runFirstStepGA();
////        decisionPack.print();
//        return runSecondGA(decisionPack);
//    }
//
//    public NSGAIICollector configNSGAII(){
//
//        int seed = dataPack.getSeed();
//        // Permutation number decides the number of permutation of a solution we generate during
//        // evaluation of energy consumption
//        int permutationNum = nsgaiiSettings.getPermutationNum();
//
//        // fitness function list
//        ArrayList<FitnessFunc> funcList = new ArrayList<>();
//
//        // dummy variables
//        double lbound = nsgaiiSettings.getLbound();
//        double ubound = nsgaiiSettings.getUbound();
//        // dummy variables
//
//
//        // Algorithm parameters
//        double crossoverRate = nsgaiiSettings.getCrossoverRate();
//        double mutationRate = nsgaiiSettings.getMutationRate();
//        int optimization = nsgaiiSettings.getOptimization(); // minimize
//        int tournamentSize = nsgaiiSettings.getTournamentSize();
//        int eliteSize = nsgaiiSettings.getEliteSize();
//        int popSize = nsgaiiSettings.getPopSize();
//        int maxGen = nsgaiiSettings.getMaxGen();
//
//        // experiment related parameters
//        double vmCpuOverheadRate = dataPack.getVmCpuOverheadRate();
//        double vmMemOverhead = dataPack.getVmMemOverhead();
//        double k = dataPack.getK();
//        double crushPro = dataPack.getCrushPro();
//        int vmTypes = dataPack.getVmTypes();
//        int applicationSize = dataPack.getApplicationSize();
//        int microServiceSize = dataPack.getMicroServiceSize();
//        int maximumServiceNum = dataPack.getMaximumServiceNum();
//
//
//        double pmCpu = dataPack.getPmCpu();
//        double pmMem = dataPack.getPmMem();
//        double pmEnergy = dataPack.getPmEnergy();
//        double[] vmMem = dataPack.getVmMem();
//        double[] vmCpu = dataPack.getVmCpu();
//
//        double[] serviceCpu = dataPack.getServiceCpu();
//        double[] serviceMem = dataPack.getServiceMem();
//        double[] serviceId = dataPack.getServiceId();
//        double[] applicationId = dataPack.getApplicationId();
//
//        // we assume the number of VM equals the size of containers.
////        int maxNumOfVm = containerSize;
//
//
//        // Initialization
//        InitPop initMethod = new FirstStepGAInitialization(
//                permutationNum,
//                applicationSize, microServiceSize,
//                lbound, ubound,
//                pmCpu, pmMem, k, pmEnergy, crushPro,
//                vmCpuOverheadRate, vmMemOverhead, vmCpu, vmMem,
//                serviceCpu, serviceMem, serviceId, applicationId
//        );
//
//
//        // Mutation operator
//        Mutation mutation = new FirstStepGAMutation((int)lbound, (int)ubound);
//
//        // Crossover operator
//        Crossover crossover = new FirstStepGACrossover();
//
//        // fitness functions
//        UnNormalizedFit energyFitness = new EnergyFitness();
//        UnNormalizedFit availabilityFitness = new AvailabilityFitness();
////
////        // the framework wrapper
//        FitnessFunc energyFit = new FitnessFunc(energyFitness.getClass());
//        FitnessFunc availabilityFit = new FitnessFunc(availabilityFitness.getClass());
//
//        // add to the fitness function list
//        funcList.add(energyFit);
//        funcList.add(availabilityFit);
//
//        // Evaluation method
//        Evaluate evaluate = new Evaluation(funcList);
//
//        //Distance measure
//        Distance crowding = new CrowdingDistance(optimization);
//
//        // Collector
//        NSGAIICollector collector = new NSGAIICollector();
//
//        ProblemParameterSettings proSet = new FirstStepGAParameterSettings(
//                evaluate, initMethod, mutation, crossover, crowding,
//                vmTypes, microServiceSize, pmCpu, pmMem, pmEnergy,
//                vmCpu, vmMem, serviceCpu, serviceMem
//        );
//
//        // Algorithm related parameters
//        ParameterSettings pars = new ParameterSettings(mutationRate, crossoverRate, lbound, ubound,
//                tournamentSize, eliteSize, optimization, popSize, maxGen, microServiceSize, seed);
//
//        // factory
//        GAFactory factory = new FirstStepGAFactory(collector, proSet, pars);
//
//       nsgaii = new NSGAIIFirstStepGA(pars, proSet, factory);
//
//       return collector;
//    }
//
//    public NSGGACollector configNSGGA(ReplicaNumDecisionPack decisionPack){
//
//        // seed
//        int seed = dataPack.getSeed();
//
//        // fitness function list
//        ArrayList<FitnessFunc> funcList = new ArrayList<>();
//
//        // dummy variables
//        double lbound = nsggaSettings.getLbound();
//        double ubound = nsggaSettings.getUbound();
//        // dummy variables
//
//
//        // Algorithm parameters
//        double crossoverRate = nsggaSettings.getCrossoverRate();
//        double mutationRate = nsggaSettings.getMutationRate();
//        int optimization = nsggaSettings.getOptimization(); // minimize
//        int tournamentSize = nsggaSettings.getTournamentSize();
//        int eliteSize = nsggaSettings.getEliteSize();
//        int popSize = nsggaSettings.getPopSize();
//        int maxGen = nsggaSettings.getMaxGen();
//
//        // experiment related parameters
//        double vmCpuOverheadRate = dataPack.getVmCpuOverheadRate();
//        double vmMemOverhead = dataPack.getVmMemOverhead();
//        double k = dataPack.getK();
//        double crushPro = dataPack.getCrushPro();
//        int vmTypes = dataPack.getVmTypes();
//        int applicationSize = dataPack.getApplicationSize();
//        int microServiceSize = dataPack.getMicroServiceSize();
//        int maximumServiceNum = dataPack.getMaximumServiceNum();
//
//        double pmCpu = dataPack.getPmCpu();
//        double pmMem = dataPack.getPmMem();
//        double pmEnergy = dataPack.getPmEnergy();
//        double[] vmMem = dataPack.getVmMem();
//        double[] vmCpu = dataPack.getVmCpu();
//
//        double[] containerCpu = decisionPack.getContainerCpu();
//        double[] containerMem = decisionPack.getContainerMem();
//        double[] containerAppId = decisionPack.getContainerAppId();
//        double[] containerMicroServiceId = decisionPack.getContainerMicroServiceId();
//        double[] containerReplicaId = decisionPack.getContainerReplicaId();
//
//        int containerSize = decisionPack.getContainerSize();
//
//        // we assume the number of VM equals the size of containers.
//        int maxNumOfVm = containerSize;
//
//        // Initialization
//        InitPop initMethod = new MultiGroupGAInitialization(containerSize, pmCpu, pmMem, k, pmEnergy, crushPro,
//                vmCpu, vmMem, containerCpu, containerMem,
//                containerAppId, containerMicroServiceId, containerReplicaId,
//                vmCpuOverheadRate, vmMemOverhead);
//
//
//        // Rearrangement operator
//        MultiGroupGARearrangement rearrangement = new MultiGroupGARearrangement(
//                containerCpu, containerMem,
//                containerAppId, containerMicroServiceId, containerReplicaId,
//                vmCpu, vmMem, vmCpuOverheadRate, vmMemOverhead,
//                pmCpu, pmMem, k, pmEnergy, crushPro);
//        // Mutation operator
//        Mutation mutation = new MultiGroupGAMutation(rearrangement,
//                containerCpu, containerMem,
//                containerAppId, containerMicroServiceId, containerReplicaId,
//                vmCpu, vmMem,
//                pmCpu, pmMem,
//                vmCpuOverheadRate,
//                vmMemOverhead);
//
//        // Crossover operator
//        Crossover crossover = new MultiGroupGACrossover(rearrangement, (MultiGroupGAInitialization) initMethod);
////        Crossover crossover = new GroupGACrossoverDiversity(rearrangement, (GroupGAInitialization) initMethod);
////
//        // fitness functions
//        UnNormalizedFit energyFitness = new multiobjectiveMicroService.EnergyFitness();
//        UnNormalizedFit availabilityFitness = new multiobjectiveMicroService.AvailabilityFitness();
////
////        // the framework wrapper
//        FitnessFunc energyFit = new FitnessFunc(energyFitness.getClass());
//        FitnessFunc availabilityFit = new FitnessFunc(availabilityFitness.getClass());
//
//        // add to the fitness function list
//        funcList.add(energyFit);
//        funcList.add(availabilityFit);
//
//        // Evaluation method
//        Evaluate evaluate = new multiobjectiveMicroService.Evaluation(funcList);
//
//        //Distance measure
//        Distance crowding = new CrowdingDistance(optimization);
//
//        // Collector
//        NSGGACollector collector = new NSGGACollector();
//
//        ProblemParameterSettings proSet = new MultiGroupGAParameterSettings(
//                evaluate, initMethod, mutation, crossover, crowding,
//                vmTypes, containerSize, maxNumOfVm, pmCpu, pmMem, pmEnergy,
//                vmCpu, vmMem, containerCpu, containerMem
//        );
//
//        // Algorithm related parameters
//        ParameterSettings pars = new ParameterSettings(mutationRate, crossoverRate, lbound, ubound,
//                tournamentSize, eliteSize, optimization, popSize, maxGen, containerSize, seed);
//
//        // factory
//        GAFactory factory = new MultiGroupFactory(collector, proSet, pars);
//
//        nsgga = new NSGAIIGroup(pars, proSet, factory);
//        return collector;
//    }
//
//
//
//    private ReplicaNumDecisionPack runFirstStepGA(){
//        // initiate NSGA-II
//        NSGAIICollector collector = configNSGAII();
//
//        int maxGen = nsgaiiSettings.getMaxGen();
//        int microServiceNum = dataPack.getMicroServiceSize();
//        int maximumMicroService = dataPack.getMaximumServiceNum();
//        int[][] microService = dataPack.getMicroService();
//
//
//
//        nsgaii.run(dataPack.getSeed());
//
//        NSGAIIPostProcessingUnit postProcessingUnit = new NSGAIIPostProcessingUnit(
//                collector.getResultData(),
//                collector.getGenTime(),
//                microService, maximumMicroService,
//                maxGen);
//
//        return postProcessingUnit.getDecisionPack();
//
////        System.out.println("Average Energy = " + postProcessing.energy());
////        System.out.println("The upper bound of availability = " + postProcessing.availability());
//    }
//
//
//    private NSGGAPostProcessingUnit runSecondGA(ReplicaNumDecisionPack decisionPack){
//        // initiate NS-GGA with decision of variables of replicas
//        NSGGACollector collector = configNSGGA(decisionPack);
//        int maxGen = nsggaSettings.getMaxGen();
//        nsgga.run(dataPack.getSeed());
//        NSGGAPostProcessingUnit postProcessingUnit = new NSGGAPostProcessingUnit(
//                collector.getResult(),
//                collector.getGenTime(),
//                maxGen,
//                collector.meanTime(),
//                collector.sdTime());
//        return postProcessingUnit;
//    }
//
//}
