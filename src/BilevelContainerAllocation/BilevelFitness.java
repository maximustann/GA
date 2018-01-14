package BilevelContainerAllocation;

import java.util.ArrayList;

import algorithms.*;
public class BilevelFitness extends CoUnNormalizedFit{
    /** some data */
    private static double k;
    private static int containerNum;
    private static double pmCpu;
    private static double pmMem;
    private static double pmEnergy;
    private static double[] vmCpu;
    private static double[] vmMem;
    private static double[] containerCpu;
    private static double[] containerMem;

    public BilevelFitness(
            int containerNum,
            double k,
            double pmCpu,
            double pmMem,
            double pmEnergy,
            double[] vmCpu,
            double[] vmMem,
            double[] containerCpu,
            double[] containerMem
    ){
        super(0,null, null);
        BilevelFitness.k = k;
        BilevelFitness.containerNum = containerNum;
        BilevelFitness.pmCpu = pmCpu;
        BilevelFitness.pmMem = pmMem;
        BilevelFitness.pmEnergy = pmEnergy;
        BilevelFitness.vmCpu = vmCpu;
        BilevelFitness.vmMem = vmMem;
        BilevelFitness.containerCpu = containerCpu;
        BilevelFitness.containerMem = containerMem;
    }

    public BilevelFitness(
            int subPop,
            Chromosome individual,
            Chromosome[] representatives
    ){
        super(subPop, individual, representatives);
    }

    // The actual calculation of energy consumption or the fitness value
    // of the chromosome
    public Object call() throws Exception {
        // TO DO
        return null;
    }
}
