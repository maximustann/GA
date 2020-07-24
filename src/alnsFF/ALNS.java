package alnsFF;

import algorithms.StdRandom;
import cloudResourceUnit.VM;

import java.util.ArrayList;
import java.util.LinkedList;

public class ALNS {

    private int seed;

    private int numOfServices;
    private int maxGen;
    private int tabuSize;
    private double bestFitness;
    private ALNSChromosome bestSolution;
    private LinkedList<ALNSChromosome> tabuList;



    private ALNSSettings settings;
    private Initialization initialize;
    private ALNSEvaluation evaluation;
    private ALNSCollector collector;

    private ReplicaPatternDestroy replicaPatternDestroy;
    private AllocationDestroy allocationDestroy;

    private int noImprovementCount;
    private int improvementThreshold;


    public ALNS(ALNSSettings settings, ALNSEvaluation evalution){
        prepare(settings, evalution);
    }

    public void run(){
        collector.collectTime(0);

        // initialize a solution
        ALNSChromosome bestSolution = initialize.init();
        bestFitness = evaluation.evaluate(bestSolution);
        collector.collect(bestSolution);
        // the main loop
        for(int i = 0; i < maxGen; i++){

            ALNSChromosome newChromosome = bestSolution.clone();
            if(noImprovementCount >= improvementThreshold){
                replicaPatternDestroy.destroy(newChromosome.getReplicas());
            }
            allocationDestroy.destroy(newChromosome);
            allocationDestroy.repair(newChromosome);
//            allocationDestroy.localSearch(newChromosome);

            double newFitness = evaluation.evaluate(newChromosome);
            if(allocationAcceptance(newChromosome, newFitness)) {
                bestSolution = newChromosome;
                bestFitness = newFitness;
                updateTabuList(newChromosome);
            } else {
                noImprovementCount++;
            }
            collector.collect(bestSolution);
        }
        collector.collectTime(1);
    }

    public void prepare(ALNSSettings settings, ALNSEvaluation evaluation){

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

    private boolean allocationAcceptance(ALNSChromosome chromosome, double newFitness){
        if(tabuCheck(chromosome) && newFitness < bestFitness){
            return true;
        } else {
            return false;
        }
    }


    private void updateTabuList(ALNSChromosome chromosome){
        tabuList.add(chromosome);
        if(tabuList.size() > tabuSize){
            tabuList.poll();
        }
    }
    private ALNSChromosome allocationDestroy(ALNSChromosome chromosome){
        ArrayList<VM> vmList = chromosome.getVmList();
        chromosome.setVmList(vmList);
        return chromosome;
    }


    // If the new replica is NOT on the list, return true
    // Otherwise, return false
    private boolean tabuCheck(ALNSChromosome chromosome){
        for(ALNSChromosome oldChromosome:tabuList){
            if(oldChromosome.equals(chromosome)) return false;
        }
        return true;
    }

    private ArrayList<VM> generateVMs(){
        ArrayList<VM> vmList = new ArrayList<>();
        return vmList;
    }


}
