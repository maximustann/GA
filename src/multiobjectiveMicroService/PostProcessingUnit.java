package multiobjectiveMicroService;

import java.util.ArrayList;

public class PostProcessingUnit {
    private ArrayList<ArrayList<MultiGroupGAChromosome>> dominantSetList;
    private ArrayList<double[]> genTime;
    private int run;
    private int generation;

    public PostProcessingUnit(ArrayList<ArrayList<MultiGroupGAChromosome>> dominantSetList,
                              ArrayList<double[]> genTime,
                              int generation, int run){
        this.dominantSetList = dominantSetList;
        this.genTime = genTime;
        this.generation = generation;
        this.run = run;
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
//            energyConsumption[i] = bestIndividualList.get((generation - 1) * (i + 1)).getEnergy();
        }
        energyConsumption[0] /= lastSet.size();

        return energyConsumption;
    }

//    // The average PM energy consumption among N runs

    public double averageEnergy(){
//        double[] energy = new double[run];
//        for(int i = 0; i < run; i++){
//            energy[i] = bestIndividualList.get(generation - 1).getEnergy();
//        }
////
////        double sumEnergy = 0;
////        for(int i = 0; i < run; i++){
////            sumEnergy += energy[i];
////        }

        return energy()[0];
    }
    // The SD PM energy consumption among N runs
    public double sdEnergy(){
//        double aveEnergy = averageEnergy();
//        double sd = 0.0;
//        double[] energy = new double[run];
//        double sdSum = 0.0;
//        for(int i = 0; i < run; i++){
//            energy[i] = bestIndividualList.get((generation - 1) * (i + 1)).getFitness();
//        }
//
//        for(int i = 0; i < run; i++){
//            sdSum += energy[i] * energy[i];
//        }
//
//        sd = Math.sqrt(sdSum / run - aveEnergy * aveEnergy);

        return 0;
    }

    public double waste(){
        double averageWaste = 0;
        ArrayList<MultiGroupGAChromosome> lastFront = dominantSetList.get(generation - 1);
        for(int i = 0; i < lastFront.size(); i++){
            averageWaste += lastFront.get(i).averageWaste();
        }
        return averageWaste / lastFront.size();
    }


    // The average PM CPU and memory utilization among N generations
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

    // The SD of PM CPU and memory utilization among N runs
    public double[] sdUtil(){
        double[] sdUtil = new double[2];
        sdUtil[0] = 0;
        sdUtil[1] = 0;

        return sdUtil;
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

//     average VM number
    public double averageNoOfVm(){
        ArrayList<MultiGroupGAChromosome> lastGen = dominantSetList.get(generation - 1);
        int averageVm = 0;
        for(int i = 0; i < lastGen.size(); i++){
            averageVm += lastGen.get(i).getNoOfVm();
        }

        return averageVm / lastGen.size();
    }


    // SD VM number
    public double sdNoOfVm(){

        return 0;
    }

    // average PM number
    public double averageNoOfPm(){
        ArrayList<MultiGroupGAChromosome> lastGen = dominantSetList.get(generation - 1);
        int averagePm = 0;
        for(int i = 0; i < lastGen.size(); i++){
            averagePm += lastGen.get(i).getNoOfPm();
        }

        return averagePm / lastGen.size();
    }


    // SD PM number
    public double sdNoOfPm(){
//        double aveNoOfPm = averageNoOfPm();
//        double sd = 0.0;
//        int[] noOfPm = new int[run];
//        double sdSum = 0.0;
//        for(int i = 0; i < run; i++){
//            noOfPm[i] = bestIndividualList.get((generation - 1) * (i + 1)).getNoOfPm();
//        }
//
//        for(int i = 0; i < run; i++){
//            sdSum += noOfPm[i] * noOfPm[i];
//        }
//
//        sd = Math.sqrt(sdSum / run - aveNoOfPm * aveNoOfPm);
        return 0;
    }



}
