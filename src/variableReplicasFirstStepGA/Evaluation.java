package variableReplicasFirstStepGA;

import algorithms.Chromosome;
import algorithms.Evaluate;
import algorithms.FitnessFunc;

import java.util.ArrayList;

public class Evaluation implements Evaluate{
    private ArrayList<FitnessFunc> funcList;

    public Evaluation(ArrayList<FitnessFunc> funcList){
        this.funcList = funcList;
    }

    public void setFuncList(ArrayList<FitnessFunc> funcList){
        this.funcList = funcList;
    }

    @Override
    public void evaluate(Chromosome[] popVar, ArrayList<double[]> fitness) {
        fitness.clear();
        ArrayList<ArrayList<double[]>> fitList = new ArrayList<>();

        // evaluate the only objective
        for(int i = 0; i < funcList.size(); i++){
            ArrayList<double[]> tempFit = funcList.get(i).execute(popVar);
            fitList.add(tempFit);
        }

        for(int i = 0; i < popVar.length; i++){
            double[] fit = new double[6];
            // fitness value
            fit[0] = fitList.get(0).get(i)[0];
            fit[1] = fitList.get(1).get(i)[0];
//            System.out.println("energy fitness = " + fit[0] + ", availability fitness = " + fit[1]);

            // order
            fit[2] = i;

            // crowding distance
            fit[3] = 0;

            // ranking
            fit[4] = 0;

            // violations
            fit[5] = 0;
            fitness.add(fit);
            //-------Debug-----------
//            System.out.println("popVar.length = " + popVar.length);
//            System.out.println("fitness = " + fit[0] + ", order = " + fit[1]);
            //-------Debug-----------
        }
    }
}
