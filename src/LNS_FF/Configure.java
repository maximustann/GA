package LNS_FF;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Configure {



    // ###############  Algorithm Parameters #####################
    private int lbound;
    private int ubound;
    private int maxGen;
    private int tabuSize;
    private int improvementThreshold;
    private double replicaChangePercentage;
    private double allocationChangePercentage;

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
            ubound                      = Integer.parseInt(pro.getProperty("ubound"));
            lbound                      = Integer.parseInt(pro.getProperty("lbound"));
            maxGen                      = Integer.parseInt(pro.getProperty("maxGen"));
            tabuSize                    = Integer.parseInt(pro.getProperty("tabuSize"));
            improvementThreshold        = Integer.parseInt(pro.getProperty("improvementThreshold"));
            replicaChangePercentage     = Double.parseDouble(pro.getProperty("replicaChangePercentage"));
            allocationChangePercentage  = Double.parseDouble(pro.getProperty("allocationChangePercentage"));

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

    public int getUbound() {
        return ubound;
    }

    public int getLbound() {
        return lbound;
    }

    public int getMaxGen() {
        return maxGen;
    }

    public int getTabuSize() {
        return tabuSize;
    }

    public int getImprovementThreshold() {
        return improvementThreshold;
    }

    public double getReplicaChangePercentage() {
        return replicaChangePercentage;
    }

    public double getAllocationChangePercentage() {
        return allocationChangePercentage;
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

    public String getReplicaNumPath() {
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
