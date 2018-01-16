package gaFactory;

import ProblemDefine.*;
import java.util.ArrayList;
import GAProcedure.CoGA;
import algorithms.*;
import commonRepresentation.IntValueChromosome;

/**
 * An Int Cooperative coevolution GA
 *
 */
public class IntCoGA extends CoGA{
    private CoGAFactory factory;
    private CoGAParameterSettings pars;
    private CoGAProblemParameterSettings proSet;


    /**
     *
     * Constructor
     * @param pars Parameter settings
     * @param proSet Problem settings
     * @param factory factory is used to assemble parts
     *
     */
    public IntCoGA(CoGAParameterSettings pars, CoGAProblemParameterSettings proSet, CoGAFactory factory){
        this.factory = factory;
        this.pars = pars;
        this.proSet = proSet;
        prepare();
    }
    /**
     * All settings are prepared here,
     * This is the list of all settings, please read carefully
     * <ul>
     * <li>numOfSubPop  the number of sub-populations </li>
     * <li>maxGen 		max number of generation </li>
     * <li>maxVars 		max number of variables in an individual</li>
     * <li>popSizes		population Sizes</li>
     * <li>lbounds		lower bounds of a variable</li>
     * <li>ubounds		upper bounds of a variable</li>
     * <li>optimization maximize (1) or minimize (0)</li>
     * <li>popFits		population fitnesses</li>
     * <li>initPops		population initialization methods</li>
     * <li>mutations	mutation methods</li>
     * <li>crossovers 	crossover methods</li>
     * <li>selections	selection methods</li>
     * <li>evaluates	evaluation methods</li>
     * <li>collectors	data collectors</li>
     * <li>sorts   		sorting methods</li>
     * </ul>
     */
    @Override
    protected void prepare(){
        numOfSubPop = pars.getNumOfSubPop();
        maxGen = pars.getMaxGen();
        maxVars = pars.getMaxVar();
        popSizes = pars.getPopSize();
        mutationRates = pars.getMutationRate();
        crossoverRates = pars.getCrossoverRate();
        lbounds = pars.getLbound();
        ubounds = pars.getUbound();
        optimization = pars.getOptimization();
        tournamentSizes = pars.getTournamentSize();
        elitSizes = pars.getElitSize();

        // This is a little bit tricky. We initial the PopFits outside the main flow
        popFits = new ArrayList[numOfSubPop];
        for(int i = 0; i < numOfSubPop; i++)
            popFits[i] = new ArrayList<double[]>();
        repFits = new double[numOfSubPop];
        // The same with popVars and representatives
        popVars = new ArrayList<Chromosome[]>();
        representatives = new Chromosome[numOfSubPop];
        for(int i = 0; i < numOfSubPop; i++)
            representatives[i] = new IntValueChromosome(maxVars[i]);

        initPops = factory.getInitPopMethod();
        mutations = factory.getMutation();
        crossovers = factory.getCrossover();
        elitisms = factory.getElitism(elitSizes, optimization);
        selections = factory.getSelection(tournamentSizes, optimization);
        evaluate = proSet.getEvaluate();
        constraints = proSet.getConstraints();
        collector = factory.getDataCollector();
        sorts = factory.getSort();
    }


}
