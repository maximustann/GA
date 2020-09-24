package NSGAII_Spread;

import cloudResourceUnit.*;
import java.util.ArrayList;

public class SpreadPostProcessingUnit {
    private ArrayList<PM> pmList;
    private ArrayList<Double> spreadTime;
    private ArrayList<Double> nsgaiiTime;
    private int generation;


    public SpreadPostProcessingUnit(
            ArrayList<PM> pmList,
            ArrayList<Double> nsgaiiTime,
            ArrayList<Double> spreadTime
    ){
        this.pmList = pmList;
        this.spreadTime = spreadTime;
        this.nsgaiiTime = nsgaiiTime;
    }



    public double[][] allParetoFront(){
        double[][] paretoFront = new double[1][3];
        paretoFront[0][0] = energy()[0];
        paretoFront[0][1] = avail(pmList);
        paretoFront[0][2] = 0;
        return paretoFront;
    }

    public double[] energy(){
        double[] energyConsumption = new double[1];

        for(int i = 0; i < pmList.size(); i++){
            energyConsumption[0] += pmList.get(i).calEnergy();
        }

        return energyConsumption;
    }
    private double avail(ArrayList<PM> pmList){
        ArrayList<Application> appList = constructAppList(pmList);
        return updateAverageAvailability(appList);
    }

    private double updateAverageAvailability(ArrayList<Application> appList){
        double availability = 0;
        for(Application application:appList){
            availability += application.availability();
        }

        return availability / appList.size();
    }

    // loop through all the containers to renew the applications on all PMs
    private ArrayList<Application> constructAppList(ArrayList<PM> pmList) {
        ArrayList<Application> appList = new ArrayList<>();
        for (PM pm : pmList) {
            ArrayList<VM> vmList = pm.getVmList();
            for (VM vm : vmList) {
                ArrayList<Container> containerList = vm.getContainerList();
                for (Container container : containerList) {
                    int applicationId = container.getApplicationId();
                    int microId = container.getMicroServiceId();
                    Application app;
                    MicroService microService;
                    // If the application list contains this application
                    if (appExist(appList, applicationId)) {
                        app = getApp(appList, applicationId);
                    } else {
                        app = new Application(applicationId);
                        appList.add(app);
                    }
                    ArrayList<MicroService> microServiceList = app.getMicroServiceList();
                    // If the application contains this micro service
                    if (microExist(microServiceList, microId)) {
                        microService = getMicroService(microServiceList, microId);
                    } else {
                        microService = new MicroService(microId);
                        microServiceList.add(microService);
                    }
                    microService.addContainer(container);
                }
            }
//            pm.updateAppTable();
        }
        return appList;
    }

    private boolean appExist(ArrayList<Application> appList, int applicationId){
        for(Application app:appList){
            if(app.getID() == applicationId) return true;
        }
        return false;
    }

    private boolean microExist(ArrayList<MicroService> microList, int microId){
        for(MicroService microService:microList){
            if(microService.getID() == microId) return true;
        }
        return false;
    }

    private MicroService getMicroService(ArrayList<MicroService> microList, int microId){
        for(MicroService microService:microList){
            if(microService.getID() == microId) return microService;
        }
        return null;
    }

    private Application getApp(ArrayList<Application> appList, int applicationId){
        for(Application app:appList){
            if(app.getID() == applicationId) return app;
        }
        return null;
    }

    public double waste(){
        double waste = 0;
        for(PM pm:pmList){
            waste += pm.getWastedResource();
        }
        return waste / pmList.size();
    }

    public double[] averageUtil(){
        double[] util = new double[2];
        for(PM pm:pmList){
            util[0] += pm.getCpuUtil();
            util[1] += pm.getMemUtil();
        }
        util[0] /= pmList.size();
        util[1] /= pmList.size();
        return util;
    }

    public double noOfPm(){
        return pmList.size();
    }

    public double noOfVm(){
        double noOfVm = 0;
        for(PM pm:pmList){
            noOfVm += pm.getVmList().size();
        }
        return noOfVm;
    }

    public double[][] convergenceCurve(){
        double[][] convergence = new double[1][2];
        return convergence;
    }

    public double getAggregatedTime(){
        double total = 0;
        for(int i = 0; i < nsgaiiTime.size(); i++) {
            total += nsgaiiTime.get(i) + spreadTime.get(i);
        }
        return total / nsgaiiTime.size();
    }

}
