/*
 * Boxiong Tan (Maximus Tann)
 * Title:        Single-objective GA framework
 * Description:  Single-objective GA framework for general optimization purpose
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2016-2019, The Victoria University of Wellington
 * ProblemParameterSettings.java - problem related parameters
 */
package ProblemDefine;
import algorithm.*;

// The existence of this abstract class can be discussed..
public abstract class ProblemParameterSettings {
	private Evaluate evaluate;

	public ProblemParameterSettings(Evaluate evaluate){
		this.evaluate = evaluate;
	}

	public Evaluate getEvaluate() {
		return evaluate;
	}

}
