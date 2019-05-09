package gaFactory;

import algorithms.Constraint;
import algorithms.Crossover;
import algorithms.Distance;
import algorithms.Elitism;
import algorithms.InitPop;
import algorithms.Mutation;
import algorithms.Selection;
import algorithms.Sort;
import commonOperators.*;
import dataCollector.DataCollector;

public abstract class IntCoGAFactory implements CoGAFactory{
    /**
     * We might need multiple collectors
     * */
    private DataCollector collector;
    private int numOfSubPop;
    private int seed;

    /**
	 * Constructor
	 * @param collector is the data collector
	 */
	public IntCoGAFactory(DataCollector collector, int numOfSubPop, int seed){
		this.collector = collector;
		this.numOfSubPop = numOfSubPop;
		this.seed = seed;
	}

	@Override
    public int getNumOfSubPop(){
	    return numOfSubPop;
    }

	@Override
	public InitPop[] getInitPopMethod() {
	    InitPop[] pop = new InitPop[numOfSubPop];
	    for(int i = 0; i < numOfSubPop; i++)
            pop[i] = new InitIntChromosomes();
		return pop;
	}

    @Override
    public DataCollector getDataCollector(){
        return collector;
    }

    @Override
    public Mutation[] getMutation() {
	    Mutation[] mutate = new Mutation[numOfSubPop];
	    for(int i = 0; i < numOfSubPop; i++)
	        mutate[i] = new IntReverseSequenceMutation();
        return  mutate;
    }


    @Override
    public Selection[] getSelection(int[] tournamentSize, int optimization) {
	    Selection[] selection = new Selection[numOfSubPop];
	    for(int i = 0; i < numOfSubPop; i++)
	        selection[i] = new TournamentSelection(tournamentSize[i], optimization, seed);
        return selection;
    }

    @Override
    public Crossover[] getCrossover() {
	    Crossover[] crossover = new Crossover[numOfSubPop];
	    for(int i = 0; i < numOfSubPop; i++)
	        crossover[i] = new SinglePointCrossover();

        return crossover;
    }

    @Override
    public Sort[] getSort(){
	    Sort[] sort = new Sort[numOfSubPop];
	    for(int i = 0; i < numOfSubPop; i++)
	        sort[i] = new sortPop();
        return sort;
    }

    @Override
    public Elitism[] getElitism(int[] elitSize, int optimization){
	    Elitism[] elitism = new Elitism[numOfSubPop];
	    for(int i = 0; i < numOfSubPop; i++)
	        elitism[i] = new CommonElitism(elitSize[i], optimization);
        return elitism;
    }

}
