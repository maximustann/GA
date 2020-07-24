package twoStepALNS_NSGGA;

import dataCollector.DataCollector;
import variableReplicasFirstStepGA.FirstStepGAChromosome;

import java.util.ArrayList;

public class NSGAIICollector extends DataCollector {
    private ArrayList<ArrayList<FirstStepGAChromosome>> resultData;
    private ArrayList<double[]> genTime;
    private double genStart;
    private double genEnd;

    public NSGAIICollector(){
        super();
        resultData = new ArrayList<>();
        genTime = new ArrayList<>();
    }

    @Override
    public void collect(Object paretoFront){
        resultData.add((ArrayList<FirstStepGAChromosome>) paretoFront);
    }

    public ArrayList<ArrayList<FirstStepGAChromosome>> getResultData() {
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

    @Override
    public void collectSet(Object set) {
        // Nothing to be done
    }
}
