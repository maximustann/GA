/*
 * Boxiong Tan (Maximus Tann)
 * Title:        GA framework
 * Description:  GA framework for general optimization purpose
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2016-2019, The Victoria University of Wellington
 * GAHaiCostFitness.java - cost fitness from Hai's Paper
 */
package wsrp;
import java.util.ArrayList;

import algorithms.*;
/**
 *
 * @author Boxiong Tan (Maximus Tann)
 * @since GA framework 1.0
 */
public class WSRPCostFitness extends UnNormalizedFit{
	private static double[] vmCost;


	public WSRPCostFitness(Chromosome individual){
		super(individual);
	}

	/**
	 *
	 * @param con a user defined constraint, in this case user define a max cost constraint
	 * @param costMatrix cost matrix, read from file, generate from normal distribution [20,100]
	 */
	public WSRPCostFitness(double[] vmCost){
		super(null);
		WSRPCostFitness.vmCost = vmCost;
	}


	public Object call() throws Exception {
		double[] fit = new double[2];
		fit[0] = 0;
		ArrayList<Integer> vmCount = new ArrayList<Integer>();
		int taskNum = individual.size() / 3;
//		for(int i = 0; i < taskNum; i++) {
//			if(vmCount.isEmpty()){
//				fit[0] += vmCost[((WSRP_IntChromosome) individual).individual[i * 3 + 1]];
//				vmCount.add(((WSRP_IntChromosome) individual).individual[i * 3 + 2]);
//				continue;
//			}
//			if(!vmCount.contains(((WSRP_IntChromosome) individual).individual[i * 3 + 2])){
//				fit[0] += vmCost[((WSRP_IntChromosome) individual).individual[i * 3 + 1]];
//				vmCount.add(((WSRP_IntChromosome) individual).individual[i * 3 + 2]);
//			}
//		}
		for(int i = 0; i < taskNum; i++){
			fit[0] += vmCost[((WSRP_IntChromosome) individual).individual[i * 3 + 1]];
		}

		fit[1] = 0;
		return fit;
	}


}
