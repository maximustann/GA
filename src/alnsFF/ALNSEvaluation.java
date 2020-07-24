package alnsFF;

public class ALNSEvaluation {

    public double evaluate(ALNSChromosome solution){
        double fitness = 0;
        fitness = solution.getVmList().size();
        solution.setFitness(fitness);
        return fitness;
    }
}
