package BilevelContainerAllocation;

import dataCollector.DataCollector;
import java.util.ArrayList;
import algorithms.*;

import javax.xml.transform.Result;

public class ResultCollector extends DataCollector {
    private ArrayList<Double> resultFitness;
    private ArrayList<Chromosome[]> resultChromosome;


    // Constructor, initialize fields
    public ResultCollector(){
        super();
        resultFitness = new ArrayList<Double>();
        resultChromosome = new ArrayList<Chromosome[]>();
    }
    @Override
    public void collect(Object result) {
        resultFitness.add((Double) result);
    }

    @Override
    public void collectSet(Object data) {
        resultChromosome.add((Chromosome[]) data);
    }

    public void printResult(){
        int generation = 0;
        for(Double fitness: resultFitness){
            System.out.println("fitness value: " + fitness + " gen: " + generation);
        }
    }
}
