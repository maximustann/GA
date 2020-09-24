package GroupGA;
import java.util.ArrayList;

public class PostProcessingUnit {
    private ArrayList<GroupGAChromosome> bestIndividualList;
    private ArrayList<double[]> genTime;
    private ArrayList<Double> timeList;
    private int run;
    private int generation;

    public PostProcessingUnit(ArrayList<GroupGAChromosome> bestIndividualList,
                              ArrayList<double[]> genTime, ArrayList<Double> timeList,
                              int generation, int run){
        this.bestIndividualList = bestIndividualList;
        this.genTime = genTime;
        this.timeList = timeList;
        this.generation = generation;
        this.run = run;
    }

    public double energy(){
        double energyConsumption = bestIndividualList.get(generation - 1).getEnergy();
        return energyConsumption;
    }


    public double waste(){
        return bestIndividualList.get(generation - 1).averageWaste();
    }

    // The average PM CPU and memory utilization among N runs
    public double[] averageUtil(){
        double[] util = new double[2];
        util[0] = bestIndividualList.get(generation - 1).averagePmCpuUtil();
        util[1] = bestIndividualList.get(generation - 1).averagePmMemUtil();

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

    public double time(){
        double total = 0;
        for(Double time:timeList) total += time;
        return total / timeList.size();
    }


//     average VM number
    public double noOfVm(){
        return bestIndividualList.get(generation - 1).getNoOfUsedVm();
    }


    public double noOfPm(){
        return bestIndividualList.get(generation - 1).getNoOfPm();
    }

}
