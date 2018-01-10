package ProblemDefine;

import algorithms.CoEvaluate;

public abstract class CoGAProblemParameterSettings {
    private CoEvaluate evaluate;
    public CoGAProblemParameterSettings(CoEvaluate[] evaluates) {
        for(int i = 0; i < evaluates.length; i++)
        this.evaluate = evaluate;

    }
    public CoEvaluate getEvaluate(){ return evaluate; }
}
