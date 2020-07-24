package alnsFF;

import cloudResourceUnit.*;

import java.util.ArrayList;

public class FFPostProcessingUnit {
    private ArrayList<PM> pmList;

    public FFPostProcessingUnit(
            ArrayList<PM> pmList){
        this.pmList = pmList;
    }
    public double[][] allParetoFront() {
        double[][] paretoFront = new double[1][4];
        paretoFront[0][1] = energy()[0];
        paretoFront[0][2] = avail(pmList);
        paretoFront[0][3] = 0;
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
        double averageWaste = 0;
        for(int i = 0; i < pmList.size(); i++){
            averageWaste += pmList.get(i).getWastedResource();
        }
        return averageWaste / pmList.size();
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
    public double[][] convergenceCurve(){
        double[][] aveFitness = new double[1][2];
//        for(int i = 0; i < generation; i++){
//            aveFitness[i] = genTime.get(i);
//        }

        return aveFitness;
    }

    //     average VM number
    public double averageNoOfVm(){
        int averageVm = 0;
        for(int i = 0; i < pmList.size(); i++){
            averageVm += pmList.get(i).getVmList().size();
        }

        return averageVm / pmList.size();
    }

    public double totalNoOfVm(){
        int total = 0;
        for(PM pm:pmList){
            total += pm.getVmList().size();
        }
        return total;
    }

    public double averageNoOfPm(){
//        return pmList.size();
        return 0;
    }

    public double noOfPm(){
        return pmList.size();
    }

    // dummy functions
    public double sdNoOfPm(){
        return 0;
    }

    public double sdNoOfVm(){
        return 0;
    }
    public double[] sdUtil(){
        double[] sdUtil = new double[2];
        sdUtil[0] = 0;
        sdUtil[1] = 0;

        return sdUtil;
    }

    public double getMeanTime() {
        return 0;
    }

    public double getSdTime() {
        return 0;
    }


}
