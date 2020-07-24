package single_chromosome_GA;

import GAProcedure.CommonGA;
import ProblemDefine.ParameterSettings;
import ProblemDefine.ProblemParameterSettings;
import algorithms.Chromosome;
import algorithms.TwoParentsCrossover;
import gaFactory.GAFactory;

import java.util.ArrayList;

public class SingleGA extends CommonGA {
    GAFactory factory;
    ParameterSettings pars;
    ProblemParameterSettings proSet;


    public SingleGA(ParameterSettings pars, ProblemParameterSettings proSet, GAFactory factory) {
        this.factory = factory;
        this.pars = pars;
        this.proSet = proSet;
        prepare();
    }

    public void run(int seed){
        collector.collectTime(0);
        initializeRand(seed);
        popVar = initPop.init(popSize, maxVar, lbound, ubound);
        ((ResultsCollector) collector).collectGenTime(0, 0);

        for(int i = 0; i < maxGen; i++){
            int childrenCount = elitism.getSize();
            Chromosome[] newPop = new SingleChromosome[popSize];
            try {
                evaluate.evaluate(popVar, popFit);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            sort.sort(popVar, popFit);
            elitism.carryover(popVar, newPop);
            collector.collect(popVar[0].clone());

            while(true) {
                int exitFlag = 0;
                Chromosome father = (SingleChromosome) popVar[selection.selected(popVar, popFit)];
                Chromosome mother = (SingleChromosome) popVar[selection.selected(popVar, popFit)];

                Chromosome[] children = ((TwoParentsCrossover)crossover)
                        .update(father, mother, crossoverRate);


                for(int j = 0; j < children.length; j++) {
                    mutation.update(children[j], mutationRate);
                    newPop[childrenCount] = children[j];
                    childrenCount++;
                    if(childrenCount == popSize) {
                        exitFlag = 1;
                        break;
                    }
                }
                if(exitFlag == 1) break;
            }
            popVar = newPop;
            ((ResultsCollector) collector).collectGenTime(1, popFit.get(0)[0]);
        }
        collector.collectTime(1);
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
        popFit = new ArrayList<>();

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