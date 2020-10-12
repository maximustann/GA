package NSGAII_NSGGA_Manual;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Configure {



    // ###############  Algorithm Parameters #####################

    // NSGAII

    private double nsgaii_ubound;
    private double nsgaii_lbound;
    private double nsgaii_crossoverRate;
    private double nsgaii_mutationRate;
    private int nsgaii_optimization;
    private int nsgaii_tournamentSize;
    private int nsgaii_eliteSize;
    private int nsgaii_popSize;
    private int nsgaii_maxGen;
    private int permutationNum;


    // NSGGA

    private double nsgga_ubound;
    private double nsgga_lbound;
    private double nsgga_crossoverRate;
    private double nsgga_mutationRate;
    private int nsgga_optimization;
    private int nsgga_tournamentSize;
    private int nsgga_eliteSize;
    private int nsgga_popSize;
    private int nsgga_maxGen;


    // ################  Problem Parameters #########################

    // PM Settings

    private String pmConfig;
    private String pmSize;
    private double k;
    private double crushPro;
    private String pmConfigPath;

    // VM Settings

    private String vmConfig;

    private double vmCpuOverheadRate;
    private double vmMemOverhead;

    private String numOfVmTypesNames;
    private int numOfVmTypes;

    private String balancedOrNot;
    private String vmConfigPath;


    // Problem Parameters

    private String base;
    private String configPath;
    private String containerDataPath;
    private String staticDynamic;
    private String experimentClass;
    private String testCaseName;

    private int applicationSize;
    private int microServiceSize;
    private String applicationName;
    private int maximumServiceNum;
//    private int testCaseSize;
    private String testCasePath;
    private String applicationPath;


    // These are the addresses of result
    private String resultBase;

    private String paretoFrontFile;
    private String energyFile;
    private String cpuMemUtilFile;
    private String wastedResourceFile;
    private String numOfPmFile;
    private String numOfVmFile;
    private String convergenceCurveFile;
    private String timeFile;
    private String replicaNumFile;

    private String paretoFrontPath;
    private String energyPath;
    private String cpuMemUtilPath;
    private String wastedResourcePath;
    private String numOfPmPath;
    private String numOfVmPath;
    private String convergenceCurvePath;
    private String timePath;
    private String replicaNumPath;



    // Constructor
    public Configure(String configFileName, String algorithmName){
        read(configFileName, algorithmName);
    }

    private void read(String fileName, String algorithmName){
        try {
            Properties pro = new Properties();
            FileInputStream in = new FileInputStream(fileName);
            pro.load(in);
            in.close();


            // Algorithm Parameters
            // NSGAII

            nsgaii_ubound                      = Double.parseDouble(pro.getProperty("nsgaii_ubound"));
            nsgaii_lbound                      = Double.parseDouble(pro.getProperty("nsgaii_lbound"));
            nsgaii_crossoverRate               = Double.parseDouble(pro.getProperty("nsgaii_crossoverRate"));
            nsgaii_mutationRate                = Double.parseDouble(pro.getProperty("nsgaii_mutationRate"));
            nsgaii_optimization                = Integer.parseInt(pro.getProperty("nsgaii_optimization"));
            nsgaii_tournamentSize              = Integer.parseInt(pro.getProperty("nsgaii_tournamentSize"));
            nsgaii_eliteSize                   = Integer.parseInt(pro.getProperty("nsgaii_eliteSize"));
            nsgaii_popSize                     = Integer.parseInt(pro.getProperty("nsgaii_popSize"));
            nsgaii_maxGen                      = Integer.parseInt(pro.getProperty("nsgaii_maxGen"));
            permutationNum                     = Integer.parseInt(pro.getProperty("permutationNum"));

            // NSGGA
            nsgga_ubound                      = Double.parseDouble(pro.getProperty("nsgga_ubound"));
            nsgga_lbound                      = Double.parseDouble(pro.getProperty("nsgga_lbound"));
            nsgga_crossoverRate               = Double.parseDouble(pro.getProperty("nsgga_crossoverRate"));
            nsgga_mutationRate                = Double.parseDouble(pro.getProperty("nsgga_mutationRate"));
            nsgga_optimization                = Integer.parseInt(pro.getProperty("nsgga_optimization"));
            nsgga_tournamentSize              = Integer.parseInt(pro.getProperty("nsgga_tournamentSize"));
            nsgga_eliteSize                   = Integer.parseInt(pro.getProperty("nsgga_eliteSize"));
            nsgga_popSize                     = Integer.parseInt(pro.getProperty("nsgga_popSize"));
            nsgga_maxGen                      = Integer.parseInt(pro.getProperty("nsgga_maxGen"));

            // Problem Parameters
            // PM Settings

            String base                 = pro.getProperty("base");
            String configPath           = pro.getProperty("configPath");
            String pmConfig             = pro.getProperty("pmConfig");
            pmSize                      = pro.getProperty("pmSize");
            k                           = Double.parseDouble(pro.getProperty("k"));
            crushPro                    = Double.parseDouble(pro.getProperty("crushPro"));
            pmConfigPath                = base + "/" + configPath + pmConfig + "/" +
                                            pmConfig + "_" + pmSize + ".csv";

            // VM Settings

            String vmConfig             = pro.getProperty("vmConfig");

            vmCpuOverheadRate           = Double.parseDouble(pro.getProperty("vmCpuOverheadRate"));
            vmMemOverhead               = Double.parseDouble(pro.getProperty("vmMemOverhead"));

            numOfVmTypesNames           = pro.getProperty("numOfVmTypesNames");
            numOfVmTypes                = Integer.parseInt(pro.getProperty("numOfVmTypes"));

            balancedOrNot               = pro.getProperty("balancedOrNot");
            vmConfigPath                = base + configPath + vmConfig + "/" +
                                            balancedOrNot + "/" + vmConfig + "_" + numOfVmTypesNames + ".csv";


            // Testcase Addresses

            String containerDataPath    = pro.getProperty("containerDataPath");
            String staticDynamic        = pro.getProperty("staticDynamic");
            String experimentClass      = pro.getProperty("experimentClass");
            testCaseName                = pro.getProperty("testCaseName");

            applicationSize             = Integer.parseInt(pro.getProperty("applicationSize"));
            microServiceSize            = Integer.parseInt(pro.getProperty("microServiceSize"));
            applicationName             = pro.getProperty("applicationName");
            maximumServiceNum           = Integer.parseInt(pro.getProperty("maximumServiceNum"));

//            testCaseSize                = Integer.parseInt(pro.getProperty("testCaseSize"));
            testCasePath                = base + "/" + containerDataPath +
                                        "/" + staticDynamic + "/" + experimentClass +
                                        "/" + testCaseName + ".csv";
            applicationPath             = base + "/" + containerDataPath +
                                        "/" + staticDynamic + "/" + experimentClass +
                                        "/" + applicationName + ".csv";

            // Result Addresses

            resultBase                  = pro.getProperty("resultBase");
            energyFile                  = pro.getProperty("energyFile");
            cpuMemUtilFile              = pro.getProperty("cpuMemUtilFile");
            wastedResourceFile          = pro.getProperty("wastedResourceFile");
            paretoFrontFile             = pro.getProperty("paretoFrontFile");
            numOfPmFile                 = pro.getProperty("numOfPmFile");
            numOfVmFile                 = pro.getProperty("numOfVmFile");
            convergenceCurveFile        = pro.getProperty("convergenceCurveFile");
            timeFile                    = pro.getProperty("timeFile");
            replicaNumFile              = pro.getProperty("replicaNumFile");


            energyPath                  = resultBase + energyFile;
            cpuMemUtilPath              = resultBase + cpuMemUtilFile;
            wastedResourcePath          = resultBase + wastedResourceFile;
            paretoFrontPath             = resultBase + paretoFrontFile;
            numOfPmPath                 = resultBase + numOfPmFile;
            numOfVmPath                 = resultBase + numOfVmFile;
            convergenceCurvePath        = resultBase + convergenceCurveFile;
            timePath                    = resultBase + timeFile;
            replicaNumPath              = resultBase + replicaNumFile;

        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public double getNsgaii_ubound() {
        return nsgaii_ubound;
    }

    public double getNsgaii_lbound() {
        return nsgaii_lbound;
    }

    public double getNsgaii_crossoverRate() {
        return nsgaii_crossoverRate;
    }

    public double getNsgaii_mutationRate() {
        return nsgaii_mutationRate;
    }

    public int getNsgaii_optimization() {
        return nsgaii_optimization;
    }

    public int getNsgaii_tournamentSize() {
        return nsgaii_tournamentSize;
    }

    public int getNsgaii_eliteSize() {
        return nsgaii_eliteSize;
    }

    public int getNsgaii_popSize() {
        return nsgaii_popSize;
    }

    public int getNsgaii_maxGen() {
        return nsgaii_maxGen;
    }

    public double getNsgga_ubound() {
        return nsgga_ubound;
    }

    public double getNsgga_lbound() {
        return nsgga_lbound;
    }

    public double getNsgga_crossoverRate() {
        return nsgga_crossoverRate;
    }

    public double getNsgga_mutationRate() {
        return nsgga_mutationRate;
    }

    public int getNsgga_optimization() {
        return nsgga_optimization;
    }

    public int getNsgga_tournamentSize() {
        return nsgga_tournamentSize;
    }

    public int getNsgga_eliteSize() {
        return nsgga_eliteSize;
    }

    public int getNsgga_popSize() {
        return nsgga_popSize;
    }

    public int getNsgga_maxGen() {
        return nsgga_maxGen;
    }

    public int getPermutationNum() {
        return permutationNum;
    }

    public int getNumOfVmTypes() {
        return numOfVmTypes;
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

    public String getBalancedOrNot() {
        return balancedOrNot;
    }

//    public int getTestCaseSize() {
//        return testCaseSize;
//    }


    public int getMaximumServiceNum() {
        return maximumServiceNum;
    }

    public int getApplicationSize() {
        return applicationSize;
    }

    public int getMicroServiceSize() {
        return microServiceSize;
    }

    public String getReplicaNumPath(){
        return replicaNumPath;
    }

    public String getTestCaseName() {
        return testCaseName;
    }

    public String getTestCasePath() {
        return testCasePath;
    }

    public String getBase() {
        return base;
    }

    public String getConfigPath() {
        return configPath;
    }

    public String getPmConfigPath(){
        return pmConfigPath;
    }

    public String getVmConfigPath(){
        return vmConfigPath;
    }

    public String getApplicationPath(){
        return applicationPath;
    }

    public String getPmConfig() {
        return pmConfig;
    }

    public String getParetoFrontPath() {
        return paretoFrontPath;
    }

    public String getEnergyPath() {
        return energyPath;
    }

    public String getCpuMemUtilPath() {
        return cpuMemUtilPath;
    }

    public String getWastedResourcePath() {
        return wastedResourcePath;
    }

    public String getNumOfPmPath() {
        return numOfPmPath;
    }

    public String getNumOfVmPath() {
        return numOfVmPath;
    }

    public String getConvergenceCurvePath() {
        return convergenceCurvePath;
    }

    public String getTimePath() {
        return timePath;
    }
}
