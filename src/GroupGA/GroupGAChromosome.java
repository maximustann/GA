package GroupGA;

import algorithms.Chromosome;
import algorithms.Gene;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class GroupGAChromosome extends Chromosome {
    private ArrayList<PM> pmList;
    private int numOfContainers;
    private double fitness;


    // Constructor
    public GroupGAChromosome(int numOfContainers){
        this.numOfContainers = numOfContainers;
    }

    public double getFitness() {
        return fitness;
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    public ArrayList<PM> getPmList() {
        return pmList;
    }

    public double getEnergy(){
        double energy = 0;
        for(PM pm:pmList){
            energy += pm.calEnergy();
        }
        return energy;
    }

    public double averagePmCpuUtil(){
        double totalCpuUtil = 0;
        for(PM pm:pmList){
            pm.updateUtilization();
            totalCpuUtil += pm.getCpuUtil();
        }
        return totalCpuUtil / pmList.size();
    }

    public double averageWaste(){
        double totalWasted = 0;
        for(PM pm:pmList){
            totalWasted += pm.getWastedResource();
        }
        return totalWasted / pmList.size();
    }

    public double averagePmMemUtil(){
        double totalMemUtil = 0;
        for(PM pm:pmList){
            pm.updateUtilization();
            totalMemUtil += pm.getMemUtil();
        }
        return totalMemUtil / pmList.size();
    }

    public int getNoOfUsedVm(){
        int totalVmNo = 0;
        for(PM pm:pmList){
            totalVmNo += pm.getVmList().size();
        }
        return totalVmNo;
    }

    public int getNoOfPm(){
        return pmList.size();
    }

    public int getNumOfContainers(){
        return numOfContainers;
    }


    public void sortPms(){
        // update wasted resources before sorting
        for(PM pm:pmList){
            pm.updateWastedResources();
        }
        Collections.sort(pmList);
    }


    // Descending order
    public void sortPmsVmOverhead(){
        Collections.sort(pmList, new Comparator<PM>() {
            @Override
            public int compare(PM o1, PM o2) {
                if(o1.getVmList().size() > o2.getVmList().size()){
                    return -1;
                } else if(o1.getVmList().size() == o2.getVmList().size()){
                    return 0;
                } else{
                    return 1;
                }
            }
        });
    }

    // Descending order with PM Cpu
    public void sortPmsCpuUtil(){
        Collections.sort(pmList, new Comparator<PM>() {
            @Override
            public int compare(PM o1, PM o2) {
                if(o1.getCpuUtil() > o2.getCpuUtil()){
                    return -1;
                } else if(o1.getCpuUtil() == o2.getCpuUtil()){
                    return 0;
                } else {
                    return 1;
                }
            }
        });
    }

    // Descending order with PM Mem:
    public void sortPmsMemUtil(){
        Collections.sort(pmList, new Comparator<PM>() {
            @Override
            public int compare(PM o1, PM o2) {
                if(o1.getMemUtil() > o2.getMemUtil()){
                    return -1;
                } else if(o1.getCpuUtil() == o2.getCpuUtil()){
                    return 0;
                } else {
                    return 1;
                }
            }
        });
    }




    public void setPmList(ArrayList<PM> pmList) {
        this.pmList = pmList;
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public int size() {
        return pmList.size();
    }

    @Override
    public Gene cut(int cutPoint, int geneIndicator) {
        return null;
    }

    @Override
    public void print() {
        System.out.println("PM num = " + size());
        System.out.println("VM num = " + getNoOfUsedVm());
        System.out.println("PM ave Cpu Util = " + averagePmCpuUtil());
        System.out.println("PM ave Mem Util = " + averagePmMemUtil());
//        for(PM pm:pmList){
//            System.out.println("pm waste = " + pm.updateWastedResources());
//        }
    }

    @Override
    public Chromosome clone() {
        GroupGAChromosome chromosome = new GroupGAChromosome(numOfContainers);
        ArrayList<PM> newPmList = new ArrayList<>();
        for(int i = 0; i < pmList.size(); i++){
            newPmList.add(pmList.get(i).clone());
        }
        chromosome.setPmList(newPmList);
        chromosome.setFitness(fitness);
        return chromosome;
    }

    @Override
    public boolean equals(Chromosome target) {
        return false;
    }
}
