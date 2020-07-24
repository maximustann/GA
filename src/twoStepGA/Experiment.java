package twoStepGA;

public class Experiment {

    public static void main(String[] args){
        String algorithmName = "twoStepGA";
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


        TwoStepGAProcedure twoStepGAProcedure = new TwoStepGAProcedure(
                            nsgaiiSettings,
                            nsggaSettings,
                            dataPack);



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


        NSGGAPostProcessingUnit postProcessingUnit = twoStepGAProcedure.run();

        WriteFile writeFile = new WriteFile
                .Builder(postProcessingUnit.allParetoFront(), paretoFrontPath)
                .setWastedResource(postProcessingUnit.waste(), wastedResourcePath)
                .setAveCpuMemUtil(postProcessingUnit.averageUtil(), postProcessingUnit.sdUtil(), aveCpuMemUtilPath)
                .setAveNumOfPm(postProcessingUnit.averageNoOfPm(), postProcessingUnit.sdNoOfPm(), numOfPmPath)
                .setAveNumOfVm(postProcessingUnit.averageNoOfVm(), postProcessingUnit.sdNoOfVm(), numOfVmPath)
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
