/*
 * Boxiong Tan (Maximus Tann)
 * Title:        GA framework
 * Description:  GA framework for general optimization purpose
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2016-2019, The Victoria University of Wellington
 * ReadFileWSRP.java - read configuration and data from files for WSRP problem
 */
package wsrp;

import FileHandlers.ReadByCol;
import FileHandlers.ReadByRow;
import FileHandlers.ReadCsvFile;
/**
 * ReadFileWSRP
 * 
 * @author Boxiong Tan (Maximus Tann)
 * @since PSO framework 1.0
 */
public class ReadFileWSRP{
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
	private double[][] VMCost;
	private double[][] taskCpu;
	private double[][] taskMem;
	private double[][] taskFreq;
	private double[][] taskSuccRate;
	private double[][] utilizationThreshold;

	public ReadFileWSRP(
					String ProblemConfigPath,
					String PMConfigPath, 
					String VMConfigPath, 
					String VMCostPath, 
					String taskCpuPath,
					String taskFreqPath,
					String taskMemPath,
					String taskSRPath,
					String utilizationThresholdPath
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
		
		VMCost = new double[1][(int) VMTypes];
		readByCol.read(VMCostPath, VMCost);
		
		taskCpu = new double[1][(int) taskNum];
		readByCol.read(taskCpuPath, taskCpu);
		
		taskMem = new double[1][(int) taskNum];
		readByCol.read(taskMemPath, taskMem);
		
		taskFreq = new double[1][(int) taskNum];
		readByCol.read(taskFreqPath, taskFreq);

		taskSuccRate = new double[1][(int) taskNum];
		readByCol.read(taskSRPath, taskSuccRate);
		
		utilizationThreshold = new double[1][1];
		readByCol.read(utilizationThresholdPath, utilizationThreshold);
	
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

	public double[] getVMCost() {
		return VMCost[0];
	}

	public double[] getTaskCpu() {
		return taskCpu[0];
	}

	public double[] getTaskMem() {
		return taskMem[0];
	}

	public double[] getTaskFreq() {
		return taskFreq[0];
	}

	public double[] getTaskSuccRate() {
		return taskSuccRate[0];
	}

	public double getUtilizationThreshold() {
		return utilizationThreshold[0][0];
	}



}