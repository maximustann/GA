package single_chromosome_GA;

import dataCollector.DataCollector;

import java.util.ArrayList;

public class ResultsCollector extends DataCollector {
    private ArrayList<SingleChromosome> resultData;
    private ArrayList<double[]> genTime;
    private double genStart;
    private double genEnd;

    public ResultsCollector(){
        super();
        resultData = new ArrayList<>();
        genTime = new ArrayList<>();
    }

    @Override
    public void collect(Object result){
        resultData.add((SingleChromosome) result);
    }

    @Override
    public void collectSet(Object set){
        // nothing to be done
    }
    public ArrayList<SingleChromosome> getResultData() {
        return resultData;
    }
    public void collectGenTime(int gFlag, double fitness){
        if(gFlag == 0) genStart = System.nanoTime();
        else {
            genEnd = System.nanoTime();
            double time = Math.floor((genEnd - genStart) / 10000000.0) / 100.0;
            double[] gTime = new double[2];
            gTime[0] = time;
            gTime[1] = fitness;
            genTime.add(gTime);
        }
    }

    public ArrayList<double[]> getGenTime() {
        return genTime;
    }

}
