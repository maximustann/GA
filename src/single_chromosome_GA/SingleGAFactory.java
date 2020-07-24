package single_chromosome_GA;

import ProblemDefine.ParameterSettings;
import ProblemDefine.ProblemParameterSettings;
import algorithms.*;
import commonOperators.CommonElitism;
import commonOperators.TournamentSelection;
import commonOperators.sortPop;
import dataCollector.DataCollector;
import gaFactory.GAFactory;

public class SingleGAFactory implements GAFactory {
    private DataCollector collector;
    private ProblemParameterSettings proSet;
    private ParameterSettings pars;

    /**
     * Constructor
     */
    public SingleGAFactory(DataCollector collector, ProblemParameterSettings proSet,
                           ParameterSettings pars){
        this.proSet = proSet;
        this.pars = pars;
        this.collector = collector;
    }

    @Override
    public InitPop getInitPopMethod() {
        return ((SingleGAParameterSettings) proSet).getInitMethod();
    }

    @Override
    public Mutation getMutation(){
        return ((SingleGAParameterSettings) proSet).getMutation();
    }


    @Override
    public Selection getSelection(int tournamentSize, int optimization) {
        return new TournamentSelection(tournamentSize, optimization, pars.getSeed());
    }

    @Override
    public Crossover getCrossover() {
        return ((SingleGAParameterSettings) proSet).getCrossover();
    }

    @Override
    public Sort getSort() {
        return new sortPop();
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
        return null;
    }

}
