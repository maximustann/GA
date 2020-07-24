//package FirstStepALNS;
//
//import java.util.ArrayList;
//
//public class ALNS {
//
//    private ArrayList<ALNSChromosome> archive;
//    private ArrayList<int[]> tabuList;
//    private int numOfServices;
//    private int[] replicaNumOfServices;
//    private int maxGen;
//    private int innerLoopMaxGen;
//    private ALNSSettings settings;
//    private Initialization initialize;
//    private EnergyFitness energyFitness;
//    private AvailabilityFitness availabilityFitness;
//    private int lbound;
//    private int ubound;
//
//    public void run(){
//
//        //
//        boolean improvement = false;
//        int noImprovementCount = 0;
//        // outer iteration changes the replica pattern
//        for(int gen = 0; gen < maxGen; gen++){
//            replicaNumOfServices = generateReplicas();
//
//            // inner iteration stops when there is no more improvement
//            // with current replicas num
//            while(noImprovementCount <= innerLoopMaxGen){
//                ALNSChromosome solution = initialize.init(replicaNumOfServices);
//                if(){
//                    noImprovementCount++;
//                }
//            }
//            replicaNumOfServices = replicaNumDestroy(replicaNumOfServices);
//
//        }
//    }
//
//    // static factory
//    public ALNS prepare(ALNSSettings settings){
//        this.maxGen = settings.getMaxGen();
//        this.innerLoopMaxGen = settings.getInnerLoopMaxGen();
//        this.numOfServices = settings.getNumOfServices();
//        this.initialize = settings.getInitialization();
//        this.lbound = settings.getLbound();
//        this.ubound = settings.getUbound();
//        archive = new ArrayList<>();
//        tabuList = new ArrayList<>();
//        replicaNumOfServices = new int[numOfServices];
//
//        return this;
//    }
//
//    private int[] generateReplicas(int[] currentReplica){
//        int[] replicas = new int[numOfServices];
//        return replicas;
//    }
//
//    // partially destroy the current replica and generate a new one
//    // If the new one is in the tabu list, then return the old one
//    private int[] replicaNumDestroy(int[] replicas){
//        int[] newReplica = generateReplicas(replicas);
//        if(tabuCheck(newReplica))
//            return newReplica;
//        else
//            return replicas;
//    }
//
//    // If the new replica is NOT on the list, return true
//    // Otherwise, return false
//    private boolean tabuCheck(int[] newReplica){
//        for(int[] shortTermReplica:tabuList){
//            for(int i = 0; i < numOfServices; i++){
//                if(shortTermReplica[i] != newReplica[i]) continue;
//            }
//            return false;
//        }
//        return true;
//    }
//
//    private ArrayList<VM> generateVMs(){
//        ArrayList<VM> vmList = new ArrayList<>();
//        return vmList;
//    }
//
//
//}
