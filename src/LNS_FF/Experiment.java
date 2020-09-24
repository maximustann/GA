package LNS_FF;


import java.io.IOException;
import util.WriteFile;
public class Experiment {

    public static void main(String[] args){
        String algorithmName = "LNS_FF";
        Configure configure = new Configure(args[0], algorithmName);
        int seed = Integer.parseInt(args[1]);
        experimentRunner(configure, seed);
    }

    public static void experimentRunner(Configure configure, int seed){


        int numOfService = configure.getMicroServiceSize();
        int maxGen = configure.getMaxGen();
        int tabuSize = configure.getTabuSize();
        int improvementThreshold = configure.getImprovementThreshold();
        int lbound = configure.getLbound();
        int ubound = configure.getUbound();
        double replicaChangePercentage = configure.getReplicaChangePercentage();
        double allocationChangePercentage = configure.getAllocationChangePercentage();

        DataPack dataPack = new DataPack(seed, configure);
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
        FFCollector ffCollector = new FFCollector();
        ReplicaNumWriteFile replicaNumWriteFile = new ReplicaNumWriteFile(configure.getReplicaNumPath());
        LNS firstStep = new LNS(settings, evaluation);
        firstStep.run();
        LNSPostProcessingUnit LNSPostProcessingUnit = new LNSPostProcessingUnit(LNSCollector.getResultData(), maxGen);
        ReplicaAllocationDecisionPack replicaAllocationDecisionPack = LNSPostProcessingUnit.getDecisionPack();
        try {
            replicaNumWriteFile.writeReplicaNum(
                    replicaAllocationDecisionPack.getContainerCpu(),
                    replicaAllocationDecisionPack.getContainerMem(),
                    replicaAllocationDecisionPack.getContainerAppId(),
                    replicaAllocationDecisionPack.getContainerMicroServiceId(),
                    replicaAllocationDecisionPack.getContainerReplicaId()
            );
        } catch (IOException e){
            e.printStackTrace();
        }
//        System.out.println(serviceCpu[i]);

        FirstFit secondStep = new FirstFit(ffCollector, replicaAllocationDecisionPack, dataPack);
        secondStep.run();

        System.out.println("finished");

        // These are the addresses of result
        String paretoFrontPath = configure.getParetoFrontPath();
        String energyPath = configure.getEnergyPath();
        String wastedResourcePath = configure.getWastedResourcePath();
        String cpuMemUtilPath = configure.getCpuMemUtilPath();
        String numOfPmPath = configure.getNumOfPmPath();
        String numOfVmPath = configure.getNumOfVmPath();
        String convergenceCurvePath = configure.getConvergenceCurvePath();
        String timePath = configure.getTimePath();


        FFPostProcessingUnit postProcessingUnit = new FFPostProcessingUnit(
                ffCollector.getResultData(),
                ffCollector.getTime(),
                LNSCollector.getTime());

        WriteFile writeFile = new WriteFile
                .Builder()
                .setParetoFront(postProcessingUnit.allParetoFront(), paretoFrontPath)
                .setWastedResource(postProcessingUnit.waste(), wastedResourcePath)
                .setCpuMemUtil(postProcessingUnit.averageUtil(), cpuMemUtilPath)
                .setNumOfPm(postProcessingUnit.noOfPm(), numOfPmPath)
                .setNumOfVm(postProcessingUnit.noOfVm(), numOfVmPath)
                .setConvergenceCurve(postProcessingUnit.convergenceCurve(), convergenceCurvePath)
                .setTime(postProcessingUnit.getAggregatedTime(), timePath)
                .build();
        try {
            writeFile.writeParetoFront()
                    .writeWastedResource()
                    .writeCpuMemUtil()
                    .writeNumOfPm()
                    .writeNumOfVm()
                    .writeConvergenceCurve()
                    .writeTime();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
