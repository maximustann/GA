package GroupGA.src;

import GAProcedure.CommonGA;
import ProblemDefine.ParameterSettings;
import ProblemDefine.ProblemParameterSettings;
import gaFactory.GAFactory;

import java.util.ArrayList;

public class GroupGA extends CommonGA {
    GAFactory factory;
    ParameterSettings pars;
    ProblemParameterSettings proSet;

    /**
     * Constructor
     */

    public GroupGA(ParameterSettings pars, ProblemParameterSettings proSet, GAFactory factory){
        this.factory = factory;
        this.pars = pars;
        this.proSet = proSet;
        prepare();
    }

    @Override
    protected void prepare(){
        maxGen = pars.getMaxGen();
        maxVar = pars.getMaxVar();
        popSize = pars.getPopSize();
        mutationRate = pars.getMutationRate();
        crossoverRate = pars.getCrossoverRate();
        lbound = pars.getLbound();
        ubound = pars.getUbound();
        optimization = pars.getOptimization();
        tournamentSize = pars.getTournamentSize();
        popFit = new ArrayList<double[]>();

        initPop = factory.getInitPopMethod();
        mutation = factory.getMutation();
        crossover = factory.getCrossover();
        selection = factory.getSelection(tournamentSize, optimization);
        elitism = factory.getElitism(pars.getElitSize(), optimization);
        evaluate = proSet.getEvaluate();
        collector = factory.getDataCollector();
        sort = factory.getSort();
    }

}
