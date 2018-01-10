package algorithms;

import java.util.ArrayList;

/**
 * Defines the interface of cooperative evaluation
 *
 * @author Boxiong Tan (Maximus Tann)
 * @since Genetic Algorithm framework 2.0
 */

public interface CoEvaluate {

    /**
     *
     * @param subPop indicates the index of the current sub-population
     * @param popVar is the population
     * @param representatives is the representatives (pBests)
     * @param popFit is the fitness values
     */
    public void evaluate(int subPop, Chromosome[] popVar, Chromosome[] representatives, ArrayList<double[]> popFit);

}
