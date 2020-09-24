package multiDGA;

import ProblemDefine.ParameterSettings;
import ProblemDefine.ProblemParameterSettings;
import algorithms.*;
import commonOperators.CommonElitism;
import commonOperators.TournamentSelection;
import dataCollector.DataCollector;
import gaFactory.GAFactory;
import multi_objective_operators.FastNonDominatedSorting;

public class DualPermutationFactory implements GAFactory{
    private DataCollector collector;
    private ProblemParameterSettings proSet;
    private ParameterSettings pars;


    /**
     * Constructor
     *
     */
    public DualPermutationFactory(DataCollector collector, ProblemParameterSettings proSet,
                                  ParameterSettings pars){
        this.proSet = proSet;
        this.pars = pars;
        this.collector = collector;
    }

    @Override
    public InitPop getInitPopMethod() {
        return ((DualPermutationParameterSettings) proSet).getInitMethod();
    }

    @Override
    public Mutation getMutation() {
        return ((DualPermutationParameterSettings) proSet).getMutation();
    }

    @Override
    public Selection getSelection(int tournamentSize, int optimization) {
        return new TournamentSelection(tournamentSize, optimization, pars.getSeed());
    }

    @Override
    public Crossover getCrossover() {
        return ((DualPermutationParameterSettings) proSet).getCrossover();
    }

    @Override
    public Sort getSort() {
        return new FastNonDominatedSorting(pars.getOptimization());
    }

    @Override
    public DataCollector getDataCollector() {
        return collector;
    }

    @Override
    public Elitism getElitism(int elitSize, int optimization) {
        return new CommonElitism(elitSize, optimization);
    }

    @Override
    public Distance getDistance() {
        return ((DualPermutationParameterSettings) proSet).getCrowdingDistance();
    }
}
