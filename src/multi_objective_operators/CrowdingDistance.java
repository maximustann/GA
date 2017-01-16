package multi_objective_operators;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import algorithms.Chromosome;
import algorithms.Distance;

public class CrowdingDistance implements Distance {
	private int optimization;
	public CrowdingDistance(int optimization){
		this.optimization = optimization;
	}
	public void calculate(Chromosome[] popVar, ArrayList<double[]> 	popFit){
		int ranking = 0, index = 0, lastIndex = 0;
		int popSize = popVar.length;
		Chromosome[] newPop = new Chromosome[popSize];
		while(true){
			if(index == popFit.size()){
				_calculate(popFit, index, lastIndex);
				break;
			}
			
			if(ranking != (int) popFit.get(index)[4]){
				ranking = (int) popFit.get(index)[4];
				_calculate(popFit, index, lastIndex);
				lastIndex = index;
			}
			index++;
		}
		// adjust the order of population as well as the index of the chromosomes 
		for(int i = 0; i < popSize; i++){
			newPop[i] = popVar[(int) popFit.get(i)[2]];
			popFit.get(i)[2] = i;
		}
		popVar = newPop;
	}

	private void _calculate(ArrayList<double[]> popFit, int index, int lastIndex) {
		ArrayList<double[]> innerPopFit = new ArrayList<double[]>();
		// new a new popFit, make sure it does not affect the old one
		for(int i = lastIndex; i < index; i++) {
			innerPopFit.add(popFit.get(i).clone());
		}
		if(innerPopFit.size() == 0) return;
		if(innerPopFit.size() == 1) {
			popFit.get(lastIndex)[3] = Double.POSITIVE_INFINITY;
			return;
		}
		if(innerPopFit.size() == 2) {
			popFit.get(lastIndex)[3] = Double.POSITIVE_INFINITY;
			popFit.get(lastIndex + 1)[3] = Double.POSITIVE_INFINITY;
			return;
		}
		
		// sort the first objective according to their fitness values.
		Collections.sort(innerPopFit, new Comparator<double[]>() {
			@Override
			public int compare(double[] fitness1, double[] fitness2) {
				int condition = 0;
				if(fitness1[0] - fitness2[0] > 0.0) condition = 1;
				else if(fitness1[0] - fitness2[0] < 0.0) condition = -1;
				else condition = 0;
				return condition;
			}
		});
		if(optimization == 1) Collections.reverse(innerPopFit);
		
		// get the values on the edge, for normalization
		double objectiveMaxn = innerPopFit.get(0)[0];
		double objectiveMinn = innerPopFit.get(innerPopFit.size() - 1)[0];
		
		// assign infinite value
		innerPopFit.get(0)[3] = Double.POSITIVE_INFINITY;
		innerPopFit.get(innerPopFit.size() - 1)[3] = Double.POSITIVE_INFINITY;
		
		
		// calculate the distance for the individuals in between 
		for(int j = 1; j < innerPopFit.size() - 1; j++){
			double distance = innerPopFit.get(j + 1)[0] - innerPopFit.get(j - 1)[0];
			distance = distance / Math.abs(objectiveMaxn - objectiveMinn);
			if(distance == distance)
				innerPopFit.get(j)[3] = distance;
		}
		
		// sort the second objective according to their fitness values.
		Collections.sort(innerPopFit, new Comparator<double[]>() {
			@Override
			public int compare(double[] fitness1, double[] fitness2) {
				int condition = 0;
				if(fitness1[1] - fitness2[1] > 0.0) condition = 1;
				else if(fitness1[1] - fitness2[1] < 0.0) condition = -1;
				else condition = 0;
				return condition;
			}
		});
		if(optimization == 1) Collections.reverse(innerPopFit);
		
		// get the values on the edge, for normalization
		objectiveMaxn = innerPopFit.get(0)[1];
		objectiveMinn = innerPopFit.get(innerPopFit.size() - 1)[1];
	
		// assign infinite value
		innerPopFit.get(0)[3] = Double.POSITIVE_INFINITY;
		innerPopFit.get(innerPopFit.size() - 1)[3] = Double.POSITIVE_INFINITY;
		
		// calculate the distance for the individuals in between 
		for(int j = 1; j < innerPopFit.size() - 1; j++){
			double distance = innerPopFit.get(j + 1)[1] - innerPopFit.get(j - 1)[1];
			// normalize
			distance = distance / Math.abs(objectiveMaxn - objectiveMinn);
			// if the max == min, the objective's distance is discarded
			if(distance == distance)
				innerPopFit.get(j)[3] += distance;
		}
		
		// sort according to their crowding distance.
		Collections.sort(innerPopFit, new Comparator<double[]>() {
			@Override
			public int compare(double[] fitness1, double[] fitness2) {
				int condition = 0;
				if(fitness1[3] - fitness2[3] > 0.0) condition = 1;
				else if(fitness1[3] - fitness2[3] < 0.0) condition = -1;
				else condition = 0;
				return condition;
			}
		});	
		Collections.reverse(innerPopFit);
		
		for(int i = lastIndex, j = 0; i < index; i++, j++) 
			popFit.set(i, innerPopFit.get(j));
	}
	
}
