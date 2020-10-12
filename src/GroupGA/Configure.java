package GroupGA;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Configure {

    // general parameters
    private int run;
    private int seed;

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
    private double k;
    private double vmCpuOverheadRate;
    private double vmMemOverhead;
    private String pmSize;
    private String numOfVmTypesNames;
    private int numOfVmTypes;
    private String numOfVmName;
    private String balancedOrNot;
    private int testCaseSize;
    private String testCaseName;
    private String experimentClass;

    // test case addresses
    // These are the addresses of test cases
    private String base;
    private String configPath;
    private String testCasePath;
    private String VMConfigPath;
    private String PMConfigPath;

    // These are the addresses of result
    private String resultBase;
    private String algorithmBase;
    private String testCaseResultPath;
    private String energyFile;
    private String energyPath;
    private String wastedResourceFile;
    private String wastedResourcePath;
    private String cpuMemUtilFile;
    private String cpuMemUtilPath;
    private String numOfPmFile;
    private String numOfPmPath;
    private String numOfVmFile;
    private String numOfVmPath;
    private String convergenceCurveFile;
    private String convergenceCurvePath;
    private String timeFile;
    private String timePath;


    // Constructor
    public Configure(String configFileName, String algorithmName){
        read(configFileName, algorithmName);

        configPath = base + "/baseConfig/";
        testCasePath = base + "/containerData/static/" + experimentClass + "/" + testCaseName + ".csv";
        VMConfigPath = configPath + "VMConfig/" + balancedOrNot + "/VMConfig_" + numOfVmTypesNames + ".csv";
        PMConfigPath = configPath + "PMConfig/" + pmSize;


        // These are the addresses of result
//        testCaseResultPath = algorithmBase + "/" + numOfVmTypesNames + "/" + testCaseName + "/";
        testCaseResultPath = resultBase;
        new File(testCaseResultPath).mkdirs();
        energyPath = testCaseResultPath + energyFile;
        wastedResourcePath = testCaseResultPath + wastedResourceFile;
        cpuMemUtilPath = testCaseResultPath + cpuMemUtilFile;
        numOfPmPath = testCaseResultPath + numOfPmFile;
        numOfVmPath = testCaseResultPath + numOfVmFile;
        convergenceCurvePath = testCaseResultPath + convergenceCurveFile;
        timePath = testCaseResultPath + timeFile;
    }

    private void read(String fileName, String algorithmName){
        try {
            Properties pro = new Properties();
            FileInputStream in = new FileInputStream(fileName);
            pro.load(in);
            in.close();

            run = Integer.parseInt(pro.getProperty("run"));
            seed = Integer.parseInt(pro.getProperty("seed"));
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
            vmCpuOverheadRate = Double.parseDouble(pro.getProperty("vmCpuOverheadRate"));
            vmMemOverhead = Double.parseDouble(pro.getProperty("vmMemOverhead"));
            pmSize = pro.getProperty("pmSize");
            numOfVmTypes = Integer.parseInt(pro.getProperty("numOfVmTypes"));
            numOfVmTypesNames = pro.getProperty("numOfVmTypesNames");
            numOfVmName = pro.getProperty("numOfVmName");
            balancedOrNot = pro.getProperty("balancedOrNot");
            experimentClass = pro.getProperty("experimentClass");
            testCaseSize = Integer.parseInt(pro.getProperty("testCaseSize"));
            testCaseName = pro.getProperty("testCaseName");
            base = pro.getProperty("base");
            configPath = pro.getProperty("configPath");
            testCasePath = pro.getProperty("testCasePath");
            resultBase = pro.getProperty("resultBase");
            algorithmBase = resultBase + "/" + algorithmName;
            energyFile = pro.getProperty("energyFile");
            wastedResourceFile = pro.getProperty("wastedResourceFile");
            cpuMemUtilFile = pro.getProperty("cpuMemUtilFile");
            numOfPmFile = pro.getProperty("numOfPmFile");
            numOfVmFile = pro.getProperty("numOfVmFile");
            convergenceCurveFile = pro.getProperty("convergenceCurveFile");
            timeFile = pro.getProperty("timeFile");



        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public int getRun() {
        return run;
    }

    public int getSeed() {
        return seed;
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

    public int getTestCaseSize() {
        return testCaseSize;
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

    public String getWastedResourceFile() {
        return wastedResourceFile;
    }

    public String getCpuMemUtilFile() {
        return cpuMemUtilFile;
    }

    public String getCpuMemUtilPath() {
        return cpuMemUtilPath;
    }

    public String getNumOfPmFile() {
        return numOfPmFile;
    }

    public String getNumOfPmPath() {
        return numOfPmPath;
    }

    public String getNumOfVmFile() {
        return numOfVmFile;
    }

    public String getNumOfVmPath() {
        return numOfVmPath;
    }

    public String getTimeFile() {
        return timeFile;
    }

    public String getTimePath() {
        return timePath;
    }

    public String getVMConfigPath() {
        return VMConfigPath;
    }

    public String getPMConfigPath() {
        return PMConfigPath;
    }


    public String getResultBase() {
        return resultBase;
    }

    public String getAlgorithmBase() {
        return algorithmBase;
    }

    public String getTestCaseResultPath() {
        return testCaseResultPath;
    }

    public String getEnergyFile() {
        return energyFile;
    }

    public String getWastedResourcePath() {
        return wastedResourcePath;
    }

    public String getEnergyPath() {
        return energyPath;
    }


    public String getConvergenceCurveFile() {
        return convergenceCurveFile;
    }

    public String getConvergenceCurvePath() {
        return convergenceCurvePath;
    }

}
