package NSGAII_Spread;

import cloudResourceUnit.PM;
import dataCollector.DataCollector;

import java.util.ArrayList;

public class SpreadCollector extends DataCollector {
    private ArrayList<PM> resultData;
    private ArrayList<double[]> genTime;
    private double genStart;
    private double genEnd;

    public SpreadCollector(){
        super();
        resultData = new ArrayList<>();
        genTime = new ArrayList<>();
    }

    @Override
    public void collect(Object paretoFront){
        resultData = (ArrayList<PM>) paretoFront;
    }


    public ArrayList<PM> getResult() {
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
