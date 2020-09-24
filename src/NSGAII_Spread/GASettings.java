package NSGAII_Spread;

public class GASettings {
    private String name;
    private double ubound;
    private double lbound;
    private double crossoverRate;
    private double mutationRate;
    private int optimization;
    private int tournamentSize;
    private int eliteSize;
    private int popSize;
    private int maxGen;
    private int permutationNum;

    public GASettings(
            String name,
            double ubound,
            double lbound,
            double crossoverRate,
            double mutationRate,
            int optimization,
            int tournamentSize,
            int eliteSize,
            int popSize,
            int maxGen,
            int permutationNum
    ){
        this.name = name;
        this.lbound = lbound;
        this.ubound = ubound;
        this.crossoverRate = crossoverRate;
        this.mutationRate = mutationRate;
        this.optimization = optimization;
        this.tournamentSize = tournamentSize;
        this.eliteSize = eliteSize;
        this.popSize = popSize;
        this.maxGen = maxGen;
        this.permutationNum = permutationNum;
    }

    public String getName() {
        return name;
    }

    public double getUbound() {
        return ubound;
    }

    public double getLbound() {
        return lbound;
    }

    public double getCrossoverRate() {
        return crossoverRate;
    }

    public double getMutationRate() {
        return mutationRate;
    }

    public int getOptimization() {
        return optimization;
    }

    public int getTournamentSize() {
        return tournamentSize;
    }

    public int getEliteSize() {
        return eliteSize;
    }

    public int getPopSize() {
        return popSize;
    }

    public int getMaxGen() {
        return maxGen;
    }

    public int getPermutationNum() {
        return permutationNum;
    }
}
