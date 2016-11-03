package gaFactory;

import java.util.ArrayList;

import GAProcedure.CommonGA;
import ProblemDefine.ParameterSettings;
import ProblemDefine.ProblemParameterSettings;

public class BinaryGA extends CommonGA {
	private GAFactory factory;
	private ParameterSettings pars;
	private ProblemParameterSettings proSet;
	
	public BinaryGA(
					ParameterSettings pars, 
					ProblemParameterSettings proSet, 
					GAFactory factory
					){
		this.factory = factory;
		this.pars = pars;
		this.proSet = proSet;
		prepare();
	}
	@Override
	protected void prepare() {
		maxGen = pars.getMaxGen();
		maxVar = pars.getMaxVar();
		popSize = pars.getPopSize();
		mutationRate = pars.getMutationRate();
		crossoverRate = pars.getCrossoverRate();
		lbound = pars.getLbound();
		ubound = pars.getUbound();
		optimization = pars.getOptimization();
		tournamentSize = pars.getTournamentSize();
		elitSize = pars.getElitSize();
		popFit = new ArrayList<double[]>();

		initPop = factory.getInitPopMethod();
		mutation = factory.getMutation();
		crossover = factory.getCrossover();
		elitism = factory.getElitism(elitSize, optimization);
		selection = factory.getSelection(tournamentSize, optimization);
		evaluate = proSet.getEvaluate();
		collector = factory.getDataCollector();
		sort = factory.getSort();	
	}

}
