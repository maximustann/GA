package twoStepGA;

import FileHandlers.ReadByCol;
import FileHandlers.ReadByRow;
import FileHandlers.ReadCsvFile;

public class ReadFile {
    private ReadCsvFile readByRow;
    private ReadCsvFile readByCol;

    private int testCaseSize;
    private double VMTypes;
    private double PMCpu;
    private double PMMem;
    private double PMEnergy;
//    private double[][] testCaseSizeTemp;
    private double[][] PMConfig;
    private double[][] VMConfig;
    private double[][] testCase;
    private int[][] application;
//    private double[][] testCaseOs;
    private double[] VMCpu;
    private double[] VMMem;

    public ReadFile(
            int VMTypes,
            int microServiceSize,
            int applicationSize,
            String testCasePath,
            String PMConfigPath,
            String VMConfigPath,
            String applicationPath
    ) {

//        this.testCases = new ArrayList<>();
//        this.testCaseOsList = new ArrayList<>();
//        this.testCaseSize = testCaseSize.clone();

        readByRow = new ReadByRow();
        readByCol = new ReadByCol();
        PMConfig = new double[1][3];
        readByCol.read(PMConfigPath, PMConfig);
        PMCpu = PMConfig[0][0];
        PMMem = PMConfig[0][1];
        PMEnergy = PMConfig[0][2];


        this.VMTypes = VMTypes;

        VMConfig = new double[VMTypes][2];
        readByRow.read(VMConfigPath, VMConfig);
        VMCpu = new double[VMTypes];
        VMMem = new double[VMTypes];
        for(int i = 0; i < VMTypes; ++i){
            VMCpu[i] = VMConfig[i][0];
            VMMem[i] = VMConfig[i][1];
        }

        testCase = new double[4][microServiceSize];
        readByCol.read(testCasePath, testCase);



        application = new int[1][applicationSize];
        double[][] tempMatrix = new double[1][applicationSize];
        readByCol.read(applicationPath, tempMatrix);
//        readByRow.read(microServicePath, tempMatrix);
        for(int i = 0; i < applicationSize; i++){
            application[0][i] = (int)tempMatrix[0][i];
        }


    }

    public double getVMTypes() {
        return VMTypes;
    }
    public double getPMCpu() {
        return PMCpu;
    }
    public double getPMMem() {
        return PMMem;
    }
    public double getPMEnergy(){
        return PMEnergy;
    }
    public double[] getVMCpu() {
        return VMCpu;
    }
    public double[] getVMMem() {
        return VMMem;
    }
    public double[] getServiceCpu() {
        return testCase[0].clone();
    }
    public double[] getServiceMem() {
        return testCase[1].clone();
    }
    public double[] getServiceId(){
        return testCase[3].clone();
    }
    public double[] getApplicationId(){
        return testCase[2].clone();
    }

    public int getTestCaseSize() {
        return testCaseSize;
    }
    //    public double[] getTaskOS() {
//        return testCaseOs[0].clone();
//    }


}
