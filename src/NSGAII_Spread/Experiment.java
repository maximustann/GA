package NSGAII_Spread;
import util.WriteFile;

public class Experiment {

    public static void main(String[] args){
        String algorithmName = "NSGAII_Spread";
        Configure configure = new Configure(args[0], algorithmName);
        int seed = Integer.parseInt(args[1]);
        experimentRunner(configure, seed);
    }

    public static void experimentRunner(Configure configure, int seed){


        GASettings nsgaiiSettings = new GASettings(
                "nsgaii",
                configure.getUbound(),
                configure.getLbound(),
                configure.getCrossoverRate(),
                configure.getMutationRate(),
                configure.getOptimization(),
                configure.getTournamentSize(),
                configure.getEliteSize(),
                configure.getPopSize(),
                configure.getMaxGen(),
                configure.getPermutationNum()
        );


        DataPack dataPack = new DataPack(
                seed,
                configure
        );



        // the addresses of replica nums from the first step GA
        String replicaNumPath = configure.getReplicaNumPath();
        ReplicaNumWriteFile replicaNumWriteFile = new ReplicaNumWriteFile(replicaNumPath);

        NSGAIISpreadProcedure NSGAIISpreadProcedure = new NSGAIISpreadProcedure(
                            replicaNumWriteFile,
                            nsgaiiSettings,
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
        SpreadPostProcessingUnit postProcessingUnit = NSGAIISpreadProcedure.run();

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
                    .writeNumOfPm()
                    .writeNumOfVm()
                    .writeConvergenceCurve()
                    .writeTime();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
