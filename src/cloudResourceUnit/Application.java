package cloudResourceUnit;

import java.util.ArrayList;

public class Application {
    private int ID;
    private ArrayList<MicroService> microServiceList;

    public Application(int ID){
        this.ID = ID;
        microServiceList = new ArrayList<>();
    }

    public void addMicroService(MicroService microService){
        microServiceList.add(microService);
    }

    public void setMicroServiceList(ArrayList<MicroService> microServiceList) {
        this.microServiceList = microServiceList;
    }

    public ArrayList<MicroService> getMicroServiceList() {
        return microServiceList;
    }

    public int getID() {
        return ID;
    }



    public double availability(){
        ArrayList<Double> avail = new ArrayList<>();
        for(MicroService microService:microServiceList){
            avail.add(microService.probability());
        }

        // sort descending
//        Collections.sort(avail,  Collections.reverseOrder());

        // return the largest probability as the availability
//        return avail.get(0);

        Double availability = 1.0;
        for(Double availPro:avail){
            availability *= availPro;
        }

        return availability;
    }
}
