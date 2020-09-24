package LNS_FF;

import cloudResourceUnit.PM;
import dataCollector.DataCollector;

import java.util.ArrayList;

public class FFCollector extends DataCollector {
    private ArrayList<PM> pmList;

    public FFCollector(){
        super();
    }

    @Override
    public void collect(Object pmList){
        this.pmList = (ArrayList<PM>) pmList;
    }

    public ArrayList<PM> getResultData() {
        return pmList;
    }


    @Override
    public void collectSet(Object set) {
        // Nothing to be done
    }
}
