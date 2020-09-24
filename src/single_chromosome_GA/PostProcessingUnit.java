package single_chromosome_GA;

import PermutationBasedGAForContainerAllocation.DualPermutationChromosome;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader;

import java.util.ArrayList;

public class PostProcessingUnit {
    private ArrayList<SingleChromosome> bestIndividualList;
    private ArrayList<double[]> genTime;
    private ArrayList<Double> time;
    private int run;
    private int generation;

    public PostProcessingUnit(ArrayList<SingleChromosome> bestIndividualList,
                              ArrayList<double[]> genTime, ArrayList<Double> time,
                              int generation, int run
                              ){
        this.bestIndividualList = bestIndividualList;
        this.genTime = genTime;
        this.time = time;
        this.generation = generation;
        this.run = run;
    }

    public double energy(){
        return bestIndividualList.get(generation - 1).getFitness();
    }



    public double waste(){
        return bestIndividualList.get(generation - 1).getAveWasted();
    }



    // The average PM CPU and memory utilization among N runs
    public double[] averageUtil(){
        double[] util = new double[2];
        util[0] = bestIndividualList.get(generation - 1).getAveCpuUtil();
        util[1] = bestIndividualList.get(generation - 1).getAveMemUtil();

        return util;
    }



    // convergence curve
    // calculate the average fitness value of each generation among all N runs
    public double[][] convergenceCurve(){
        double[][] aveFitness = new double[generation][2];
        for(int i = 0; i < generation; i++){
            aveFitness[i] = genTime.get(i);
        }
        return aveFitness;
    }

    // VM number
    public double noOfVm(){
        return bestIndividualList.get(generation - 1).getNumOfVM();
    }


    public double getTime(){
        double total = 0;
        for(Double time1:time){
            total += time1;
        }
        return total / time.size();
    }

    // PM number
    public double noOfPm(){

        return bestIndividualList.get(generation - 1).getNumOfPM();
    }


    private double sumArray(double[] fitness){
        double sum = 0;
        for(int i = 0; i < fitness.length; i++){
            sum += fitness[i];
        }
        return sum;
    }




}
