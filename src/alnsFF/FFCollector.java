package alnsFF;

import cloudResourceUnit.PM;
import dataCollector.DataCollector;

import java.util.ArrayList;

public class FFCollector extends DataCollector {
    private ArrayList<PM> pmList;
    private ArrayList<double[]> genTime;
    private double genStart;
    private double genEnd;

    public FFCollector(){
        super();
        genTime = new ArrayList<>();
    }

    @Override
    public void collect(Object pmList){
        this.pmList = (ArrayList<PM>) pmList;
    }

    public ArrayList<PM> getResultData() {
        return pmList;
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
