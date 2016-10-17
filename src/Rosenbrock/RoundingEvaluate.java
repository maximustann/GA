package Rosenbrock;
import java.util.ArrayList;

import algorithms.Evaluate;
import algorithms.FitnessFunction;


public class RoundingEvaluate implements Evaluate {

	private ArrayList<FitnessFunction> funcList;
	private double threshold;

	// constructor, initialize fitness function list
	public RoundingEvaluate(ArrayList<FitnessFunction> funcList, double threshold){
		this.funcList = funcList;
	}

	// You can change the fitness function list
	public void setFuncList(ArrayList<FitnessFunction> funcList){
		this.funcList = funcList;
	}

	private double staticRounding(double var){
		if(var > threshold) return 1;
		else return 0;
	}
	
	@Override
	public void evaluate(double[][] popVar, double[] fitness){
		double[][] binaryPopVar = new double[popVar.length][popVar[0].length];
		
		for(int i = 0; i < popVar.length; i++){
			for(int j = 0; j < popVar[0].length; j++) { 
				binaryPopVar[i][j] = staticRounding(popVar[i][j]);
			}
		}
		
		for(int i = 0; i < fitness.length; i++) {
			fitness[i] = funcList.get(0).normalizedFit(binaryPopVar)[i];
		}
	}

}
