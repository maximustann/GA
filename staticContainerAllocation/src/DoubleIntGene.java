import algorithms.Gene;

public class DoubleIntGene implements Gene{
    public int[] containerAllocation;
    public int[] vmAllocation;

    // Constructor
    public DoubleIntGene(int numOfContainers, int numOfVms){
        containerAllocation = new int[numOfContainers];
        vmAllocation = new int[numOfVms];
    }


    public int getNumOfContainers(){
        return containerAllocation.length;
    }

    public int getNumOfVms(){
        return vmAllocation.length;
    }

//    @Override
//    public int size(){
        //doing nothing
//        return 0;
//    }
}
