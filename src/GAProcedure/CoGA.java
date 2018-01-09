package GAProcedure;

import algorithms.Chromosome;
import algorithms.Coevolution;
import algorithms.TwoParentsCrossover;

public abstract class CoGA extends Coevolution {
/**
 * Steps:
 * <ul>
 *  <li> start timer </li>
 * 	<li> 1. initialize sub-populations </li>
 * 	<ul>
 * 		For each generation
 * 		<li> 2. evaluate each population, collect the best fitness value</li>
 * 		<li> 3. sort population </li>
 * 		<li> 4. select parents </li>
 * 		<li> 5. crossover </li>
 * 		<li> 6. store children </li>
 * 		<li> 7. mutation </li>
 * 	</ul>
 * 	<li> end timer </li>
 * </ul>
 *
 * @param seed Random seed
 */
    @Override
    public void run(int seed){
        collectors[0].collectTime(0);
    }
}
