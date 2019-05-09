import algorithms.Gene;
public class DualPermutationGene implements Gene {
    public int[] containerPermutation;
    public int[] vmTypes;
    private int numOfContainers;
    private int numOfVms;

    //Constructor
    public DualPermutationGene(int numOfContainers, int numOfVms){
        containerPermutation = new int[numOfContainers];
        vmTypes = new int[numOfVms];
        this.numOfContainers = numOfContainers;
        this.numOfVms = numOfVms;
    }

    @Override
    public int size(){
        return 0;
    }

    public int getNumOfContainers(){
        return numOfContainers;
    }
    public int getNumOfVms(){
        return numOfVms;
    }

    public void print(){
        System.out.println("container Permutation: ");
        for(int i = 0; i < numOfContainers; i++){
            System.out.print(containerPermutation[i] + " ");
        }
        System.out.println();
        System.out.println("vm Types: ");
        for(int i = 0; i < numOfVms; i++){
            System.out.print(vmTypes[i] + " ");
        }
        System.out.println();
    }

}
