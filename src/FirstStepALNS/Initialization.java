//package FirstStepALNS;
//
//import java.util.ArrayList;
//import algorithms.StdRandom;
//
//public class Initialization {
//
//    private int numOfServices;
//
//
//    // VM data
//    private double vmCpuOverheadRate;
//    private double vmMemOverhead;
//    private double[] vmCpu;
//    private double[] vmMem;
//
//
//    public Initialization(int numOfServices){
//        this.numOfServices = numOfServices;
//    }
//
//    public ALNSChromosome init(int[] replicaNumOfServices){
//        ALNSChromosome chromosome = generateChromosome(replicaNumOfServices);
//        return chromosome;
//    }
//
//    private ALNSChromosome generateChromosome(int[] replicaNumOfServices){
//        ALNSChromosome chromosome = new ALNSChromosome();
//        int numOfContainers = calNumOfContainers(replicaNumOfServices);
//        ArrayList<VM> vmList = generateVMs(numOfContainers);
//
//    }
//
//    private int calNumOfContainers(int[] replicaNumOfServices){
//        int numOfContainers = 0;
//        for(int i = 0; i < numOfServices; i++) numOfContainers += replicaNumOfServices[i];
//        return numOfContainers;
//    }
//
//
//    // generate numOfContainers number of random types of VMs,
//    private ArrayList<VM> generateVMs(int numOfContainers){
//        ArrayList<VM> vmList = new ArrayList<>();
//        int numOfVmTypes = vmCpu.length;
//
//        for(int i = 0; i < numOfContainers; i++){
//            int vmType = StdRandom.uniform(numOfVmTypes);
//            vmList.add(new VM(vmType, vmCpu[vmType], vmMem[vmType],
//                                vmCpuOverheadRate, vmMemOverhead));
//        }
//        return vmList;
//    }
//
//}
