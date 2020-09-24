package LNS_FF;

import algorithms.StdRandom;
import cloudResourceUnit.VM;

import java.util.ArrayList;
import java.util.LinkedList;

public class LNS {

    private int seed;

    private int numOfServices;
    private int maxGen;
    private int tabuSize;
    private double bestFitness;
    private LNSChromosome bestOverallSolution;
    private LinkedList<LNSChromosome> tabuList;



    private LNSSettings settings;
    private Initialization initialize;
    private LNSEvaluation evaluation;
    private LNSCollector collector;

    private ReplicaPatternDestroy replicaPatternDestroy;
    private AllocationDestroy allocationDestroy;

    private int noImprovementCount;
    private int improvementThreshold;


    public LNS(LNSSettings settings, LNSEvaluation evalution){
        prepare(settings, evalution);
    }

    public void run(){
        collector.collectTime(0);

        // initialize a solution
        LNSChromosome bestOverallSolution = initialize.init();
        bestFitness = evaluation.evaluate(bestOverallSolution);
        LNSChromosome currentBestSolution = bestOverallSolution.clone();
        double currentBestFitness = bestFitness;

        collector.collect(bestOverallSolution);
        // the main loop
        for(int i = 0; i < maxGen; i++){
            LNSChromosome newChromosome;
            // if there are 10 consecutive no improvements,
            // the replica pattern is changed from the best overall solution
            if(noImprovementCount >= improvementThreshold){
                newChromosome = bestOverallSolution.clone();
                replicaPatternDestroy.destroy(newChromosome);
                currentBestSolution = newChromosome;
                noImprovementCount = 0;

            // else, we will continue searching the replica pattern's search space
            } else {
                newChromosome = currentBestSolution.clone();
                allocationDestroy.destroy(newChromosome);
                allocationDestroy.repair(newChromosome);
                allocationDestroy.localSearch(newChromosome);
            }

            double newFitness = evaluation.evaluate(newChromosome);

            // the acceptance is only compared with the current Best fitness
            // and also the tabu list
            if(allocationAcceptance(newChromosome, newFitness, currentBestFitness)) {
                currentBestSolution = newChromosome;
                currentBestFitness = newFitness;
                updateTabuList(newChromosome);
                noImprovementCount = 0;
            } else {
                noImprovementCount++;
            }
            if(currentBestFitness < bestFitness){
                bestFitness = currentBestFitness;
                bestOverallSolution = currentBestSolution;
            }
            collector.collect(bestOverallSolution);
        }
        collector.collectTime(1);
    }

    public void prepare(LNSSettings settings, LNSEvaluation evaluation){

        this.seed = settings.getSeed();
        this.maxGen = settings.getMaxGen();
        this.numOfServices = settings.getNumOfServices();
        this.tabuSize = settings.getTabuSize();
        this.initialize = settings.getInitialization();
        this.improvementThreshold = settings.getImprovementThreshold();
        this.replicaPatternDestroy = settings.getReplicaPatternDestroy();
        this.allocationDestroy = settings.getAllocationDestroy();
        this.collector = settings.getCollector();
        this.evaluation = evaluation;

        StdRandom.setSeed(seed);
        tabuList = new LinkedList<>();
    }

    private boolean allocationAcceptance(LNSChromosome chromosome, double newFitness, double currentBestFitness){
        if(tabuCheck(chromosome) && newFitness < currentBestFitness){
            return true;
        } else {
            return false;
        }
    }

    private void updateTabuList(LNSChromosome chromosome){
        tabuList.add(chromosome);
        if(tabuList.size() > tabuSize){
            tabuList.poll();
        }
    }
    private LNSChromosome allocationDestroy(LNSChromosome chromosome){
        ArrayList<VM> vmList = chromosome.getVmList();
        chromosome.setVmList(vmList);
        return chromosome;
    }


    // If the new replica is NOT on the list, return true
    // Otherwise, return false
    private boolean tabuCheck(LNSChromosome chromosome){
        for(LNSChromosome oldChromosome:tabuList){
            if(oldChromosome.equals(chromosome)) return false;
        }
        return true;
    }

    private ArrayList<VM> generateVMs(){
        ArrayList<VM> vmList = new ArrayList<>();
        return vmList;
    }


}
