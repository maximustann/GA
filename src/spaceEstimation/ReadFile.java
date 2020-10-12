package spaceEstimation;

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
//    private double[][] testCaseOs;
    private double[] VMCpu;
    private double[] VMMem;

    public ReadFile(
            int VMTypes,
            int testCaseSize,
            String testCasePath,
            String PMConfigPath,
            String VMConfigPath
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

//        double[][] testCaseSizeTemp = new double[1][1];
//        readByCol.read(testCaseSizePath, testCaseSizeTemp);
//        testCaseSize = (int) testCaseSizeTemp[0][0];


        testCase = new double[5][testCaseSize];
//        testCaseOs = new double[1][testCaseSize];
        readByCol.read(testCasePath, testCase);
//        readByCol.read(testCaseOSPath, testCaseOs);
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
    public double[] getTaskCpu() {
        return testCase[0].clone();
    }
    public double[] getTaskMem() {
        return testCase[1].clone();
    }
    public double[] getTaskAppId(){
        return testCase[2].clone();
    }
    public double[] getTaskMicroServiceId(){
        return testCase[3].clone();
    }
    public double[] getTaskReplicaId(){
        return testCase[4].clone();
    }

    public int getTestCaseSize() {
        return testCaseSize;
    }
    //    public double[] getTaskOS() {
//        return testCaseOs[0].clone();
//    }


}
