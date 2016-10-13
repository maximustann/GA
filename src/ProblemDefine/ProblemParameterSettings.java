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
