/*
 * Boxiong Tan (Maximus Tann)
 * Title:        GA framework
 * Description:  GA framework for general optimization purpose
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2016-2019, The Victoria University of Wellington
 * ResultCollector.java - collect the results for Hai's paper
 */
package wsrp;

import java.util.ArrayList;

import dataCollector.DataCollector;
/**
 *
 * @author Boxiong Tan (Maximus Tann)
 * @since GA framework 1.0
 */
public class ResultCollector extends DataCollector {
	private ArrayList<ArrayList<double[]>> resultData;
	private ArrayList<ArrayList<WSRP_IntChromosome>> nonDominatedSet;
	
	

	public ResultCollector(){
		super();
		resultData = new ArrayList<ArrayList<double[]>>();
		nonDominatedSet = new ArrayList<ArrayList<WSRP_IntChromosome>>();
	}

	/**
	 * add fitness value
	 */
	public void collect(Object data) {
		resultData.add((ArrayList<double[]>) data);
	}

	public ArrayList<ArrayList<double[]>> getResult(){
		return resultData;
	}

	/**
	 * 
	 * @return non dominated Set
	 */
	public ArrayList<ArrayList<WSRP_IntChromosome>> getNonDonSet(){
		return nonDominatedSet;
	}

	/**
	 * print all fitness results
	 */
	public void printResult(){
		for(int i = 0; i < resultData.size(); i++){
			System.out.println("generation = " + i + ", number = " + resultData.get(i).size());
			for(int j = 0; j < resultData.get(i).size(); j++){
//				nonDominatedSet.get(i).get(j).print();
				System.out.println("costFitness = " + resultData.get(i).get(j)[0]
						+ ", EnergyFitness = " + resultData.get(i).get(j)[1]
						+ ", index = " + resultData.get(i).get(j)[2]
						+ ", CD = " + resultData.get(i).get(j)[3]
						+ ", ranking = " + resultData.get(i).get(j)[4]
						+ ", violations = " + resultData.get(i).get(j)[5]);
			}
		}
		System.out.println();
	}
	
	public void printNonDonSet(){
		for(int i = 0; i < nonDominatedSet.size(); i++){
			System.out.println("generation = " + i);
			for(int j = 0; j < nonDominatedSet.get(i).size(); j++){
				nonDominatedSet.get(i).get(j).print();
			}
		}
	}

	/**
	 * get the last fitness value of many runs
	 */
	public ArrayList<ArrayList<double[]>> getLastResult(int runs, int maxGen){
		ArrayList<ArrayList<double[]>> lastResults = new ArrayList<ArrayList<double[]>>();
		for(int i = 1; i <= runs; i++){
			ArrayList<double[]> run = new ArrayList<double[]>();
			for(int j = 0; j < resultData.get(i * maxGen - 1).size(); j++){
				run.add(resultData.get(i * maxGen - 1).get(j));
			}
			lastResults.add(run);
		}
		return lastResults;
	}
	
//	public boolean checkFitnessIndPair(){
//		if(resultData.size() != nonDominatedSet.size()){
//			System.out.println("Generation size problem !");
//			return false;
//		}
//		for(int i = 0; i < resultData.size(); i++){
//			System.out.println("Generation = " + i);
//			if(resultData.get(i).size() != nonDominatedSet.get(i).size()){
//				System.out.println("fit size = " + resultData.get(i).size() + " ,nonDominatedSet size = " + nonDominatedSet.get(i).size());
//				System.out.println("None dominated set size problem !");
//				return false;
//			}
//		}
//		System.out.println("Perfect!");
//		return true;
//	}
	
	
	public void postProcessing(){
		ArrayList<ArrayList<WSRP_IntChromosome>> nonDomSet = new ArrayList<ArrayList<WSRP_IntChromosome>>();
		ArrayList<ArrayList<double[]>> nonDomFit = new ArrayList<ArrayList<double[]>>();
		for(int i = 0; i < resultData.size(); i++){
			ArrayList noDup = eliminatedReplicates(nonDominatedSet.get(i), resultData.get(i));
			nonDomSet.add((ArrayList<WSRP_IntChromosome>) noDup.get(0));
			nonDomFit.add((ArrayList<double[]>) noDup.get(1));
		}
		nonDominatedSet = nonDomSet;
		resultData = nonDomFit;
	}
	
	private ArrayList eliminatedReplicates(ArrayList<WSRP_IntChromosome> inputSet, ArrayList<double[]> inputFit){
		ArrayList<WSRP_IntChromosome> outputSet = new ArrayList<WSRP_IntChromosome>();
		ArrayList<double[]> outputFit = new ArrayList<double[]>();
		for(int i = 0; i < inputFit.size(); i++){
			if(!checkDuplicate(inputSet.get(i), outputSet)){
				outputSet.add(inputSet.get(i).clone());
				outputFit.add(inputFit.get(i).clone());
			}
		}
		
		ArrayList returnList = new ArrayList();
		returnList.add(outputSet);
		returnList.add(outputFit);
		return returnList;
	}
	
	private boolean checkDuplicate(WSRP_IntChromosome target, ArrayList<WSRP_IntChromosome> inputList){
		for(int i = 0; i < inputList.size(); i++){
			if(inputList.get(i).equals(target)){
				return true;
			}
		}
		return false;
	}
	
	
	public void proNonDominatePrinting(){
	}
	
	
	
	

	@Override
	public void collectSet(Object set) {
		
		// TODO Auto-generated method stub
		nonDominatedSet.add((ArrayList<WSRP_IntChromosome>)set);
	}


//	public void mean(int runs){
//		int size = resultData.size();
//		int gen = size / runs;
//		double best = 0;
//		for(int i = 1; i <= runs; i++) {
//			best += resultData.get(i * gen - 1)[0];
//		}
//		System.out.println(best / runs);
//	}

}
