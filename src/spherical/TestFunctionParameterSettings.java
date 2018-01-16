/*
 * Boxiong Tan (Maximus Tann)
 * Title:        Single-objective Genetic algorithm framework
 * Description:  Single-objective Genetic algorithm framework for general optimization purpose
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2016-2019, The Victoria University of Wellington
 * TestFunctionParameterSettings.java an implementation of unNormalizedFit
 */
package spherical;

import ProblemDefine.ProblemParameterSettings;
import algorithms.Constraint;
import algorithms.Evaluate;

public class TestFunctionParameterSettings extends ProblemParameterSettings{

	public TestFunctionParameterSettings(Evaluate evaluate, Constraint[] constraints) {
		super(evaluate, constraints);
	}

}
