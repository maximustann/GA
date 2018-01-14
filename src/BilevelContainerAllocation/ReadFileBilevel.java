package BilevelContainerAllocation;

import FileHandlers.ReadByCol;
import FileHandlers.ReadByRow;
import FileHandlers.ReadCsvFile;

public class ReadFileBilevel {
    private ReadCsvFile readByRow;
    private ReadCsvFile readByCol;

    private double taskNum;
    private double VMTypes;
    private double PMCpu;
    private double PMMem;
    private double PMEnergy;
    private double[][] ProblemConfig;
    private double[][] PMConfig;
    private double[][] VMConfig;
    private double[] VMCpu;
    private double[] VMMem;
    private double[][] taskCpu;
    private double[][] taskMem;

    public ReadFileBilevel(
            String ProblemConfigPath,
            String PMConfigPath,
            String VMConfigPath,
            String taskCpuPath,
            String taskMemPath
    ){

        readByRow = new ReadByRow();
        readByCol = new ReadByCol();

        ProblemConfig = new double[1][2];
        readByCol.read(ProblemConfigPath, ProblemConfig);
        VMTypes = ProblemConfig[0][0];
        taskNum = ProblemConfig[0][1];

        VMConfig = new double[(int) VMTypes][2];
        readByRow.read(VMConfigPath, VMConfig);
        VMCpu = new double[(int) VMTypes];
        VMMem = new double[(int) VMTypes];
        for(int i = 0; i < VMTypes; i++){
            VMCpu[i] = VMConfig[i][0];
            VMMem[i] = VMConfig[i][1];
        }

        PMConfig = new double[1][3];
        readByCol.read(PMConfigPath, PMConfig);
        PMCpu = PMConfig[0][0];
        PMMem = PMConfig[0][1];
        PMEnergy = PMConfig[0][2];

        taskCpu = new double[1][(int) taskNum];
        readByCol.read(taskCpuPath, taskCpu);

        taskMem = new double[1][(int) taskNum];
        readByCol.read(taskMemPath, taskMem);

        // End ReadFileWSRP
    }

    public double getTaskNum() {
        return taskNum;
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
        return taskCpu[0];
    }

    public double[] getTaskMem() {
        return taskMem[0];
    }


}
