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

public class IntCoGAFactory implements CoGAFactory{
    /**
     * We might need multiple collectors
     * */
    private DataCollector[] collectors;

    /**
	 * Constructor
	 * @param collector is the data collector
	 */
	public IntCoGAFactory(DataCollector[] collector){
//		this.collector = collector;
        for(int i = 0; i < collector.length; i++)
            collectors[i] = collector[i];
	}

	@Override
	public InitPop[] getInitPopMethod(int numOfSubPop) {
	    InitPop[] pop = new InitPop[numOfSubPop];
	    for(int i = 0; i < numOfSubPop; i++)
            pop[i] = new InitIntChromosomes();
		return pop;
	}

    @Override
    public DataCollector[] getDataCollector() {
        return collectors;
    }

    @Override
    public Mutation[] getMutation(int numOfSubPop) {
	    Mutation[] mutate = new Mutation[numOfSubPop];
	    for(int i = 0; i < numOfSubPop; i++)
	        mutate[i] = new IntReverseSequenceMutation();
        return  mutate;
    }


    @Override
    public Selection[] getSelection(int[] tournamentSize, int[] optimization,
                                    int numOfSubPop) {
	    Selection[] selection = new Selection[numOfSubPop];
	    for(int i = 0; i < numOfSubPop; i++)
	        selection[i] = new TournamentSelection(tournamentSize[i], optimization[i]);
        return selection;
    }

    @Override
    public Crossover[] getCrossover(int numOfSubPop) {
	    Crossover[] crossover = new Crossover[numOfSubPop];
	    for(int i = 0; i < numOfSubPop; i++)
	        crossover[i] = new SinglePointCrossover();

        return crossover;
    }

    @Override
    public Sort[] getSort(int numOfSubPop){
	    Sort[] sort = new Sort[numOfSubPop];
	    for(int i = 0; i < numOfSubPop; i++)
	        sort[i] = new sortPop();
        return sort;
    }

    @Override
    public Elitism[] getElitism(int[] elitSize, int[] optimization, int numOfSubPop){
	    Elitism[] elitism = new Elitism[numOfSubPop];
	    for(int i = 0; i < numOfSubPop; i++)
	        elitism[i] = new CommonElitism(elitSize[i], optimization[i]);
        return elitism;
    }

    @Override
    public Constraint[] getConstraint(int numOfSubPop) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Distance[] getDistance(int numOfSubPop) {
        // TODO Auto-generated method stub
        return null;
    }
}
