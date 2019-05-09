import algorithms.Chromosome;
import algorithms.Evaluate;
import algorithms.FitnessFunc;

import java.util.ArrayList;

public class AllocationEvaluation implements Evaluate{
    private ArrayList<FitnessFunc> funcList;

    public AllocationEvaluation(ArrayList<FitnessFunc> funcList){
        this.funcList = funcList;
    }

    public void setFuncList(ArrayList<FitnessFunc> funcList){
        this.funcList = funcList;
    }

    public void evaluate(Chromosome[] popVar, ArrayList<double[]> fitness){
        // we must clean the fitness every time
        fitness.clear();
        ArrayList<ArrayList<double[]>> fitList = new ArrayList<>();
        for(int i = 0; i < funcList.size(); i++){
            ArrayList<double[]> fit = funcList.get(i).execute(popVar);
            fitList.add(fit);
        }

        for(int i = 0; i < popVar.length; i++){
            double[] fit = new double[1];
            fit[0] = fitList.get(0).get(i)[0];
            fitness.add(fit);
        }

    }
}
