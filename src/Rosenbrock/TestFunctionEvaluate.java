package Rosenbrock;

import java.util.ArrayList;

import algorithm.Chromosome;
import algorithm.Evaluate;
import algorithm.FitnessFunc;
import algorithm.FitnessFunction;

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
	public void evaluate(Chromosome[] popVar, ArrayList<double[]> popFit) {
		popFit.clear();
		FitnessFunction fitnessFunction = funcList.get(0);
		ArrayList<double[]> tempFit = fitnessFunction.normalizedFit(popVar);
		for(int i = 0; i < tempFit.size(); i++) {
			popFit.add(tempFit.get(i));
		}
	}
}
