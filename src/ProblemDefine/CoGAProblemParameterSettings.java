package ProblemDefine;

import algorithms.CoEvaluate;
import algorithms.Constraint;

public abstract class CoGAProblemParameterSettings {
    /**
     * Notice that, ProblemParameterSettings is related to Problem input including evaluation
     */
    private CoEvaluate evaluate;

    public CoGAProblemParameterSettings(CoEvaluate evaluate) {
        this.evaluate = evaluate;
    }
    public CoEvaluate getEvaluate(){ return evaluate; }
}
