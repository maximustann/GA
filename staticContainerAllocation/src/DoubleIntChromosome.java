import algorithms.Chromosome;
import algorithms.Gene;

import java.util.HashMap;
import java.util.HashSet;

import static constants.Parts.*;


public class DoubleIntChromosome extends Chromosome{
    public int[] containerAllocation;
    public int[] vmAllocation;


    /**
     * Constructor 1
     * @param numOfContainer The number of container
     * @param numOfVm The theoretical maximum number of each type of VM
     */
    public DoubleIntChromosome(int numOfContainer, int numOfVm){
        containerAllocation = new int[numOfContainer];
        vmAllocation = new int[numOfVm];
    }

    /**
     * Constructor 2
     *
     * We need two genes to construct a chromosome.
     * In this case, gene is tight coupling with chromosome. You will have to manually
     * define a chromosome and a gene.
     *
     * It constructs a new chromosome with two gene pieces.
     *
     * @param firstPart
     * @param secondPart
     */
    public DoubleIntChromosome(DoubleIntGene firstPart, DoubleIntGene secondPart){
        containerAllocation = new int[firstPart.getNumOfContainers() + secondPart.getNumOfContainers()];
        for(int i = 0; i < firstPart.getNumOfContainers(); i++) {
            containerAllocation[i] = firstPart.containerAllocation[i];
        }
        for(int i = firstPart.getNumOfContainers(), j = 0; j < secondPart.getNumOfContainers(); j++, i++) {
            containerAllocation[i] = secondPart.containerAllocation[j];
        }


        vmAllocation = new int[firstPart.getNumOfVms() + secondPart.getNumOfVms()];
        for(int i = 0; i < firstPart.getNumOfVms(); i++) {
            vmAllocation[i] = firstPart.vmAllocation[i];
        }
        for(int i = firstPart.getNumOfVms(), j = 0; j < secondPart.getNumOfVms(); j++, i++) {
            vmAllocation[i] = secondPart.vmAllocation[j];
        }
    }



    /**
     *
     * @param cutPoint indicates where to cut
     * @param geneIndicator denotes which part of chromosome should it returns, 0 denotes
     * @return
     */
    @Override
    public Gene cut(int cutPoint, int geneIndicator) {
        // actually doing nothing
        return null;
    }

    public int getNumOfContainers(){
        return containerAllocation.length;
    }

    public int getNumOfVms(){
        return vmAllocation.length;
    }

    @Override
    public int size(){
        // doing nothing
        return 0;
    }

    public DoubleIntGene cut(int cutPointOnContainer, int cutPointOnVm, int geneIndicator){
        DoubleIntGene part;
        if(geneIndicator == FIRST.getNum()){
            part = new DoubleIntGene(cutPointOnContainer + 1, cutPointOnVm + 1);
            for(int i = 0; i < cutPointOnContainer + 1; i++){
                part.containerAllocation[i] = containerAllocation[i];
            }
            for(int i = 0; i < cutPointOnVm + 1; i++){
                part.vmAllocation[i] = vmAllocation[i];
            }

        }else {
            part = new DoubleIntGene(
                getNumOfContainers() - (cutPointOnContainer + 1),
                getNumOfVms() - (cutPointOnVm + 1)
            );

            // Copy two pieces of genes
            for(int i = cutPointOnContainer + 1, j = 0; i < getNumOfContainers(); i++, j++){
                part.containerAllocation[j] = containerAllocation[i];
            }
            for(int i = cutPointOnVm + 1, j = 0; i < getNumOfVms(); i++, j++){
                part.vmAllocation[j] = vmAllocation[i];
            }

        }

        return part;
    }

    @Override
    public DoubleIntChromosome clone(){
        DoubleIntChromosome copy = new DoubleIntChromosome(getNumOfContainers(), getNumOfVms());
        for(int i = 0; i < getNumOfContainers(); i++){
            copy.containerAllocation[i] = containerAllocation[i];
        }
        for(int i = 0; i < getNumOfVms(); i++){
            copy.vmAllocation[i] = vmAllocation[i];
        }
        return copy;
    }

    @Override
    public void print(){
        for(int i = 0; i < getNumOfContainers(); i++){
            System.out.println(containerAllocation[i] + " ");
        }
        System.out.println();
        for(int i = 0; i < getNumOfVms(); i++){
            System.out.println(vmAllocation[i] + " ");
        }
        System.out.println("-------------------------------------------------");
    }

    @Override
    public boolean equals(Chromosome target){
        return equals((DoubleIntChromosome) target);
    }


    // the actual comparison between the current one and the target
    private boolean equals(DoubleIntChromosome target){
        int numOfContainers = getNumOfContainers();
        int numOfVms = getNumOfVms();
        for(int i = 0; i < numOfContainers; i++){
            if(containerAllocation[i] != target.containerAllocation[i]){
                return false;
            }
        }
        for(int i = 0; i < numOfVms; i++){
            if(vmAllocation[i] != target.vmAllocation[i]){
                return false;
            }
        }
        return true;
    }

    // return the number of active VMs
    public int getNumOfActiveVm(){
        int num = 0;
        int length = vmAllocation.length;
        for(int i = 0; i < length; ++i){
            if(vmAllocation[i] != 0){
                ++num;
            }
        }
        return num;
    }

    // return the number of active PMs
    public int getNumOfActivePM(){
        HashSet<Integer> pms = new HashSet();
        for(int i = 0; i < vmAllocation.length; i++){
            // If the VM exists, we add the index into the set
            if(vmAllocation[i] != 0){
                pms.add(vmAllocation[i]);
            }
        }

        // after examining the allocation of all VMs, we have a set of PMs.
        // we simply return the size of the set
        return pms.size();
    }

}
