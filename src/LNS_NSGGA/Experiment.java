package LNS_NSGGA;

import NSGAII_NSGGA.GASettings;
import NSGAII_NSGGA.NSGGAPostProcessingUnit;
import util.WriteFile;


public class Experiment {

    public static void main(String[] args){
        String algorithmName = "LNS_NSGGA";
        Configure configure = new Configure(args[0], algorithmName);
        int seed = Integer.parseInt(args[1]);
        experimentRunner(configure, seed);
    }

    public static void experimentRunner(Configure configure, int seed){


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


        DataPack dataPack = new DataPack(seed, configure);
        String replicaNumPath = configure.getReplicaNumPath();
        ReplicaNumWriteFile replicaNumWriteFile = new ReplicaNumWriteFile(replicaNumPath);
        LNS_NSGGAProcedure procedure = new LNS_NSGGAProcedure(
                replicaNumWriteFile,
                nsggaSettings,
                dataPack
        );

//        LNSPostProcessingUnit LNSPostProcessingUnit = new LNSPostProcessingUnit(LNSCollector.getResultData(), maxGen);
//        ReplicaAllocationDecisionPack replicaAllocationDecisionPack = LNSPostProcessingUnit.getDecisionPack();
//        try {
//            replicaNumWriteFile.writeReplicaNum(
//                    replicaAllocationDecisionPack.getContainerCpu(),
//                    replicaAllocationDecisionPack.getContainerMem(),
//                    replicaAllocationDecisionPack.getContainerAppId(),
//                    replicaAllocationDecisionPack.getContainerMicroServiceId(),
//                    replicaAllocationDecisionPack.getContainerReplicaId()
//            );
//        } catch (IOException e){
//            e.printStackTrace();
//        }
//        System.out.println(serviceCpu[i]);

//        FirstFit secondStep = new FirstFit(ffCollector, replicaAllocationDecisionPack, dataPack);
//        secondStep.run();

//        System.out.println("finished");
        NSGGAPostProcessingUnit postProcessingUnit = procedure.run();

//        // These are the addresses of result
        String paretoFrontPath = configure.getParetoFrontPath();
        String energyPath = configure.getEnergyPath();
        String wastedResourcePath = configure.getWastedResourcePath();
        String cpuMemUtilPath = configure.getCpuMemUtilPath();
        String numOfPmPath = configure.getNumOfPmPath();
        String numOfVmPath = configure.getNumOfVmPath();
        String convergenceCurvePath = configure.getConvergenceCurvePath();
        String timePath = configure.getTimePath();


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
