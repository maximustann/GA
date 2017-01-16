package algorithms;

import java.util.ArrayList;

public interface Constraint {
	public void evaluate(Chromosome [] popVar, ArrayList<double[]> popViolation);
}
