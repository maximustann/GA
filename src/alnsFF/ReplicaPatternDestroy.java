package alnsFF;

import algorithms.StdRandom;

public class ReplicaPatternDestroy {
    private double replicaDestroyPercentage;
    private int lbound;
    private int ubound;

    public ReplicaPatternDestroy(
            double replicaDestroyPercentage,
            int lbound,
            int ubound){
        this.replicaDestroyPercentage = replicaDestroyPercentage;
        this.lbound = lbound;
        this.ubound = ubound;
    }
    public void destroy(int[] oldReplicaPattern){

        for(int i = 0; i < oldReplicaPattern.length; i++){
            double u = StdRandom.uniform();
            if(u <= replicaDestroyPercentage){
                oldReplicaPattern[i] = StdRandom.uniform(lbound, ubound);
            }
        }
    }
}
