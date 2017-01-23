package wsrp;


import java.util.ArrayList;

import FileHandlers.ReadByRow;

public class PostProcessing {
	private WSRP_Constraint constraint;
	private ArrayList<ArrayList<double[]>> emptiness;
	private ArrayList<ArrayList<Double>> utilization;

	public PostProcessing(WSRP_Constraint constraint){
		this.constraint = constraint;
	}

	public void processing(ArrayList<ArrayList<WSRP_IntChromosome>> nonDom){
		emptiness = new ArrayList<ArrayList<double[]>>();
		utilization = new ArrayList<ArrayList<Double>>();
		// for each generation
		for(int i = 0; i < nonDom.size(); i++){
			// for each none dominated set
			ArrayList<double[]> genEmptiness = new ArrayList<double[]>();
			ArrayList<Double> genUtil = new ArrayList<Double>();
			for(int j = 0; j < nonDom.get(i).size(); j++){
				genEmptiness.add(constraint.emptinessMean(nonDom.get(i).get(j)));
				genUtil.add(constraint.averageNonVioVmUtil(nonDom.get(i).get(j)));
			}
			emptiness.add(genEmptiness);
			utilization.add(genUtil);
		}
	}

	public void printEmptiness(){
		for(int i = 0; i < emptiness.size(); i++){
			System.out.println("Generation = " + i);
			for(int j = 0; j < emptiness.get(i).size(); j++){
				System.out.println("Cpu emptiness = " + emptiness.get(i).get(j)[0] +
								   " ,Mem emptiness = " + emptiness.get(i).get(j)[1]);
			}
		}
	}

	public void printUtilization(){
		for(int i = 0; i < utilization.size(); i++){
			System.out.println("Generation = " + i);
			for(int j = 0; j < emptiness.get(i).size(); j++){
				System.out.println("uitlization = " + utilization.get(i).get(j));
			}
		}
	}

	public void printEmpUtil(){
		for(int i = 0; i < utilization.size(); i++){
			System.out.println("Generation = " + i);
			for(int j = 0; j < utilization.get(i).size(); j++){
				System.out.println("PM Cpu utilization = " + emptiness.get(i).get(j)[0] +
								   " ,PM Mem utilization = " + emptiness.get(i).get(j)[1] +
								   " ,vmNoVia utilization = " + utilization.get(i).get(j));
			}
		}
	}

}
