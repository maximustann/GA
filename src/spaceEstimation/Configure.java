package spaceEstimation;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Configure {

    // general parameters
    private int run;
    private int seed;
    private int permutationNum;

    // algorithm parameters
    private double crossoverRate;
    private double mutationRate;
    private int optimization;
    private int tournamentSize;
    private int eliteSize;
    private int popSize;
    private int maxGen;
    private double ubound;
    private double lbound;

    // problem parameters
    private double crushPro;
    private double k;
    private double vmCpuOverheadRate;
    private double vmMemOverhead;
    private String pmSize;
    private String numOfVmTypesNames;
    private int numOfVmTypes;
    private int applicationSize;
    private int maximumServiceNum;
    private String numOfVmName;
    private String balancedOrNot;
    private int testCaseSize;
    private String testCaseName;
    private String testCaseSizeName;
    private String applicationName;

    // test case addresses
    // These are the addresses of test cases
    private String base;
    private String configPath;
    private String testCasePath;
    private String applicationPath;
    private String VMConfigPath;
    private String PMConfigPath;


    // Temp addresses
    private String configPathTemp;
    private String testCasePathTemp;

    // These are the addresses of result
    private String resultPath;


    // Constructor
    public Configure(String configFileName){
        read(configFileName);

        configPath = base + configPathTemp;
        testCasePath = base + testCasePathTemp;
        applicationPath = testCasePath + applicationName;
        VMConfigPath = configPath + "VMConfig/" + balancedOrNot + "/VMConfig_" + numOfVmTypesNames + ".csv";
        PMConfigPath = configPath + "PMConfig/" + pmSize;
    }

    private void read(String fileName){
        try {
            Properties pro = new Properties();
            FileInputStream in = new FileInputStream(fileName);
            pro.load(in);
            in.close();

            run = Integer.parseInt(pro.getProperty("run"));
            seed = Integer.parseInt(pro.getProperty("seed"));
            permutationNum = Integer.parseInt(pro.getProperty("permutationNum"));
            ubound = Double.parseDouble(pro.getProperty("ubound"));
            lbound = Double.parseDouble(pro.getProperty("lbound"));
            crossoverRate = Double.parseDouble(pro.getProperty("crossoverRate"));
            mutationRate = Double.parseDouble(pro.getProperty("mutationRate"));
            optimization = Integer.parseInt(pro.getProperty("optimization"));
            tournamentSize = Integer.parseInt(pro.getProperty("tournamentSize"));
            eliteSize = Integer.parseInt(pro.getProperty("eliteSize"));
            popSize = Integer.parseInt(pro.getProperty("popSize"));
            maxGen = Integer.parseInt(pro.getProperty("maxGen"));
            k = Double.parseDouble(pro.getProperty("k"));
            crushPro = Double.parseDouble(pro.getProperty("crushPro"));
            vmCpuOverheadRate = Double.parseDouble(pro.getProperty("vmCpuOverheadRate"));
            vmMemOverhead = Double.parseDouble(pro.getProperty("vmMemOverhead"));
            pmSize = pro.getProperty("pmSize");
            numOfVmTypes = Integer.parseInt(pro.getProperty("numOfVmTypes"));
            numOfVmTypesNames = pro.getProperty("numOfVmTypesNames");
            numOfVmName = pro.getProperty("numOfVmName");
            applicationSize = Integer.parseInt(pro.getProperty("applicationSize"));
            maximumServiceNum = Integer.parseInt(pro.getProperty("maximumServiceNum"));
            balancedOrNot = pro.getProperty("balancedOrNot");
            testCaseSize = Integer.parseInt(pro.getProperty("testCaseSize"));
            testCaseName = pro.getProperty("testCaseName");
            testCaseSizeName = pro.getProperty("testCaseSizeName");
            applicationName = pro.getProperty("applicationName");
            base = pro.getProperty("base");
            configPathTemp = pro.getProperty("configPath");
            testCasePathTemp = pro.getProperty("testCasePath");
            resultPath = pro.getProperty("resultPath");

        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public int getTestCaseSize() {
        return testCaseSize;
    }

    public int getRun() {
        return run;
    }

    public int getSeed() {
        return seed;
    }

    public int getPermutationNum() {
        return permutationNum;
    }

    public double getLbound() {
        return lbound;
    }

    public double getUbound() {
        return ubound;
    }

    public int getNumOfVmTypes() {
        return numOfVmTypes;
    }

    public double getCrossoverRate() {
        return crossoverRate;
    }

    public double getMutationRate() {
        return mutationRate;
    }

    public int getOptimization() {
        return optimization;
    }

    public int getTournamentSize() {
        return tournamentSize;
    }

    public int getEliteSize() {
        return eliteSize;
    }

    public int getPopSize() {
        return popSize;
    }

    public int getMaxGen() {
        return maxGen;
    }

    public double getK() {
        return k;
    }

    public double getCrushPro() {
        return crushPro;
    }

    public double getVmCpuOverheadRate() {
        return vmCpuOverheadRate;
    }

    public double getVmMemOverhead() {
        return vmMemOverhead;
    }

    public String getPmSize() {
        return pmSize;
    }

    public String getNumOfVmTypesNames() {
        return numOfVmTypesNames;
    }

    public String getNumOfVmName() {
        return numOfVmName;
    }

    public String getBalancedOrNot() {
        return balancedOrNot;
    }

    public String getTestCaseSizeName() {
        return testCaseSizeName;
    }

    public int getMaximumServiceNum() {
        return maximumServiceNum;
    }

    public int getApplicationSize() {
        return applicationSize;
    }


    public String getTestCaseName() {
        return testCaseName;
    }

    public String getBase() {
        return base;
    }

    public String getConfigPath() {
        return configPath;
    }

    public String getTestCasePath() {
        return testCasePath;
    }



    public String getVMConfigPath() {
        return VMConfigPath;
    }

    public String getPMConfigPath() {
        return PMConfigPath;
    }

    public String getApplicationPath() {
        return applicationPath;
    }

    public String getResultPath(){
        return resultPath;
    }

}
