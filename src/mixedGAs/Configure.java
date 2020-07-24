package mixedGAs;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Configure {

    // general parameters
    private int run;
    private int seed;

    // dual permutation GA parameters
    private double dual_crossoverRate;
    private double dual_mutationRate;
    private int dual_optimization;
    private int dual_tournamentSize;
    private int dual_eliteSize;
    private int dual_popSize;
    private int dual_maxGen;
    private double dual_ubound;
    private double dual_lbound;

    // dual permutation GA parameters
    private double group_crossoverRate;
    private double group_mutationRate;
    private int group_optimization;
    private int group_tournamentSize;
    private int group_eliteSize;
    private int group_popSize;
    private int group_maxGen;
    private double group_ubound;
    private double group_lbound;

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
    private String aveEnergyFile;
    private String aveEnergyPath;
    private String wastedResourceFile;
    private String wastedResourcePath;
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
        testCasePath = base + "/containerData/static/" + testCaseName + ".csv";
        VMConfigPath = configPath + "VMConfig/" + balancedOrNot + "/VMConfig_" + numOfVmTypesNames + ".csv";
        PMConfigPath = configPath + "PMConfig/" + pmSize;


        // These are the addresses of result
//        testCaseResultPath = algorithmBase + "/" + numOfVmTypesNames + "/" + testCaseName + "/";
        testCaseResultPath = resultBase;
        new File(testCaseResultPath).mkdirs();
        energyPath = testCaseResultPath + energyFile;
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

            // dual permutation GA parameters
            dual_ubound = Double.parseDouble(pro.getProperty("dual_ubound"));
            dual_lbound = Double.parseDouble(pro.getProperty("dual_lbound"));
            dual_crossoverRate = Double.parseDouble(pro.getProperty("dual_crossoverRate"));
            dual_mutationRate = Double.parseDouble(pro.getProperty("dual_mutationRate"));
            dual_optimization = Integer.parseInt(pro.getProperty("dual_optimization"));
            dual_tournamentSize = Integer.parseInt(pro.getProperty("dual_tournamentSize"));
            dual_eliteSize = Integer.parseInt(pro.getProperty("dual_eliteSize"));
            dual_popSize = Integer.parseInt(pro.getProperty("dual_popSize"));
            dual_maxGen = Integer.parseInt(pro.getProperty("dual_maxGen"));

            // Grouped GA parameters
            group_ubound = Double.parseDouble(pro.getProperty("group_ubound"));
            group_lbound = Double.parseDouble(pro.getProperty("group_lbound"));
            group_crossoverRate = Double.parseDouble(pro.getProperty("group_crossoverRate"));
            group_mutationRate = Double.parseDouble(pro.getProperty("group_mutationRate"));
            group_optimization = Integer.parseInt(pro.getProperty("group_optimization"));
            group_tournamentSize = Integer.parseInt(pro.getProperty("group_tournamentSize"));
            group_eliteSize = Integer.parseInt(pro.getProperty("group_eliteSize"));
            group_popSize = Integer.parseInt(pro.getProperty("group_popSize"));
            group_maxGen = Integer.parseInt(pro.getProperty("group_maxGen"));

            k = Double.parseDouble(pro.getProperty("k"));
            vmCpuOverheadRate = Double.parseDouble(pro.getProperty("vmCpuOverheadRate"));
            vmMemOverhead = Double.parseDouble(pro.getProperty("vmMemOverhead"));
            pmSize = pro.getProperty("pmSize");
            numOfVmTypes = Integer.parseInt(pro.getProperty("numOfVmTypes"));
            numOfVmTypesNames = pro.getProperty("numOfVmTypesNames");
            numOfVmName = pro.getProperty("numOfVmName");
            balancedOrNot = pro.getProperty("balancedOrNot");
            testCaseSize = Integer.parseInt(pro.getProperty("testCaseSize"));
            testCaseName = pro.getProperty("testCaseName");
            base = pro.getProperty("base");
            configPath = pro.getProperty("configPath");
            testCasePath = pro.getProperty("testCasePath");
            resultBase = pro.getProperty("resultBase");
            algorithmBase = resultBase + "/" + algorithmName;
            energyFile = pro.getProperty("energyFile");
            aveEnergyFile = pro.getProperty("aveEnergyFile");
            wastedResourceFile = pro.getProperty("wastedResourceFile");
            aveCpuMemUtilFile = pro.getProperty("aveCpuMemUtilFile");
            aveNumOfPmFile = pro.getProperty("aveNumOfPmFile");
            aveNumOfVmFile = pro.getProperty("aveNumOfVmFile");
            convergenceCurveFile = pro.getProperty("convergenceCurveFile");
            aveTimeFile = pro.getProperty("aveTimeFile");

        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public double getDual_crossoverRate() {
        return dual_crossoverRate;
    }

    public double getDual_mutationRate() {
        return dual_mutationRate;
    }

    public int getDual_optimization() {
        return dual_optimization;
    }

    public int getDual_tournamentSize() {
        return dual_tournamentSize;
    }

    public int getDual_eliteSize() {
        return dual_eliteSize;
    }

    public int getDual_popSize() {
        return dual_popSize;
    }

    public int getDual_maxGen() {
        return dual_maxGen;
    }

    public double getDual_ubound() {
        return dual_ubound;
    }

    public double getDual_lbound() {
        return dual_lbound;
    }

    public double getGroup_crossoverRate() {
        return group_crossoverRate;
    }

    public double getGroup_mutationRate() {
        return group_mutationRate;
    }

    public int getGroup_optimization() {
        return group_optimization;
    }

    public int getGroup_tournamentSize() {
        return group_tournamentSize;
    }

    public int getGroup_eliteSize() {
        return group_eliteSize;
    }

    public int getGroup_popSize() {
        return group_popSize;
    }

    public int getGroup_maxGen() {
        return group_maxGen;
    }

    public double getGroup_ubound() {
        return group_ubound;
    }

    public double getGroup_lbound() {
        return group_lbound;
    }

    public int getRun() {
        return run;
    }

    public int getSeed() {
        return seed;
    }

    public int getNumOfVmTypes() {
        return numOfVmTypes;
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

    public String getEnergyPath() {
        return energyPath;
    }

    public String getWastedResourcePath() {
        return wastedResourcePath;
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
