package single_chromosome_GA;

import java.util.ArrayList;

public class PostProcessingUnit {

    private ArrayList<SingleChromosome> bestIndividualList;
    private ArrayList<double[]> genTime;
    private int run;
    private int generation;


    public PostProcessingUnit(ArrayList<SingleChromosome> bestIndividualList,
                              ArrayList<double[]> genTime,
                              int generation, int run){
        this.bestIndividualList = bestIndividualList;
        this.genTime = genTime;
        this.generation = generation;
        this.run = run;
    }

    public double[] energy(){
        double[] energyConsumption = new double[1];
        energyConsumption[0] = bestIndividualList.get(generation - 1).getFitness();
        return energyConsumption;
    }

    public double sdEnergy(){
        return 0;
    }

    public double[] sdUtil(){
        double[] sdutil = new double[2];
        sdutil[0] = 0;
        sdutil[1] = 0;
        return sdutil;
    }
    public double sdNoOfPm(){
        return 0;
    }
    public double sdNoOfVm(){
        return 0;
    }

    public double[][] convergenceCurve(){
        double[][] aveFitness = new double[generation][2];
        for(int i = 0; i < generation; i++){
            aveFitness[i] = genTime.get(i);
        }
        return aveFitness;
    }

    public double averageEnergy(){
        return bestIndividualList.get(generation - 1).getFitness();
    }

    public double averageNoOfPm(){
        return bestIndividualList.get(generation - 1).getNumOfPM();
    }
    public double averageNoOfVm(){
        return bestIndividualList.get(generation - 1).getNumOfVM();
    }

    public double[] averageUtil(){
        double[] util = new double[2];
        util[0] = bestIndividualList.get(generation - 1).getAveCpuUtil();
        util[1] = bestIndividualList.get(generation - 1).getAveMemUtil();
        return util;
    }

    public double waste(){
        return bestIndividualList.get(generation - 1).getAveWasted();
    }

}
