package twoStepGA;

import multiobjectiveMicroService.MultiGroupGAChromosome;

import java.util.ArrayList;

public class NSGGAPostProcessingUnit {
    private ArrayList<ArrayList<MultiGroupGAChromosome>> dominantSetList;
    private ArrayList<double[]> genTime;
    private int generation;
    private double meanTime;
    private double sdTime;

    public NSGGAPostProcessingUnit(
            ArrayList<ArrayList<MultiGroupGAChromosome>> dominantSetList,
            ArrayList<double[]> genTime,
            int generation,
            double meanTime,
            double sdTime
            ){
        this.dominantSetList = dominantSetList;
        this.genTime = genTime;
        this.generation = generation;
        this.meanTime = meanTime;
        this.sdTime = sdTime;
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
            ArrayList<MultiGroupGAChromosome> dominateFront = dominantSetList.get(i);
            int frontSize = dominateFront.size();
            //For each front
            for(int j = 0; j < frontSize; j++){
                paretoFront[counter][0] = dominateFront.get(j).getEnergyFitness();
                paretoFront[counter][1] = dominateFront.get(j).getAvailabilityFitness();
                paretoFront[counter][2] = i;
                counter++;
            }
        }
        return paretoFront;
    }

    public double[] energy(){
        double[] energyConsumption = new double[1];
        ArrayList<MultiGroupGAChromosome> lastSet = dominantSetList.get(generation - 1);

        for(int i = 0; i < lastSet.size(); i++){
            energyConsumption[0] += lastSet.get(i).getEnergy();
        }
        energyConsumption[0] /= lastSet.size();

        return energyConsumption;
    }

    public double waste(){
        double averageWaste = 0;
        ArrayList<MultiGroupGAChromosome> lastFront = dominantSetList.get(generation - 1);
        for(int i = 0; i < lastFront.size(); i++){
            averageWaste += lastFront.get(i).averageWaste();
        }
        return averageWaste / lastFront.size();
    }

    public double[] averageUtil(){
        double[] util = new double[2];
        ArrayList<MultiGroupGAChromosome> lastFront = dominantSetList.get(generation - 1);
        for(int i = 0; i < lastFront.size(); i++){
            MultiGroupGAChromosome solution = lastFront.get(i);
            util[0] += solution.averagePmCpuUtil();
            util[1] += solution.averagePmMemUtil();
        }

        util[0] /= lastFront.size();
        util[1] /= lastFront.size();

        return util;
    }

    public double[][] convergenceCurve(){
        double[][] aveFitness = new double[generation][2];
//        for(int i = 0; i < generation; i++){
//            aveFitness[i] = genTime.get(i);
//        }

        return aveFitness;
    }

    //     average VM number
    public double averageNoOfVm(){
        ArrayList<MultiGroupGAChromosome> lastGen = dominantSetList.get(generation - 1);
        int averageVm = 0;
        for(int i = 0; i < lastGen.size(); i++){
            averageVm += lastGen.get(i).getNoOfVm();
        }

        return averageVm / lastGen.size();
    }

    public double averageNoOfPm(){
        ArrayList<MultiGroupGAChromosome> lastGen = dominantSetList.get(generation - 1);
        int averagePm = 0;
        for(int i = 0; i < lastGen.size(); i++){
            averagePm += lastGen.get(i).getNoOfPm();
        }

        return averagePm / lastGen.size();
    }

    // dummy functions
    public double sdNoOfPm(){
        return 0;
    }

    public double sdNoOfVm(){
        return 0;
    }
    public double[] sdUtil(){
        double[] sdUtil = new double[2];
        sdUtil[0] = 0;
        sdUtil[1] = 0;

        return sdUtil;
    }

    public double getMeanTime() {
        return meanTime;
    }

    public double getSdTime() {
        return sdTime;
    }

}
