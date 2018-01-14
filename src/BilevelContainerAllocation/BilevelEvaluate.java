package BilevelContainerAllocation;

import algorithms.Chromosome;
import algorithms.CoEvaluate;
import algorithms.CoFitnessFunc;

import java.util.ArrayList;

public class BilevelEvaluate implements CoEvaluate{
    /**
     * Evaluation function list
     */
    private ArrayList<CoFitnessFunc> funcList;
    /**
     * @param funcList Evaluation function list
     */

    public BilevelEvaluate(ArrayList<CoFitnessFunc> funcList){
        this.funcList = funcList;
    }

    public void setFuncList(ArrayList<CoFitnessFunc> funcList){
        this.funcList = funcList;
    }

    public void evaluate(int subPop, Chromosome[] popVar,
                         Chromosome[] representatives, ArrayList<double[]> fitness) {
        fitness.clear();
        ArrayList<ArrayList<double[]>> fitList = new ArrayList<ArrayList<double[]>>();

        // Although, in this problem, we only have one objective.
        for(int i = 0; i < funcList.size(); i++){
            ArrayList<double[]> tempFit = funcList.get(i).execute(subPop, popVar, representatives);
            fitList.add(tempFit);
        }

        for(int i = 0; i < popVar.length; i++){
            // fit[0]: fitness value, fit[1]: index,

            double[] fit = new double[2];
            fit[0] = fitList.get(0).get(i)[0];
            fit[1] = i;

            fitness.add(fit);
        }
    }

}
