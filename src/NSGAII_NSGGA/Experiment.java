package NSGAII_NSGGA;

import util.WriteFile;
public class Experiment {

    public static void main(String[] args){
        String algorithmName = "NSGAII_NSGGA";
        Configure configure = new Configure(args[0], algorithmName);
        int seed = Integer.parseInt(args[1]);
        experimentRunner(configure, seed);
    }

    public static void experimentRunner(Configure configure, int seed){


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

        DataPack dataPack = new DataPack(
                seed,
                configure
        );

        // the addresses of replica nums from the first step GA
        String replicaNumPath = configure.getReplicaNumPath();
        ReplicaNumWriteFile replicaNumWriteFile = new ReplicaNumWriteFile(replicaNumPath);

        TwoStepGAProcedure twoStepGAProcedure = new TwoStepGAProcedure(
                            replicaNumWriteFile,
                            nsgaiiSettings,
                            nsggaSettings,
                            dataPack);



        // These are the addresses of result
        String paretoFrontPath = configure.getParetoFrontPath();
        String energyPath = configure.getEnergyPath();
        String wastedResourcePath = configure.getWastedResourcePath();
        String cpuMemUtilPath = configure.getCpuMemUtilPath();
        String numOfPmPath = configure.getNumOfPmPath();
        String numOfVmPath = configure.getNumOfVmPath();
        String convergenceCurvePath = configure.getConvergenceCurvePath();
        String timePath = configure.getTimePath();


        // Start to run the algorithm here.
        NSGGAPostProcessingUnit postProcessingUnit = twoStepGAProcedure.run();

        WriteFile writeFile = new WriteFile
                .Builder()
                .setParetoFront(postProcessingUnit.allParetoFront(), paretoFrontPath)
                .setWastedResource(postProcessingUnit.waste(), wastedResourcePath)
                .setCpuMemUtil(postProcessingUnit.averageUtil(), cpuMemUtilPath)
                .setNumOfPm(postProcessingUnit.noOfPm(), numOfPmPath)
                .setNumOfVm(postProcessingUnit.noOfVm(),numOfVmPath)
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
