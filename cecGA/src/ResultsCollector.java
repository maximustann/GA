import dataCollector.DataCollector;

import java.util.ArrayList;

public class ResultsCollector extends DataCollector {
    private ArrayList<CecGAChromosome> resultData;


    public ResultsCollector(){
        super();
        resultData = new ArrayList<>();

    }

    @Override
    public void collect(Object result) {
        resultData.add((CecGAChromosome) result);
    }

    public ArrayList<CecGAChromosome> getResultData() {
        return resultData;
    }

    @Override
    public void collectSet(Object set) {
        // Nothing to be done
    }
}
