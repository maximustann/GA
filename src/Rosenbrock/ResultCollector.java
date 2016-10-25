package Rosenbrock;

import java.util.ArrayList;

import algorithm.Chromosome;
import dataCollector.DataCollector;

public class ResultCollector implements DataCollector {
	private ArrayList<double[]> resultData;
	private ArrayList<Chromosome[]> data;

	public ResultCollector(){
		resultData = new ArrayList<double[]>();
		data = new ArrayList<Chromosome[]>();
	}
	@Override
	public void collect(Object data) {
		resultData.add((double[]) data);
	}

	public ArrayList<double[]> getResult(){
		return resultData;
	}
	

	public void printResult(){
		for(int i = 0; i < resultData.size(); i++){
			System.out.println("fitness: " + resultData.get(i)[0]);
		}
		System.out.println();
	}
	
	public void printPop(){
		for(int i = 0; i < data.size(); i++) {
			for(int j = 0; j < data.get(0).length; j++) {
				data.get(i)[j].print();
				System.out.println();
			}
			System.out.println();
		}
	}
	
	
	
	@Override
	public void collectChromosome(Chromosome[] popVar) {
		data.add(popVar);
	}
	
	
}
