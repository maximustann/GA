package Rosenbrock;

import java.util.ArrayList;
import java.util.Arrays;

import algorithm.Chromosome;
import dataCollector.DataCollector;

public class ArrayResultCollector implements DataCollector {
	private ArrayList<double[]> resultData;

	public ArrayResultCollector(){
		resultData = new ArrayList<double[]>();
	}
	@Override
	public void collect(Object data) {
		double[] myData;
//		new double[((double [])data).length];
		myData = Arrays.copyOf(((double [])data), ((double [])data).length);
		resultData.add(myData);
	}

	public ArrayList<double[]> getResult(){
		return resultData;
	}

	public void printResult(){
		for(int i = 0; i < resultData.size(); i++){
			for(int j = 0; j < ((double[]) resultData.get(0)).length; j++){
				System.out.print(((double[]) resultData.get(i))[j] + " ");
			}
			System.out.println();
		}
	}
	@Override
	public void collectChromosome(Chromosome[] individual) {
		// TODO Auto-generated method stub

	}

}
