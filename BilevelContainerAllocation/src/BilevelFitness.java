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
        // if subPop is 0 and 1, means this individual is for allocation


        double fitness = 0.0;
        ((IntValueChromosome)individual).toMatrix(containerNum);
        ((IntValueChromosome) representatives[1]).toMatrix(containerNum);

        if(subPop == 0) fitness = containerIndividualFitness();
        else fitness = vmIndividualFitness();
        return fitness;
    }

    private double vmIndividualFitness(){
        double fitness;
        // For containers, we first calculate their requirement on each VM
        // requirement[container][VM]
        double[][] requirement = containerCpuRequirements((IntValueChromosome) representatives[0]);

        // Then, we calculate the desired capacity of VMs
        // neededCapacity[VM]
        double[] neededCapacity = neededVMCapacity(requirement);

        // Then, we calculate the PM usage based on the needed capacity and VM deployment
        double[] pmUsed = pmUsage(neededCapacity, (IntValueChromosome) individual);

        // Finally, we calaculate the energy consumption of all PMs
        fitness = energyConsumption(pmUsed);


        return fitness;
    }

    private double containerIndividualFitness(){
        double fitness;
        // For containers, we first calculate their requirement on each VM
        // requirement[container][VM]
        double[][] requirement = containerCpuRequirements((IntValueChromosome) individual);

        // Then, we calculate the desired capacity of VMs
        // neededCapacity[VM]
        double[] neededCapacity = neededVMCapacity(requirement);

        // Then, we calculate the PM usage based on the needed capacity and VM deployment
        double[] pmUsed = pmUsage(neededCapacity, (IntValueChromosome) representatives[1]);

        // Finally, we calaculate the energy consumption of all PMs
        fitness = energyConsumption(pmUsed);
        return fitness;
    }


    /**
     * Here comes our fitness function.
     * For each pm, we use the follow formula to calculate its energy consumption
     * energy = k * pmEnergy + (1 - k) * pmEnergy * (pmUsed / pmCpu)
     *
     * @param pmUsed
     * @return
     */
    private double energyConsumption(double[] pmUsed){
        double energy = 0;

        // For all pms. Notice that we have the same number of PM and containers.
        for(int i = 0; i < containerNum; i++){
            // If pmUsed[i] is a very small number, then it will be treated as non-activated.
            if(pmUsed[i] <= 0.0005) continue;
            energy += k * pmEnergy + (1 - k) * pmEnergy * (pmUsed[i] / pmCpu);
        }
        return energy;
    }

    private double[] pmUsage(double[] neededCapacity, IntValueChromosome vmAllocation){
        double[] pmUsed = new double[containerNum];
        for(int j = 0; j < containerNum; j++){
            for(int i = 0; i < containerNum; i++){
                pmUsed[j] += vmAllocation.matrixIndividual[i][j] * neededCapacity[j];
            }
        }
        return pmUsed;
    }


    // calculate the desired VM capacity based on current deployment of containers
    // By adding up the CPU requirement of containers in VMs
    private double[] neededVMCapacity(double[][] requirement){
        double[] neededCapacity = new double[containerNum];
        // For each column
        for(int j = 0; j < containerNum; j++){
            for(int i = 0; i < containerNum; i++){
                neededCapacity[j] += requirement[i][j];
            }
        }
        return neededCapacity;
    }

    private double[][] containerCpuRequirements(IntValueChromosome container){
        double[][] requirementMatrix = new double[containerNum][containerNum];
        // for each row
        for(int i = 0; i < containerNum; i++) {
            // for each column
            for(int j = 0; j < containerNum; j++) {
               requirementMatrix[i][j] = ((IntValueChromosome) container).matrixIndividual[i][j] * BilevelFitness.containerCpu[i];
            }
        }
        return requirementMatrix;
    }


}
