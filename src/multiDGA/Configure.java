package multiDGA;

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
    private String applicationName;

    // test case addresses
    // These are the addresses of test cases
    private String base;
    private String configPath;
    private String testCasePath;
    private String applicationPath;
    private String VMConfigPath;
    private String PMConfigPath;
    private String caseNum;

    // These are the addresses of result
    private String resultBase;
    private String algorithmBase;
    private String testCaseResultPath;
    private String energyFile;
    private String energyPath;
    private String paretoFrontFile;
    private String paretoFrontPath;
    private String wastedResourceFile;
    private String wastedResourcePath;
    private String aveEnergyFile;
    private String aveEnergyPath;
    private String aveCpuMemUtilFile;
    private String aveCpuMemUtilPath;
    private String aveNumOfPmFile;
    private String aveNumOfPmPath;
    private String aveNumOfVmFile;
    private String aveNumOfVmPath;
    private String convergenceCurveFile;
    private String convergenceCurvePath;
    private String aveTimeFile;
    private String aveTimePath;


    // Constructor
    public Configure(String configFileName, String algorithmName){
        read(configFileName, algorithmName);

        configPath = base + "/baseConfig/";
//        testCasePath = base + "/containerData/static/" + testCaseName + ".csv";
        testCasePath = base + "/containerData/static/spaceEstimation/" + testCaseName + "_" + numOfVmTypesNames + "_" + caseNum + ".csv";
        applicationPath = base + "/containerData/static/" + applicationName + ".csv";
        VMConfigPath = configPath + "VMConfig/" + balancedOrNot + "/VMConfig_" + numOfVmTypesNames + ".csv";
        PMConfigPath = configPath + "PMConfig/" + pmSize;


        // These are the addresses of result
//        testCaseResultPath = algorithmBase + "/" + numOfVmTypesNames + "/" + testCaseName + "/";
        testCaseResultPath = resultBase;
        new File(testCaseResultPath).mkdirs();
        energyPath = testCaseResultPath + energyFile;
        paretoFrontPath = testCaseResultPath + paretoFrontFile;
        aveEnergyPath = testCaseResultPath + aveEnergyFile;
        wastedResourcePath = testCaseResultPath + wastedResourceFile;
        aveCpuMemUtilPath = testCaseResultPath + aveCpuMemUtilFile;
        aveNumOfPmPath = testCaseResultPath + aveNumOfPmFile;
        aveNumOfVmPath = testCaseResultPath + aveNumOfVmFile;
        convergenceCurvePath = testCaseResultPath + convergenceCurveFile;
        aveTimePath = testCaseResultPath + aveTimeFile;
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
            crushPro = Double.parseDouble(pro.getProperty("crushPro"));
            vmCpuOverheadRate = Double.parseDouble(pro.getProperty("vmCpuOverheadRate"));
            vmMemOverhead = Double.parseDouble(pro.getProperty("vmMemOverhead"));
            pmSize = pro.getProperty("pmSize");
            numOfVmTypes = Integer.parseInt(pro.getProperty("numOfVmTypes"));
            numOfVmTypesNames = pro.getProperty("numOfVmTypesNames");
            numOfVmName = pro.getProperty("numOfVmName");
            caseNum = pro.getProperty("caseNum");
            applicationSize = Integer.parseInt(pro.getProperty("applicationSize"));
            maximumServiceNum = Integer.parseInt(pro.getProperty("maximumServiceNum"));
            balancedOrNot = pro.getProperty("balancedOrNot");
            testCaseSize = Integer.parseInt(pro.getProperty("testCaseSize"));
            testCaseName = pro.getProperty("testCaseName");
            applicationName = pro.getProperty("applicationName");
            base = pro.getProperty("base");
            configPath = pro.getProperty("configPath");
            testCasePath = pro.getProperty("testCasePath");
            resultBase = pro.getProperty("resultBase");
            algorithmBase = resultBase + "/" + algorithmName;
            energyFile = pro.getProperty("energyFile");
            paretoFrontFile = pro.getProperty("paretoFrontFile");
            wastedResourceFile = pro.getProperty("wastedResourceFile");
            aveEnergyFile = pro.getProperty("aveEnergyFile");
            aveCpuMemUtilFile = pro.getProperty("aveCpuMemUtilFile");
            aveNumOfPmFile = pro.getProperty("aveNumOfPmFile");
            aveNumOfVmFile = pro.getProperty("aveNumOfVmFile");
            convergenceCurveFile = pro.getProperty("convergenceCurveFile");
            aveTimeFile = pro.getProperty("aveTimeFile");


        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public String getParetoFrontPath() {
        return paretoFrontPath;
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

    public int getTestCaseSize() {
        return testCaseSize;
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

    public String getAveEnergyFile() {
        return aveEnergyFile;
    }

    public String getAveEnergyPath() {
        return aveEnergyPath;
    }

    public String getAveCpuMemUtilFile() {
        return aveCpuMemUtilFile;
    }

    public String getAveCpuMemUtilPath() {
        return aveCpuMemUtilPath;
    }

    public String getAveNumOfPmFile() {
        return aveNumOfPmFile;
    }

    public String getAveNumOfPmPath() {
        return aveNumOfPmPath;
    }

    public String getAveNumOfVmFile() {
        return aveNumOfVmFile;
    }

    public String getAveNumOfVmPath() {
        return aveNumOfVmPath;
    }

    public String getConvergenceCurveFile() {
        return convergenceCurveFile;
    }

    public String getConvergenceCurvePath() {
        return convergenceCurvePath;
    }

    public String getAveTimeFile() {
        return aveTimeFile;
    }

    public String getAveTimePath() {
        return aveTimePath;
    }
}
