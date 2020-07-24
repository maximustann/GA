package cecGA.src;

import java.util.ArrayList;

public class PostProcessingUnit {
    private ArrayList<CecGAChromosome> bestIndividualList;
    private ArrayList<double[]> genTime;
    private int run;
    private int generation;
    private double pmCpu;
    private double pmMem;

    public PostProcessingUnit(ArrayList<CecGAChromosome> bestIndividualList,
                              ArrayList<double[]> genTime,
                              int generation, int run,
                              double pmCpu, double pmMem){
        this.bestIndividualList = bestIndividualList;
        this.genTime = genTime;
        this.generation = generation;
        this.run = run;
        this.pmCpu = pmCpu;
        this.pmMem = pmMem;
    }

    public double[] energy(){
        double[] energyConsumption = new double[1];
//        for(int i = 0; i < run; i++){
//            energyConsumption[i] = bestIndividualList.get((generation - 1) * (i + 1)).getFitness();
//        }
        energyConsumption[0] = bestIndividualList.get(generation - 1).getFitness();
        return energyConsumption;
    }

    // The average PM energy consumption among N runs
    public double averageEnergy(){
//        double[] energy = new double[run];
//        for(int i = 0; i < run; i++){
//            energy[i] = bestIndividualList.get((generation - 1) * (i + 1)).getFitness();
//        }
//
//        double sumEnergy = 0;
//        for(int i = 0; i < run; i++){
//            sumEnergy += energy[i];
//        }
//
//        return sumEnergy / run;
        return bestIndividualList.get(generation - 1).getFitness();
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
//
//        return sd;
        return 0;
    }

    public double waste(){
        return bestIndividualList.get(generation - 1).getWastedResources();
    }



    // The average PM CPU and memory utilization among N runs
    public double[] averageUtil(){
        double[] util = new double[2];
        util[0] = bestIndividualList.get(generation - 1).averagePmCpuUtil(13200);
        util[1] = bestIndividualList.get(generation - 1).averagePmMemUtil(16000);

//        double[] cpuUtil = new double[run];
//        double[] memUtil = new double[run];
//
//        for(int i = 0; i < run; i++){
//            cpuUtil[i] = bestIndividualList.get((generation - 1) * (i + 1)).averagePmCpuUtil(pmCpu);
//            memUtil[i] = bestIndividualList.get((generation - 1) * (i + 1)).averagePmMemUtil(pmMem);
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
//            cpuUtil[i] = bestIndividualList.get((generation - 1) * (i + 1)).averagePmCpuUtil(pmCpu);
//            memUtil[i] = bestIndividualList.get((generation - 1) * (i + 1)).averagePmMemUtil(pmMem);
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
    public double[][] convergenceCurve(){
        double[][] aveFitness = new double[generation][2];
        for(int i = 0; i < generation; i++){
            aveFitness[i] = genTime.get(i);
        }


        return aveFitness;


    }

    // average VM number
    public double averageNoOfVm(){
//        int[] noOfVm = new int[run];
//        for(int i = 0; i < run; i++){
//            noOfVm[i] = bestIndividualList.get((generation - 1) * (i + 1)).getNoOfUsedVm();
//        }
//
//        int sumNumOfVm = 0;
//        for(int i = 0; i < run; i++){
//           sumNumOfVm += noOfVm[i];
//        }
//
//        return sumNumOfVm / (run / 1.0);
        return bestIndividualList.get(generation - 1).getNoOfUsedVm();
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
//            noOfPm[i] = bestIndividualList.get((generation - 1) * (i + 1)).getNumOfPm();
//        }
//
//        int sumNumOfPm = 0;
//        for(int i = 0; i < run; i++){
//            sumNumOfPm += noOfPm[i];
//        }
//
//        return sumNumOfPm / (run / 1.0);
        return bestIndividualList.get(generation - 1).getNumOfPm();
    }


    // SD PM number
    public double sdNoOfPm(){
//        double aveNoOfPm = averageNoOfPm();
//        double sd = 0.0;
//        int[] noOfPm = new int[run];
//        double sdSum = 0.0;
//        for(int i = 0; i < run; i++){
//            noOfPm[i] = bestIndividualList.get((generation - 1) * (i + 1)).getNumOfPm();
//        }
//
//        for(int i = 0; i < run; i++){
//            sdSum += noOfPm[i] * noOfPm[i];
//        }
//
//        sd = Math.sqrt(sdSum / run - aveNoOfPm * aveNoOfPm);
//        return sd;
        return 0;
    }

    private double sumArray(double[] fitness){
        double sum = 0;
        for(int i = 0; i < fitness.length; i++){
            sum += fitness[i];
        }
        return sum;
    }



}
