package single_chromosome_GA;

import algorithms.Chromosome;
import algorithms.Gene;

import java.util.ArrayList;

public class SingleChromosome extends Chromosome {
    private int[] allocationVector;
    private int numOfContainers;
    private double fitness;
    private int numOfPM;
    private int numOfVM;
    private double aveCpuUtil;
    private double aveMemUtil;
    private double aveWasted;

    public void setAveWasted(double aveWasted) {
        this.aveWasted = aveWasted;
    }

    public double getAveWasted() {
        return aveWasted;
    }

    public double getAveCpuUtil() {
        return aveCpuUtil;
    }

    public void setAveCpuUtil(double aveCpuUtil) {
        this.aveCpuUtil = aveCpuUtil;
    }

    public double getAveMemUtil() {
        return aveMemUtil;
    }

    public void setAveMemUtil(double aveMemUtil) {
        this.aveMemUtil = aveMemUtil;
    }

    public int getNumOfPM() {
        return numOfPM;
    }

    public void setNumOfPM(int numOfPM) {
        this.numOfPM = numOfPM;
    }

    public int getNumOfVM() {
        return numOfVM;
    }

    public void setNumOfVM(int numOfVM) {
        this.numOfVM = numOfVM;
    }

    public SingleChromosome(int numOfContainers){
        this.numOfContainers = numOfContainers;
        allocationVector = new int[numOfContainers * 2];
    }

    public int getNumOfContainers(){
        return numOfContainers;
    }
    public double getFitness(){
        return fitness;
    }

    public void setFitness(double fitness){
        this.fitness = fitness;
    }

    public void setAllocationVector(int[] allocationVector){
        for(int i = 0; i < allocationVector.length; i++){
            this.allocationVector[i] = allocationVector[i];
        }
    }


    public int[] getAllocationVector() {
        return allocationVector;
    }

    @Override
    public void print(){
    }

    @Override
    public Chromosome clone(){
        SingleChromosome chromosome = new SingleChromosome(numOfContainers);
        chromosome.setAllocationVector(allocationVector);
        chromosome.setFitness(fitness);
        chromosome.setAveWasted(aveWasted);
        chromosome.setAveCpuUtil(aveCpuUtil);
        chromosome.setAveMemUtil(aveMemUtil);
        chromosome.setNumOfVM(numOfVM);
        chromosome.setNumOfPM(numOfPM);
        return chromosome;
    }

    @Override
    public boolean equals(Chromosome target){
        return false;
    }


    @Override
    public boolean equals(Object obj){
        return super.equals(obj);
    }

    @Override
    public Gene cut(int cutPoint, int geneIndicator){
        return null;
    }

    @Override
    public int size() {
        return numOfContainers;
    }
}
