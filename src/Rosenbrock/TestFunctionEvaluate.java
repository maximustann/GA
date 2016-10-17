package Rosenbrock;

import java.util.ArrayList;

import algorithms.Evaluate;
import algorithms.FitnessFunction;

public class TestFunctionEvaluate implements Evaluate{
	private ArrayList<FitnessFunction> funcList;

	// constructor, initialize fitness function list
	public TestFunctionEvaluate(ArrayList<FitnessFunction> funcList){
		this.funcList = funcList;
	}

	// You can change the fitness function list
	public void setFuncList(ArrayList<FitnessFunction> funcList){
		this.funcList = funcList;
	}

	@Override
	public void evaluate(double[][] popVar, double[] fitness){
		for(int i = 0; i < fitness.length; i++) {
			fitness[i] = funcList.get(0).normalizedFit(popVar)[i];
		}
	}
}
