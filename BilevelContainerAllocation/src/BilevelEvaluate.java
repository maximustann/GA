import algorithms.Chromosome;
import algorithms.CoEvaluate;
import algorithms.CoFitnessFunc;
import algorithms.FitnessFunc;

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
        // must clean the previous popFit first
        fitness.clear();

        //get fitness function list , just in case you have multiple objectives
        // we know we have only one.
        CoFitnessFunc fitnessFunc = funcList.get(0);
        ArrayList<double[]> tempFit = null;

        try {
            // execute the evaluation
            tempFit = fitnessFunc.execute(subPop, popVar, representatives);
        } catch (Exception e){
            e.printStackTrace();
        }

        for(int i = 0; i < tempFit.size(); i++){
            // fit[0]: fitness value, fit[1]: index,

            double[] fit = new double[2];
            fit[0] = tempFit.get(i)[0];
            fit[1] = i;

            fitness.add(fit);
        }
    }

}
