package gaFactory;

import algorithm.Crossover;
import algorithm.Elitism;
import algorithm.InitPop;
import algorithm.Mutation;
import algorithm.Selection;
import algorithm.Sort;
import commonOperators.BinaryFlipCoinMutation;
import commonOperators.CommonElitism;
import commonOperators.InitBinaryChromosome;
import commonOperators.SinglePointCrossover;
import commonOperators.TournamentSelection;
import commonOperators.sortPop;
import dataCollector.DataCollector;

public class BinaryGAFactory implements GAFactory {
	private DataCollector collector;
	
	public BinaryGAFactory(DataCollector collector){
		this.collector = collector;
	}
	@Override
	public InitPop getInitPopMethod() {
		return new InitBinaryChromosome();
	}

	@Override
	public Mutation getMutation() {
		return new BinaryFlipCoinMutation();
	}

	@Override
	public Selection getSelection(int tournamentSize, int optimization) {
		return new TournamentSelection(tournamentSize, optimization);
	}

	@Override
	public Crossover getCrossover() {
		return new SinglePointCrossover();
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

}
