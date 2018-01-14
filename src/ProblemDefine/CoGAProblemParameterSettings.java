package ProblemDefine;

import algorithms.CoEvaluate;

public abstract class CoGAProblemParameterSettings {
    private CoEvaluate evaluate;
    public CoGAProblemParameterSettings(CoEvaluate evaluate) {
        this.evaluate = evaluate;
    }
    public CoEvaluate getEvaluate(){ return evaluate; }
}
