import java.util.ArrayList;

import algorithms.*;
import commonRepresentation.IntValueChromosome;

public class BilevelFitness extends CoUnNormalizedFit{
    /** some data */
    private static double k;
    private static int containerNum;
    private static double pmCpu;
    private static double pmEnergy;
    private static double[] vmCpu;
    private static double[] containerCpu;

    public BilevelFitness(
            int containerNum,
            double k,
            double pmCpu,
            double pmEnergy,
            double[] vmCpu,
            double[] containerCpu
    ){
        super(0,null, null);
        BilevelFitness.k = k;
        BilevelFitness.containerNum = containerNum;
        BilevelFitness.pmCpu = pmCpu;
        BilevelFitness.pmEnergy = pmEnergy;
        BilevelFitness.vmCpu = vmCpu;
        BilevelFitness.containerCpu = containerCpu;
    }

    public BilevelFitness(
            int subPop,
            Chromosome individual,
            Chromosome[] representatives
    ){
        super(subPop, individual, representatives);
    }

    // The actual calculation of energy consumption or the fitness value
    // of a single chromosome
    public Object call() throws Exception {
        // fit[fitness_value, ranking]
        double[] fit = new double[2];
        fit[0] = fitnessIndividual();
        fit[1] = 0;
        return fit;
    }

    private double fitnessIndividual(){
        double fitness = 0.0;
        return fitness;
    }

}
