package wsrp;

import java.util.ArrayList;

import FileHandlers.ReadByRow;

public class PostProcessing {
	private WSRP_Constraint constraint;

	public PostProcessing(
			WSRP_Constraint constraint
			){
		this.constraint = constraint;
	}

	public void process(int runs, ArrayList<ArrayList<double[]>> fitness, ArrayList<ArrayList<double[]>> nonDominatedSet){
		for(int i = 0; i < runs; i++){
			_process(fitness.get(i), nonDominatedSet.get(i));
		}
	}

	public void _process(ArrayList<double[]> fitness, ArrayList<double[]> nonDominatedSet){
		int size = nonDominatedSet.size();
		int[] vmViolations = new int[size];
		int[] serviceViolations = new int[size];
		int[] pmCount = new int[size];
		ArrayList<int[]> vmsInPM = new ArrayList<int[]>();
		ArrayList resourceLeft = new ArrayList();


		WSRP_IntChromosome[] nonDonSet = adaptor(nonDominatedSet);
		int serviceNo = nonDonSet[0].size() / 3;
		// for each individual
		for(int i = 0; i < size; i++){
			int[] pmIndex = constraint.pmCount(nonDonSet[i]);
			pmCount[i] = pmIndex[pmIndex.length - 1] + 1;
			vmViolations[i] = (int) fitness.get(i)[5];
			serviceViolations[i] = constraint.countServiceViolations(nonDonSet[i]);

			int[] serviceInfo = new int[4];
			for(int j = 0; j < serviceNo; j++){
				serviceInfo[0] = j;
				serviceInfo[1] = pmIndex[j];
				serviceInfo[2] = nonDonSet[i].individual[j * 3 + 1];
				serviceInfo[3] = nonDonSet[i].individual[j * 3 + 2];
			}
			vmsInPM.add(serviceInfo);
			resourceLeft.add(constraint.leftResource(nonDonSet[i]));
		}
	}

	private WSRP_IntChromosome[] adaptor(ArrayList<double[]> nonDominatedSet){
		int size = nonDominatedSet.size();
		int maxVar = nonDominatedSet.get(0).length;
		WSRP_IntChromosome[] popVar = new WSRP_IntChromosome[size];
		for(int i = 0; i < size; i++){
			popVar[i] = new WSRP_IntChromosome(maxVar);
			for(int j = 0; j < maxVar; j++){
				popVar[i].individual[j] = (int) nonDominatedSet.get(i)[j];
			}
		}
		return popVar;

	}

}
