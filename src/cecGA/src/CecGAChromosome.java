package cecGA.src;

import algorithms.Gene;
import commonRepresentation.IntGene;
import commonRepresentation.IntValueChromosome;

import java.util.ArrayList;
import java.util.HashMap;

public class CecGAChromosome extends IntValueChromosome{
    // The following fields will be filled after the evaluation stage.
    // the pmList includes PMs,
    // each PM includes a list of VMs,
    // each VM is represented as int[] with two attributes,
    // [0] VM index, [1] VM type
    private ArrayList<ArrayList<int[]>> pmList;


    // the pmStatusList includes the status of each PM,
    // each PM status is represented as double[] with three attributes
    // [0] CPU util, [1] MEM util, [2] energy
    private ArrayList<double[]> pmStatusList;


    // vmList includes VMs
    // each VM has an array of containers,
    // each entry of the array is the index of a container
    private ArrayList<int[]> vmList;

    // vmStatusList includes the statuses of VMs
    // each VM status is represented as double[] with two attributes
    //[0] CPU util, [1] Mem util
    private ArrayList<double[]> vmStatusList;


    // Fitness value
    private double fitness;

    public double getWastedResources() {
        return wastedResources;
    }

    public void setWastedResources(double wastedResources) {
        this.wastedResources = wastedResources;
    }

    // wastedResources
    private double wastedResources;

    public CecGAChromosome(Gene firstPart, Gene secondPart){
        super(firstPart, secondPart);
    }

    public CecGAChromosome(int size){
        super(size);
    }



    public void setVmList(ArrayList<int[]> vmList){
        this.vmList = vmList;
    }
    public void setVmStatusList(ArrayList<double[]> vmStatusList){
        this.vmStatusList = vmStatusList;
    }
    public void setPmList(ArrayList<ArrayList<int[]>> pmList){
        this.pmList = pmList;
    }
    public void setPmStatusList(ArrayList<double[]> pmStatusList){
        this.pmStatusList = pmStatusList;
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    public double getFitness() {
        return fitness;
    }


    public ArrayList<double[]> getPmStatusList() {
        return pmStatusList;
    }

    public int getNoOfUsedVm(){
        if(vmList == null || vmList.isEmpty()){
            return 0;
        } else {
            return vmList.size();
        }
    }

    public void printPmList(){
        for(int i = 0; i < pmList.size(); i++){
            ArrayList<int[]> pm = pmList.get(i);
            System.out.print("PM " + i + ": ");
            for(int j = 0; j < pm.size(); j++){
                System.out.print("index = " + pm.get(j)[0] + " : type = " + pm.get(j)[1] + ", ");
            }
            System.out.println();
        }
    }

    public int getNumOfPm(){
        if(pmList == null || pmList.isEmpty()){
            return 0;
        } else {
            return pmList.size();
        }
    }

    public double averagePmCpuUtil(double pmCpu){
        double totalCpuUtil = 0;
        for(double[] pmStatus:pmStatusList){
            totalCpuUtil += (pmStatus[0] / pmCpu);
        }

        return totalCpuUtil / pmStatusList.size();
    }

    public double averagePmMemUtil(double pmMem){
        double totalMemUtil = 0;
        for(double[] pmStatus:pmStatusList){
            totalMemUtil += (pmStatus[1] / pmMem);
        }

        return totalMemUtil / pmStatusList.size();
    }




    @Override
    /**
     * Print in one line
     */
    public void print() {
        for(int i = 0; i < size(); i++){
            System.out.print(individual[i] + " ");
            if(((i + 1) % 2 == 0) && ((i + 1) != 0)) System.out.print(" | ");
        }
        System.out.println();
        System.out.println("numOfPm = " + getNumOfPm());
        System.out.println("ActualUsedVm = " + getNoOfUsedVm());
        System.out.println("averagePmCpuUtil = " + averagePmCpuUtil(13200));
        System.out.println("averagePmMemUtil = " + averagePmMemUtil(16000));
//        printPmList();
    }

    public CecGAChromosome clone(){
        CecGAChromosome copy = new CecGAChromosome(size());
        copy.setPmList((ArrayList<ArrayList<int[]>>) pmList.clone());
        copy.setPmStatusList((ArrayList<double[]>) pmStatusList.clone());
        copy.setFitness(fitness);
        copy.setVmList((ArrayList<int[]>) vmList.clone());
        copy.setVmStatusList((ArrayList<double[]>) vmStatusList.clone());
        copy.setWastedResources(wastedResources);
        for(int i = 0; i < size(); i++){
            copy.individual[i] = individual[i];
        }
        return copy;
    }

    public IntGene cut(int cutPoint, int geneIndicator){
        IntGene part;
        if(geneIndicator == 0){
            part = new IntGene(cutPoint + 1);
            for(int i = 0; i < cutPoint + 1; i++){
                part.gene[i] = individual[i];
            }
        } else{
            part = new IntGene(size() - (cutPoint + 1));
            for(int i = cutPoint + 1, j = 0; i < size(); i++, j++){
                part.gene[j] = individual[i];
            }
        }
        return part;
    }


}
