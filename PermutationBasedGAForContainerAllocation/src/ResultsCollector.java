import dataCollector.DataCollector;
import spherical.ResultCollector;

import java.util.ArrayList;

public class ResultsCollector extends DataCollector {
    private ArrayList<DualPermutationChromosome> resultData;


    public ResultsCollector(){
        super();
        resultData = new ArrayList<>();

    }

    @Override
    public void collect(Object result) {
        resultData.add((DualPermutationChromosome) result);
    }

    public ArrayList<DualPermutationChromosome> getResultData() {
        return resultData;
    }

    @Override
    public void collectSet(Object set) {
        // Nothing to be done
    }
}
