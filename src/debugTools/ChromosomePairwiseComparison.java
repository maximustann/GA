/*
 * Boxiong Tan (Maximus Tann)
 * Title:        PSO algorithm framework
 * Description:  PSO algorithm framework for general optimization purpose
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2016-2019, The Victoria University of Wellington
 * ChromosomePairwiseComparison.java - An Interface of comparison between two chromosomes.
 */
package debugTools;

import algorithm.Chromosome;
/**
 * chromosome comparison is used in checking whether two chromosomes are the same
 * why not extends comparable? Good question...
 * 
 * @author Boxiong Tan (Maximus Tann) 
 * @since PSO framework 1.0
 */
public interface ChromosomePairwiseComparison {
	public boolean compare(Chromosome v1, Chromosome v2);
	public void comparePop(Chromosome[] pop1, Chromosome[] pop2);
}
