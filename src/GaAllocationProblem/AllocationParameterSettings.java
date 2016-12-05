/*
 * Boxiong Tan (Maximus Tann)
 * Title:        GA  framework
 * Description:  GA framework for general optimization purpose
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2016-2019, The Victoria University of Wellington
 * AllocationParameterSettings.java - allocation parameter setting for Hai's Paper 
 */
package GaAllocationProblem;

/**
 * AllocationParameterSettings for Hai's Paper
 *
 * @author Boxiong Tan (Maximus Tann)
 * @since GA framework 1.0
 */
import ProblemDefine.ProblemParameterSettings;
import algorithms.Evaluate;

public class AllocationParameterSettings extends ProblemParameterSettings{
	private double[] costMatrix;
	private double[] freqMatrix;
	private double[] latencyMatrix;

	/**
	 * 
	 * @param evaluate user defined Evaluation method
	 * @param costMatrix cost matrix, which is an input matrix read from file
	 * @param freqMatrix frequency matrix, which is an input matrix read from file
	 * @param latencyMatrix latency matrix
	 */
	public AllocationParameterSettings(
										Evaluate evaluate, 
										double[] costMatrix,
										double[] freqMatrix, 
										double[] latencyMatrix
										) {
		super(evaluate);
		this.costMatrix = costMatrix;
		this.freqMatrix = freqMatrix;
		this.latencyMatrix = latencyMatrix;
	}

	public double[] getCostMatrix() {
		return costMatrix;
	}

	public double[] getFreqMatrix() {
		return freqMatrix;
	}

	public double[] getLatencyMatrix() {
		return latencyMatrix;
	}

}
