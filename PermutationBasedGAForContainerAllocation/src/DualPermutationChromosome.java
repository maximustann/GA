import algorithms.Chromosome;
import algorithms.Gene;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class DualPermutationChromosome extends Chromosome{
    public int[] containerPermutation;
    public int[] vmTypes;
    private int numOfContainer;
    private int numOfVm;

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

    // fitness value
    private double fitness;

    /**
     * Constructor 1
     */
    public DualPermutationChromosome(int numOfContainer, int numOfVm){
        this.numOfContainer = numOfContainer;
        this.numOfVm = numOfVm;

        containerPermutation = new int[numOfContainer];
        vmTypes = new int[numOfVm];
    }

    /**
     * Constructor 2
     * Constructor is used for crossover to generate children,
     * In this case, it is designed for order 1 type of crossover
     */
    public DualPermutationChromosome(DualPermutationGene firstPart,
                                     DualPermutationGene secondPart,
                                     int startPointOnContainer,
                                     int endPointOnContainer,
                                     int cutPointOnVm,
                                     int numOfContainer,
                                     int numOfVm){
        this.numOfContainer = numOfContainer;
        this.numOfVm = numOfVm;

        containerPermutation = generateContainers(firstPart, secondPart,
                                                    startPointOnContainer,
                                                    endPointOnContainer);

        vmTypes = generateVmTypes(firstPart, secondPart, cutPointOnVm, numOfVm);

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

    public int getNoOfUsedVm(){
        if(vmList == null || vmList.isEmpty()){
            return 0;
        } else {
            return vmList.size();
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
    public DualPermutationGene cut(int cutPoint, int geneIndicator) {
        return null;
    }

    public int getNumOfContainer(){
        return numOfContainer;
    }

    public int getNumOfVm(){
        return numOfVm;
    }

    @Override
    public int size(){
        return 0;
    }


    // In this representation, we must use the whole chromosome as the a gene.
    // We cannot separate them into smaller parts.
    public DualPermutationGene cut(int startPointOnContainer, int endPointOnContainer, int cutPointOnVm){
        DualPermutationGene part = new DualPermutationGene(numOfContainer, numOfVm);

        // copy the current values into the gene
        System.arraycopy(containerPermutation, 0, part.containerPermutation, 0, numOfContainer);
        System.arraycopy(vmTypes, 0, part.vmTypes, 0, numOfVm);
        return part;
    }

    @Override
    public DualPermutationChromosome clone(){
        DualPermutationChromosome ind = new DualPermutationChromosome(numOfContainer, numOfVm);
        System.arraycopy(containerPermutation, 0, ind.containerPermutation, 0, numOfContainer);
        System.arraycopy(vmTypes, 0, ind.vmTypes, 0, numOfVm);
        return ind;
    }

    @Override
    public void print(){
        System.out.println("container Permutation: ");
        for(int i = 0; i < numOfContainer; ++i){
            System.out.print(containerPermutation[i] + " ");
        }
        System.out.println();
        System.out.println("vm Types: ");
        for(int i = 0; i < numOfVm; ++i){
            System.out.print(vmTypes[i] + " ");
        }
        System.out.println();
        System.out.println("numOfPm = " + getNumOfPm());
        System.out.println("ActualUsedVm = " + getNoOfUsedVm());
        System.out.println("averagePmCpuUtil = " + averagePmCpuUtil(3300));
        System.out.println("averagePmMemUtil = " + averagePmMemUtil(4000));
    }

    @Override
    public boolean equals(Chromosome o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DualPermutationChromosome that = (DualPermutationChromosome) o;
        return numOfContainer == that.numOfContainer &&
                numOfVm == that.numOfVm &&
                Arrays.equals(containerPermutation, that.containerPermutation) &&
                Arrays.equals(vmTypes, that.vmTypes);
    }

    @Override
    public int hashCode() {

        int result = Objects.hash(numOfContainer, numOfVm);
        result = 31 * result + Arrays.hashCode(containerPermutation);
        result = 31 * result + Arrays.hashCode(vmTypes);
        return result;
    }


    private int[] generateVmTypes(DualPermutationGene firstPart,
                                  DualPermutationGene secondPart,
                                  int cutPointOnVm, int numOfVm){
        vmTypes = new int[numOfVm];
        for(int i = 0; i < cutPointOnVm; i++){
            vmTypes[i] = firstPart.vmTypes[i];
        }
        for(int i = cutPointOnVm; i < firstPart.getNumOfVms(); i++){
            vmTypes[i] = secondPart.vmTypes[i];
        }
        return vmTypes;
    }

    private int[] generateContainers(DualPermutationGene firstPart,
                                     DualPermutationGene secondPart,
                                     int startPoint, int endPoint){

        containerPermutation = new int[numOfContainer];
        // Condition One, startPoint == 0 && endPoint == numOfContainer, the whole chromosome is copied.
        if(startPoint == 0 && endPoint == numOfContainer){
            System.arraycopy(firstPart.containerPermutation, 0, containerPermutation, 0, numOfContainer);
            return containerPermutation;
        }


        // Condition Two -- cut from the beginning
        if(startPoint == 0 && endPoint != numOfContainer){
            int countIndex = endPoint;
            int[] tail = new int[numOfContainer - endPoint];
            int[] head = new int[endPoint];
            System.arraycopy(secondPart.containerPermutation, endPoint, tail,
                            0, numOfContainer - endPoint);
            System.arraycopy(secondPart.containerPermutation, 0, head,
                            0, endPoint);

            // Copy to child
            System.arraycopy(firstPart.containerPermutation, startPoint,
                    containerPermutation, startPoint, endPoint - startPoint);

            // check tail
            for (int i = 0; i < tail.length; ++i) {
                if (!valueExists(tail[i], firstPart.containerPermutation, startPoint, endPoint)) {
                    containerPermutation[countIndex] = tail[i];
                    countIndex++;
                }
            }

            // check head
            for(int i = 0; countIndex < numOfContainer; ++i){
                if (!valueExists(head[i], firstPart.containerPermutation, startPoint, endPoint)) {
                    containerPermutation[countIndex] = head[i];
                    countIndex++;
                }
            }

            return containerPermutation;
        }

        // Condition Two -- cut from the middle to the end
        if(startPoint != 0 && endPoint == numOfContainer){
            int countIndex = 0;
            int[] head = new int[startPoint];
            int[] tail = new int[numOfContainer - startPoint];

            System.arraycopy(secondPart.containerPermutation, startPoint, tail,
                    0, numOfContainer - startPoint);
            System.arraycopy(secondPart.containerPermutation, 0, head,
                    0, startPoint);

            // Copy to child
            System.arraycopy(firstPart.containerPermutation, startPoint,
                    containerPermutation, startPoint, endPoint - startPoint);



            // check head
            for(int i = 0; i < head.length; ++i){
                if (!valueExists(head[i], firstPart.containerPermutation, startPoint, endPoint)) {
                    containerPermutation[countIndex] = head[i];
                    countIndex++;
                }
            }

            // check tail
            for (int i = 0; countIndex < startPoint; ++i) {
                if (!valueExists(tail[i], firstPart.containerPermutation, startPoint, endPoint)) {
                    containerPermutation[countIndex] = tail[i];
                    countIndex++;
                }
            }

            return containerPermutation;
        }


        // Condition Three
        if(startPoint != 0 && endPoint != numOfContainer){
            int countIndex = endPoint;
            int headPointer = 0;
            int[] head = new int[endPoint];
//            int[] middle = new int[endPoint - startPoint];
            int[] tail = new int[numOfContainer - endPoint];


            // Copy to child
            System.arraycopy(firstPart.containerPermutation, startPoint,
                    containerPermutation, startPoint, endPoint - startPoint);

            // Copy three parts
            System.arraycopy(secondPart.containerPermutation, endPoint, tail,
                    0, numOfContainer - endPoint);
//            System.arraycopy(secondPart.containerPermutation, startPoint, middle,
//                    0, endPoint - startPoint);
            System.arraycopy(secondPart.containerPermutation, 0, head,
                    0, endPoint);

            // check tail
            for (int i = 0; i < tail.length; ++i) {
                if (!valueExists(tail[i], firstPart.containerPermutation, startPoint, endPoint)) {
                    containerPermutation[countIndex] = tail[i];
                    countIndex++;
                }
            }

            // If we have reach the end
            if(countIndex == numOfContainer){
                countIndex = 0;

                // Check the head
                for(; countIndex < startPoint; ++headPointer){
                    if (!valueExists(head[headPointer], firstPart.containerPermutation, startPoint, endPoint)) {
                        containerPermutation[countIndex] = head[headPointer];
                        countIndex++;
                    }
                }

                // We should reach the end

            } else {
                // check head
                for(; countIndex < numOfContainer; ++headPointer){
                    if (!valueExists(head[headPointer], firstPart.containerPermutation, startPoint, endPoint)) {
                        containerPermutation[countIndex] = head[headPointer];
                        countIndex++;
                    }
                }

                // we must have reach the end
                countIndex = 0;

                // check the head
                for(; countIndex < startPoint; ++headPointer){
                    if (!valueExists(head[headPointer], firstPart.containerPermutation, startPoint, endPoint)) {
                        containerPermutation[countIndex] = head[headPointer];
                        countIndex++;
                    }
                }
            } // end else

        } // End third condition

        return containerPermutation;
    }
    // Check if the value exist in the myArray in the range of [startPoint, endPoint)
    private boolean valueExists(int value, int[] myArray, int startPoint, int endPoint){
        for(int i = startPoint; i < endPoint; ++i){
            if(value == myArray[i]) return true;
        }
        return false;
    }

}
