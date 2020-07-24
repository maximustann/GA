package alnsFF;


public class Experiment {

    public static void main(String[] args){
        String algorithmName = "alnsFF";
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
        Initialization intialization = new Initialization(numOfService, lbound, ubound, dataPack);
        ReplicaPatternDestroy replicaPatternDestroy = new ReplicaPatternDestroy(replicaChangePercentage, lbound, ubound);
        AllocationDestroy allocationDestroy = new AllocationDestroy(
                allocationChangePercentage,
                dataPack);
        ALNSCollector alnsCollector = new ALNSCollector();
        ALNSSettings settings = new ALNSSettings(seed, maxGen, numOfService, tabuSize, improvementThreshold,
                                                intialization, replicaPatternDestroy, allocationDestroy,
                                                alnsCollector);
        ALNSEvaluation evaluation = new ALNSEvaluation();
        FFCollector ffCollector = new FFCollector();
        ALNS firstStep = new ALNS(settings, evaluation);
        firstStep.run();
        ALNSPostProcessingUnit alnsPostProcessingUnit = new ALNSPostProcessingUnit(alnsCollector.getResultData(), maxGen);
        ReplicaAllocationDecisionPack replicaAllocationDecisionPack = alnsPostProcessingUnit.getDecisionPack();
//        System.out.println(serviceCpu[i]);

        FirstFit secondStep = new FirstFit(ffCollector, replicaAllocationDecisionPack, dataPack);
        secondStep.run();

        System.out.println("finished");

        // These are the addresses of result
        String paretoFrontPath = configure.getParetoFrontPath();
        String energyPath = configure.getEnergyPath();
        String aveEnergyPath = configure.getAveEnergyPath();
        String wastedResourcePath = configure.getWastedResourcePath();
        String aveCpuMemUtilPath = configure.getAveCpuMemUtilPath();
        String numOfPmPath = configure.getNumOfPmPath();
        String numOfVmPath = configure.getNumOfVmPath();
        String convergenceCurvePath = configure.getConvergenceCurvePath();
        String aveTimePath = configure.getAveTimePath();


        FFPostProcessingUnit postProcessingUnit = new FFPostProcessingUnit(ffCollector.getResultData());

        WriteFile writeFile = new WriteFile
                .Builder(postProcessingUnit.allParetoFront(), paretoFrontPath)
                .setWastedResource(postProcessingUnit.waste(), wastedResourcePath)
                .setAveCpuMemUtil(postProcessingUnit.averageUtil(), postProcessingUnit.sdUtil(), aveCpuMemUtilPath)
                .setAveNumOfPm(postProcessingUnit.noOfPm(), postProcessingUnit.sdNoOfPm(), numOfPmPath)
                .setAveNumOfVm(postProcessingUnit.totalNoOfVm(), postProcessingUnit.sdNoOfVm(), numOfVmPath)
                .setConvergenceCurve(postProcessingUnit.convergenceCurve(), convergenceCurvePath)
                .setAveTime(postProcessingUnit.getMeanTime(), postProcessingUnit.getSdTime(),aveTimePath)
                .build();

        try {
            writeFile.writeParetoFront()
                    .writeWastedResource()
                    .writeAveSdCpuMemUtil()
                    .writeAveSdNumOfPm()
                    .writeAveSdNumOfVm()
                    .writeConvergenceCurve();
//                    .writeAveSdTime();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
