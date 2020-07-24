package spaceEstimation;

import java.io.IOException;
import java.util.ArrayList;

public class Experiment {

    public static void main(String[] args){
        Configure configure = new Configure(args[0]);
        experimentRunner(configure);
    }

    public static void experimentRunner(Configure configure){
        // We need to do a number of runs.
        int run = configure.getRun();

        // Get the starting seed number
        int seed = configure.getSeed();

        // Get the number of permutations
        int permutationNum = configure.getPermutationNum();

        // fitness function list
//        ArrayList<FitnessFunc> funcList = new ArrayList<>();

        // dummy variables
        double lbound = configure.getLbound();
        double ubound = configure.getUbound();
        // dummy variables


        // experiment related parameters
        double vmCpuOverheadRate = configure.getVmCpuOverheadRate();
        double vmMemOverhead = configure.getVmMemOverhead();
        double k = configure.getK();
        double crushPro = configure.getCrushPro();
        int vmTypes = configure.getNumOfVmTypes();
        String vmTypesName = configure.getNumOfVmTypesNames();
        int containerSize = configure.getTestCaseSize();
        int applicationSize = configure.getApplicationSize();
        int maximumServiceNum = configure.getMaximumServiceNum();

        // These are the addresses of test cases
        String testCasePath = configure.getTestCasePath();
        String testCaseName = configure.getTestCaseName();
//        String testCaseSizeName = configure.getTestCaseSizeName();
        String VMConfigPath = configure.getVMConfigPath();
        String PMConfigPath = configure.getPMConfigPath();
        String applicationPath = configure.getApplicationPath();

        // These are the addresses of result
        String resultPath = configure.getResultPath();


        ArrayList<double[]> result = new ArrayList<>();
        for(int i = 1; i <= 100; ++i) {
            result.add(
                    singleRun(
                    testCasePath,
                    testCaseName,
                    PMConfigPath,
                    VMConfigPath,
                    applicationPath,
                    vmTypesName,
                    containerSize,
                    i,
                    permutationNum,
                    vmTypes,
                    applicationSize,
                    maximumServiceNum,
                    k,
                    crushPro,
                    vmCpuOverheadRate,
                    vmMemOverhead));
        }
        WriteFile writeFile = new WriteFile();
        try{
            writeFile.write(resultPath, result);
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public static double[] singleRun(
            String testCasePathTemp,
            String testCaseName,
            String PMConfigPath,
            String VMConfigPath,
            String applicationPath,
            String vmTypesName,
            int testCaseSize,
            int seed,
            int permutationNum,
            int vmTypes,
            int applicationSize,
            int maximumServiceNum,
            double k,
            double crushPro,
            double vmCpuOverheadRate,
            double vmMemOverhead
                                ){
        String testCasePath = testCasePathTemp + testCaseName + "_" + vmTypesName + "_" + seed + ".csv";
//        String testCaseSizePath = testCasePathTemp + testCaseSizeName + "_" + vmTypesName + "_" + seed + ".csv";
        applicationPath = applicationPath + "_" + seed + ".csv";
        ReadFile readFile = new ReadFile(vmTypes, testCaseSize, testCasePath, PMConfigPath, VMConfigPath);

//        ReadMicroServices readMicroServices = new ReadMicroServices(
//                applicationSize,
//                maximumServiceNum,
//                applicationPath);

        double pmCpu = readFile.getPMCpu();
        double pmMem = readFile.getPMMem();
        double pmEnergy = readFile.getPMEnergy();
        double[] vmMem = readFile.getVMMem();
        double[] vmCpu = readFile.getVMCpu();

        double[] containerCpu = readFile.getTaskCpu();
        double[] containerMem = readFile.getTaskMem();
        double[] containerAppId = readFile.getTaskAppId();
        double[] containerMicroServiceId = readFile.getTaskMicroServiceId();
        double[] containerReplicaId = readFile.getTaskReplicaId();
        int containerSize = readFile.getTestCaseSize();

        // we assume the number of VM equals the size of containers.
        int maxNumOfVm = containerSize;

        FitnessEstimation fitnessEstimate = new FitnessEstimation(pmCpu,
                pmMem,
                k,
                pmEnergy,
                crushPro,
                vmCpuOverheadRate,
                vmMemOverhead,
                containerCpu,
                containerMem,
                containerAppId,
                containerMicroServiceId,
                containerReplicaId,
                vmCpu,
                vmMem);


        double fitness[] = fitnessEstimate.estimate(permutationNum);

        return fitness;
    }
}
