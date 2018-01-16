package ProblemDefine;

import algorithms.CoEvaluate;
import algorithms.Constraint;

public abstract class CoGAProblemParameterSettings {
    /**
     * Notice that, ProblemParameterSettings is related to Problem input including evaluation and constraint
     * handling.
     */
    private CoEvaluate evaluate;
    private Constraint[] constraints;

    public CoGAProblemParameterSettings(CoEvaluate evaluate, Constraint[] constraints) {
        this.evaluate = evaluate;
        this.constraints = constraints;
    }
    public CoEvaluate getEvaluate(){ return evaluate; }

    public Constraint[] getConstraints() {
        return constraints;
    }
}
