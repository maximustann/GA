package variableReplicasFirstStepGA;

import algorithms.Chromosome;
import algorithms.Mutation;
import algorithms.StdRandom;




/**
 * In this version, the mutation operator simply decides which PMs to break down and reallocate their containers
 */
public class FirstStepGAMutation implements Mutation {
    private int lbound;
    private int ubound;


    public FirstStepGAMutation(
        int lbound,
        int ubound
    ){
        this.lbound = lbound;
        this.ubound = ubound;
    }


    @Override
    public void update(Chromosome child, double mutationRate) {
        mutateVmTypes((FirstStepGAChromosome) child, mutationRate);
    }



    private void mutateVmTypes(FirstStepGAChromosome child, double mutationRate){
        int[] individual = child.getIndividual();
        int numOfServices = child.size();
        int numOfContainer = child.getNumOfContainers();
        for(int i = 0; i < numOfServices; ++i){
            double u = StdRandom.uniform();
            if(u < mutationRate){
                individual[i] = StdRandom.uniform(lbound, ubound);
            }
        }

        child.setIndividual(individual, numOfServices);
    }


}
