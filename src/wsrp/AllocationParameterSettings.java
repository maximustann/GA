/*
 * Boxiong Tan (Maximus Tann)
 * Title:        GA  framework
 * Description:  GA framework for general optimization purpose
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2016-2019, The Victoria University of Wellington
 * AllocationParameterSettings.java - allocation parameter setting for Hai's Paper
 */
package wsrp;

/**
 * AllocationParameterSettings for Hai's Paper
 *
 * @author Boxiong Tan (Maximus Tann)
 * @since GA framework 1.0
 */
import ProblemDefine.ProblemParameterSettings;
import algorithms.Distance;
import algorithms.Evaluate;
import algorithms.InitPop;
import algorithms.Mutation;

public class AllocationParameterSettings extends ProblemParameterSettings{
	private int vmTypes;
	private int taskNum;
	private double pmCpu;
	private double pmMem;
	private double pmEnergy;
	private double[] vmCpu;
	private double[] vmMem;
	private double[] vmCost;
	private double[] taskCpu;
	private double[] taskMem;
	private double[] taskFreq;
	private InitPop initMethod;
	private Mutation mutate;
	private WSRP_Constraint constraint;
	private Distance crowd;
	/**
	 * @param evaluate user defined Evaluation method
	 */
	public AllocationParameterSettings(
										Evaluate evaluate,
										InitPop initMethod,
										Mutation mutate,
										WSRP_Constraint constraint,
										Distance crowd,
										int vmTypes,
										int taskNum,
										double pmCpu,
										double pmMem,
										double pmEnergy,
										double[] vmCpu,
										double[] vmMem,
										double[] vmCost,
										double[] taskCpu,
										double[] taskMem,
										double[] taskFreq
										) {
		super(evaluate);
		this.vmTypes = vmTypes;
		this.taskNum = taskNum;
		this.pmCpu = pmCpu;
		this.pmMem = pmMem;
		this.pmEnergy = pmEnergy;
		this.vmCpu = vmCpu;
		this.vmMem = vmMem;
		this.vmCost = vmCost;
		this.taskCpu = taskCpu;
		this.taskMem = taskMem;
		this.taskFreq = taskFreq;
		this.initMethod = initMethod;
		this.mutate = mutate;
		this.constraint = constraint;
		this.crowd = crowd;
	}

	public Distance getDistance(){
		return crowd;
	}
	public WSRP_Constraint getConstraint(){
		return constraint;
	}
	public Mutation getMutatioin(){
		return mutate;
	}

	public InitPop getInitPop(){
		return initMethod;
	}
	public int getVmTypes() {
		return vmTypes;
	}
	public int getTaskNum() {
		return taskNum;
	}
	public double getPmCpu() {
		return pmCpu;
	}
	public double getPmMem() {
		return pmMem;
	}
	public double getPmEnergy() {
		return pmEnergy;
	}
	public double[] getVmCpu() {
		return vmCpu;
	}
	public double[] getVmMem() {
		return vmMem;
	}
	public double[] getVmCost() {
		return vmCost;
	}
	public double[] getTaskCpu() {
		return taskCpu;
	}
	public double[] getTaskMem() {
		return taskMem;
	}
	public double[] getTaskFreq() {
		return taskFreq;
	}


}
