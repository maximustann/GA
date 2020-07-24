package variableReplicasFirstStepGA;


import algorithms.StdRandom;

import java.util.ArrayList;

public class PostProcessingUnit {
    private ArrayList<ArrayList<FirstStepGAChromosome>> dominantSetList;
    private ArrayList<double[]> genTime;
    private int generation;

    public PostProcessingUnit(ArrayList<ArrayList<FirstStepGAChromosome>> dominantSetList,
                              ArrayList<double[]> genTime,
                              int generation){
        this.dominantSetList = dominantSetList;
        this.genTime = genTime;
        this.generation = generation;
    }

    public FirstStepGAChromosome randomSolutionFromParetoFront(){
        int randomSolutionIndex = StdRandom.uniform(paretoFront().size());
        return paretoFront().get(randomSolutionIndex);
    }

    public ArrayList<FirstStepGAChromosome> paretoFront(){
        return dominantSetList.get(generation - 1);
    }


    public double[][] allParetoFront(){
        int nrow = 0;
        for(int i = 0; i < dominantSetList.size(); i++){
            nrow += dominantSetList.get(i).size();
        }
        // energy, availability, generation
        double[][] paretoFront = new double[nrow][3];

        // counter
        int counter = 0;
        // For each generation, we have a front
        for(int i = 0; i < dominantSetList.size(); i++){
            ArrayList<FirstStepGAChromosome> dominateFront = dominantSetList.get(i);
            int frontSize = dominateFront.size();
            //For each front
            for(int j = 0; j < frontSize; j++){
                paretoFront[counter][0] = dominateFront.get(j).calEnergyFitness();
                paretoFront[counter][1] = dominateFront.get(j).calAvailFitness();
                paretoFront[counter][2] = i;
                counter++;
            }
        }
        return paretoFront;
    }



    public double energy(){
        double energyConsumption = 0.0;
        ArrayList<FirstStepGAChromosome> pareto = paretoFront();

        for(int i = 0; i < pareto.size(); i++){
            energyConsumption += pareto.get(i).calEnergyFitness();
        }
        energyConsumption /= pareto.size();

        return energyConsumption;
    }

    public double availability(){
        double availability = 0.0;
        ArrayList<FirstStepGAChromosome> pareto = paretoFront();
//        System.out.println("pareto size = " + pareto.size());
        for(int i = 0; i < pareto.size(); i++){
            availability += pareto.get(i).calAvailFitness();
//            System.out.println(pareto.get(i).calAvailFitness());
        }
        availability /= pareto.size();

        return availability;

    }
//
////    // The average PM energy consumption among N runs
//    public double averageEnergy(){
//        return energy()[0];
//    }
//    // The SD PM energy consumption among N runs
//    public double sdEnergy(){
//        return 0;
//    }
//
//    public double waste(){
//        double averageWaste = 0;
//        ArrayList<MultiGroupGAChromosome> lastFront = dominantSetList.get(generation - 1);
//        for(int i = 0; i < lastFront.size(); i++){
//            averageWaste += lastFront.get(i).averageWaste();
//        }
//        return averageWaste / lastFront.size();
//    }


//    // The average PM CPU and memory utilization among N generations
//    public double[] averageUtil(){
//        double[] util = new double[2];
//        ArrayList<MultiGroupGAChromosome> lastFront = dominantSetList.get(generation - 1);
//        for(int i = 0; i < lastFront.size(); i++){
//            MultiGroupGAChromosome solution = lastFront.get(i);
//            util[0] += solution.averagePmCpuUtil();
//            util[1] += solution.averagePmMemUtil();
//        }
//
//        util[0] /= lastFront.size();
//        util[1] /= lastFront.size();
//
//        return util;
//    }
//
//    // The SD of PM CPU and memory utilization among N runs
//    public double[] sdUtil(){
//        double[] sdUtil = new double[2];
//        sdUtil[0] = 0;
//        sdUtil[1] = 0;
//
//        return sdUtil;
//    }
//
//
//
//    // convergence curve
//    // calculate the average fitness value of each generation among all N runs
//    public double[][] convergenceCurve(){
//        double[][] aveFitness = new double[generation][2];
//        for(int i = 0; i < generation; i++){
//            aveFitness[i] = genTime.get(i);
//
//        }
//
//        return aveFitness;
//    }
//
////     average VM number
//    public double averageNoOfVm(){
//        ArrayList<MultiGroupGAChromosome> lastGen = dominantSetList.get(generation - 1);
//        int averageVm = 0;
//        for(int i = 0; i < lastGen.size(); i++){
//            averageVm += lastGen.get(i).getNoOfVm();
//        }
//
//        return averageVm / lastGen.size();
//    }
//
//
//    // SD VM number
//    public double sdNoOfVm(){
//        return 0;
//    }
//
//    // average PM number
//    public double averageNoOfPm(){
//        ArrayList<MultiGroupGAChromosome> lastGen = dominantSetList.get(generation - 1);
//        int averagePm = 0;
//        for(int i = 0; i < lastGen.size(); i++){
//            averagePm += lastGen.get(i).getNoOfPm();
//        }
//
//        return averagePm / lastGen.size();
//    }
//
//
//    // SD PM number
//    public double sdNoOfPm(){
//        return 0;
//    }



}
