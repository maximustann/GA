package Rosenbrock;

import java.util.ArrayList;

import algorithm.Chromosome;
import algorithm.Evaluate;
import algorithm.FitnessFunc;

public class TestFunctionEvaluate implements Evaluate{
	private ArrayList<FitnessFunc> funcList;

	// constructor, initialize fitness function list
	public TestFunctionEvaluate(ArrayList<FitnessFunc> funcList){
		this.funcList = funcList;
	}

	// You can change the fitness function list
	public void setFuncList(ArrayList<FitnessFunc> funcList){
		this.funcList = funcList;
	}

	@Override
	public void evaluate(Chromosome[] popVar, ArrayList<double[]> popFit) {
		popFit.clear();
		FitnessFunc fitnessFunction = funcList.get(0);
		ArrayList<double[]> tempFit = null;
		try {
			tempFit = fitnessFunction.execute(popVar);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for(int i = 0; i < tempFit.size(); i++) {
			popFit.add(tempFit.get(i));
		}
	}
}
