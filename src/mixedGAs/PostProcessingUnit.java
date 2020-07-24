package mixedGAs;
import GroupGA.src.GroupGAChromosome;
import PermutationBasedGAForContainerAllocation.DualPermutationChromosome;

import java.util.ArrayList;

public class PostProcessingUnit {
    private ArrayList<GroupGAChromosome> groupGAChromosomeArrayList;
    private ArrayList<DualPermutationChromosome> dualPermutationChromosomeArrayList;
    private int run;
    private int groupGeneration;
    private int dualGeneration;

    public PostProcessingUnit(ArrayList<GroupGAChromosome> groupGAChromosomeArrayList,
                              ArrayList<DualPermutationChromosome> dualPermutationChromosomeArrayList,
                              int groupGeneration, int dualGeneration, int run){
        this.groupGAChromosomeArrayList = groupGAChromosomeArrayList;
        this.dualPermutationChromosomeArrayList = dualPermutationChromosomeArrayList;
        this.groupGeneration = groupGeneration;
        this.dualGeneration = dualGeneration;
        this.run = run;
    }

    public double[] energy(){
        double[] energyConsumption = new double[1];
//        for(int i = 0; i < run; i++){
//            energyConsumption[i] = bestIndividualList.get((generation - 1) * (i + 1)).getEnergy();
//        }
//        return energyConsumption;
        energyConsumption[0] = groupGAChromosomeArrayList.get(groupGeneration - 1).getEnergy();
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

        return groupGAChromosomeArrayList.get(groupGeneration - 1).getEnergy();
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
        return groupGAChromosomeArrayList.get(groupGeneration - 1).averageWaste();
    }


    // The average PM CPU and memory utilization among N runs
    public double[] averageUtil(){
        double[] util = new double[2];
        util[0] = groupGAChromosomeArrayList.get(groupGeneration - 1).averagePmCpuUtil();
        util[1] = groupGAChromosomeArrayList.get(groupGeneration - 1).averagePmMemUtil();
//        double[] cpuUtil = new double[run];
//        double[] memUtil = new double[run];

//        for(int i = 0; i < run; i++){
//            cpuUtil[i] = bestIndividualList.get((generation - 1) * (i + 1)).averagePmCpuUtil();
//            memUtil[i] = bestIndividualList.get((generation - 1) * (i + 1)).averagePmMemUtil();
//        }
//
//        double totalCpuUtil = 0;
//        double totalMemUtil = 0;
//        for(int i = 0; i < run; i++){
//            totalCpuUtil += cpuUtil[i];
//            totalMemUtil += memUtil[i];
//        }
//
//        util[0] = totalCpuUtil / run;
//        util[1] = totalMemUtil / run;
        return util;
    }

    // The SD of PM CPU and memory utilization among N runs
    public double[] sdUtil(){
        double[] sdUtil = new double[2];
        sdUtil[0] = 0;
        sdUtil[1] = 0;
//
//        double[] aveUtil = averageUtil();
//        double aveCpuUtil = aveUtil[0];
//        double aveMemUtil = aveUtil[1];
//
//
//        double[] cpuUtil = new double[run];
//        double[] memUtil = new double[run];
//
//        double sumCpuUtil = 0.0;
//        double sumMemUtil = 0.0;
//
//        for(int i = 0; i < run; i++){
//            cpuUtil[i] = bestIndividualList.get((generation - 1) * (i + 1)).averagePmCpuUtil();
//            memUtil[i] = bestIndividualList.get((generation - 1) * (i + 1)).averagePmMemUtil();
//        }
//
//        for(int i = 0; i < run; i++){
//            sumCpuUtil += cpuUtil[i] * cpuUtil[i];
//            sumMemUtil += memUtil[i] * memUtil[i];
//        }
//
//        sdUtil[0] = Math.sqrt((sumCpuUtil / run) - aveCpuUtil * aveCpuUtil);
//        sdUtil[1] = Math.sqrt((sumMemUtil / run) - aveMemUtil * aveMemUtil);
//
        return sdUtil;
    }



    // convergence curve
    // calculate the average fitness value of each generation among all N runs
    public double[] convergenceCurve(){
        int totalGen = dualGeneration + groupGeneration;
        double[] aveFitness = new double[totalGen];
        for(int i = 0; i < dualGeneration; i++){
            aveFitness[i] = dualPermutationChromosomeArrayList.get(i).getFitness();

//            double[] fitness = new double[run];
//            for(int j = 0; j < run; j++){
//                fitness[j] = bestIndividualList.get(j * generation + i).getFitness();
//            }
//            fitnessList.add(fitness);
        }
        for(int i = dualGeneration; i < totalGen; i++){
            aveFitness[i] = groupGAChromosomeArrayList.get(i - dualGeneration).getFitness();
        }

//        for(int i = 0; i < generation; i++){
//            aveFitness[i] = sumArray(fitnessList.get(i)) / run;
//        }
        return aveFitness;
    }

//     average VM number
    public double averageNoOfVm(){
//        int[] noOfVm = new int[run];
//        for(int i = 0; i < run; i++){
////            bestIndividualList.get((generation - 1) * (i + 1)).print();
//            noOfVm[i] = bestIndividualList.get((generation - 1) * (i + 1)).getNoOfUsedVm();
//        }
//
//        int sumNumOfVm = 0;
//        for(int i = 0; i < run; i++){
//           sumNumOfVm += noOfVm[i];
//        }
//
//        return sumNumOfVm / (run / 1.0);
        return groupGAChromosomeArrayList.get(groupGeneration - 1).getNoOfUsedVm();
    }


    // SD VM number
    public double sdNoOfVm(){
//        double aveNoOfVm = averageNoOfVm();
//        double sd = 0.0;
//        int[] noOfVm = new int[run];
//        double sdSum = 0.0;
//        for(int i = 0; i < run; i++){
//            noOfVm[i] = bestIndividualList.get((generation - 1) * (i + 1)).getNoOfUsedVm();
//        }
//
//        for(int i = 0; i < run; i++){
//            sdSum += noOfVm[i] * noOfVm[i];
//        }
//
//        sd = Math.sqrt(sdSum / run - aveNoOfVm * aveNoOfVm);
        return 0;
    }

    // average PM number
    public double averageNoOfPm(){
//        int[] noOfPm = new int[run];
//        for(int i = 0; i < run; i++){
//            noOfPm[i] = bestIndividualList.get((generation - 1) * (i + 1)).getNoOfPm();
//        }
//
//        int sumNumOfPm = 0;
//        for(int i = 0; i < run; i++){
//            sumNumOfPm += noOfPm[i];
//        }
//
//        return sumNumOfPm / (run / 1.0);
        return groupGAChromosomeArrayList.get(groupGeneration - 1).getNoOfPm();
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

//    private double sumArray(double[] fitness){
//        double sum = 0;
//        for(int i = 0; i < fitness.length; i++){
//            sum += fitness[i];
//        }
//        return sum;
//    }



}
