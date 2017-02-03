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
	private ArrayList<ArrayList<ArrayList<double[]>>> resultData;
	private ArrayList<ArrayList<WSRP_IntChromosome>> nonDominatedSet;
	private ArrayList<ArrayList<double[]>> nonDominatedFit;



	public ResultCollector(){
		super();
		resultData = new ArrayList<ArrayList<ArrayList<double[]>>>();
		nonDominatedSet = new ArrayList<ArrayList<WSRP_IntChromosome>>();
	}

	/**
	 * add fitness value
	 */
	public void collect(Object data) {
		resultData.add((ArrayList<ArrayList<double[]>>) data);
	}

	public ArrayList<ArrayList<ArrayList<double[]>>> getResult(){
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
	 * It calculates the no-duplicated none dominatedSet for all results.
	 */
	public void postProcessing(){
		ArrayList<ArrayList<WSRP_IntChromosome>> nonDomSet = new ArrayList<ArrayList<WSRP_IntChromosome>>();
		ArrayList<ArrayList<double[]>> nonDomFit = new ArrayList<ArrayList<double[]>>();
		for(int i = 0; i < resultData.get(0).size(); i++){
			ArrayList noDup = eliminatedReplicates(nonDominatedSet.get(i), resultData.get(0).get(i));
			nonDomSet.add((ArrayList<WSRP_IntChromosome>) noDup.get(0));
			nonDomFit.add((ArrayList<double[]>) noDup.get(1));
		}
		nonDominatedSet = nonDomSet;
		nonDominatedFit = nonDomFit;
	}



	/**
	 * print all fitness results
	 */
	public void printResult(){
		for(int i = 0; i < resultData.get(0).size(); i++){
			System.out.println("generation = " + i + ", number = " + resultData.get(0).get(i).size());
			for(int j = 0; j < resultData.get(0).get(i).size(); j++){
//				nonDominatedSet.get(i).get(j).print();
				System.out.println("costFitness = " + resultData.get(0).get(i).get(j)[0]
						+ ", EnergyFitness = " + resultData.get(0).get(i).get(j)[1]
						+ ", index = " + resultData.get(0).get(i).get(j)[2]
						+ ", CD = " + resultData.get(0).get(i).get(j)[3]
						+ ", ranking = " + resultData.get(0).get(i).get(j)[4]
						+ ", violations = " + resultData.get(0).get(i).get(j)[5]);
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
//	public ArrayList<ArrayList<double[]>> getLastResult(int runs, int maxGen){
//		ArrayList<ArrayList<double[]>> lastResults = new ArrayList<ArrayList<double[]>>();
//		for(int i = 1; i <= runs; i++){
//			ArrayList<double[]> run = new ArrayList<double[]>();
//			for(int j = 0; j < resultData.get(i * maxGen - 1).size(); j++){
//				run.add(resultData.get(i * maxGen - 1).get(j));
//			}
//			lastResults.add(run);
//		}
//		return lastResults;
//	}





	/**
	 *
	 * @param inputSet a list of individuals
	 * @param inputFit a list of fitness values of the individuals
	 * @return a list of two lists, entry one is individual, entry two is fitness values
	 */
	private ArrayList eliminatedReplicates(ArrayList<WSRP_IntChromosome> inputSet, ArrayList<double[]> inputFit){
		ArrayList<WSRP_IntChromosome> outputSet = new ArrayList<WSRP_IntChromosome>();
		ArrayList<double[]> outputFit = new ArrayList<double[]>();

		for(int i = 0; i < inputFit.size(); i++){
			// If the the input is not in output set, then add it into output set, also add fitness values
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

	/**
	 * check if the input target is in inputList
	 * @param target an individual
	 * @param inputList a non-dominated set
	 * @return if the individual exists in the inputList, then return true, otherwise false
	 */
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
