package Rosenbrock;

import java.util.ArrayList;

import dataCollector.DataCollector;

public class ResultCollector implements DataCollector {
	private ArrayList<Double> resultData;
	private ArrayList<double[][]> particleData;

	public ResultCollector(){
		resultData = new ArrayList<Double>();
		particleData = new ArrayList<double[][]>();
	}
	@Override
	public void collect(Object data) {
		resultData.add((Double) data);
	}

	public ArrayList<Double> getResult(){
		return resultData;
	}
	
	public ArrayList<double[][]> getParticle(){
		return particleData;
	}

	public void printResult(){
		for(int i = 0; i < resultData.size(); i++){
			System.out.println(resultData.get(i));
		}
		System.out.println();
	}
	
	public void printParticle(){
		for(int i = 0; i < particleData.size(); i++){
			for(int j = 0; j < particleData.get(0).length; j++){
				for(int k = 0; k < particleData.get(0)[0].length; k++){
					System.out.print(particleData.get(i)[j][k] + " ");
				}
				System.out.println();
			}
		}
		System.out.println();
	}
	@Override
	public void collectParticle(double[][] particle) {
		particleData.add(particle);
	}
	
	
}
