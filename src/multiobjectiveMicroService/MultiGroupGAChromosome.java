package multiobjectiveMicroService;

import algorithms.Chromosome;
import algorithms.Gene;
import cloudResourceUnit.*;
import java.util.*;

public class MultiGroupGAChromosome extends Chromosome {
    private ArrayList<Application> appList;
    private ArrayList<PM> pmList;
    private int numOfContainers;
    private double energyFitness;
    private double availabilityFitness;


    // Constructor
    public MultiGroupGAChromosome(int numOfContainers){
        this.numOfContainers = numOfContainers;
    }



    public void updateAppList(){
        appList = constructAppList();
    }






    // sort the PMs according to its replica number in ascending order
    //
    public void sortPmAvailability(){
        ArrayList<Integer> availScore = new ArrayList<>();
        HashMap<Integer, Integer> indexToId = new HashMap<>();
        // For each pm, we setup a counter for its replicas
        for(int i = 0; i < pmList.size(); i++){
            int replicas = 0;
            availScore.add(replicas);
            indexToId.put(pmList.get(i).getID(), i);
        }

        // For each micro-service, we adds up the replicas for each PM
        for(Application application:appList){
            ArrayList<MicroService> microServiceList = application.getMicroServiceList();
            for(MicroService microService:microServiceList){
                ArrayList<int[]> pms = microService.replicaNum();
                if(pms.size() == 0) continue;
                for(int[] pm:pms){
                    int index = indexToId.get(pm[0]);
                    availScore.set(index, availScore.get(index) + pm[1]);
                }
            }
        }

        // assign score for each pm
        for(int i = 0; i < pmList.size(); i++){
            pmList.get(i).setReplicaNum(availScore.get(i));
        }


        Collections.sort(pmList, new Comparator<PM>() {
            @Override
            public int compare(PM o1, PM o2) {
                if(o1.getReplicaNum() > o2.getReplicaNum()){
                    return 1;
                } else if(o1.getReplicaNum() == o2.getReplicaNum()){
                    return 0;
                } else{
                    return -1;
                }
            }
        });


    }


    public void setAvailabilityFitness(double availabilityFitness) {
        this.availabilityFitness = availabilityFitness;
    }


    public double getEnergyFitness() {
        return energyFitness;
    }

    public double getAvailabilityFitness() {
        return availabilityFitness;
    }

    public void setEnergyFitness(double fitness) {
        this.energyFitness = fitness;
    }

    public ArrayList<Application> getAppList() {
        return appList;
    }

    public void setAppList(ArrayList<Application> appList) {
        this.appList = appList;
    }

    public ArrayList<PM> getPmList() {
        return pmList;
    }

    public double getEnergy(){
        double energy = 0;
        for(PM pm:pmList){
            energy += pm.calEnergy();
        }
        return energy;
    }


    public double updateAverageAvailability(){
        double availability = 0;
        for(Application application:appList){
            availability += application.availability();
        }
        availabilityFitness = availability / appList.size();
        return availabilityFitness;
    }

    public double averagePmCpuUtil(){
        double totalCpuUtil = 0;
        for(PM pm:pmList){
            pm.updateUtilization();
            totalCpuUtil += pm.getCpuUtil();
        }
        return totalCpuUtil / pmList.size();
    }

    public double averageWaste(){
        double totalWasted = 0;
        for(PM pm:pmList){
            totalWasted += pm.getWastedResource();
        }
        return totalWasted / pmList.size();
    }

    public double averagePmMemUtil(){
        double totalMemUtil = 0;
        for(PM pm:pmList){
            pm.updateUtilization();
            totalMemUtil += pm.getMemUtil();
        }
        return totalMemUtil / pmList.size();
    }

    public int getNoOfVm(){
        int totalVmNo = 0;
        for(PM pm:pmList){
            totalVmNo += pm.getVmList().size();
        }
        return totalVmNo;
    }

    public int getNoOfPm(){
        return pmList.size();
    }

    public int getNumOfContainers(){
        return numOfContainers;
    }


    public void sortPms(){
        // update wasted resources before sorting
        for(PM pm:pmList){
            pm.updateWastedResources();
        }
        Collections.sort(pmList);
    }


    // Descending order
    public void sortPmsVmOverhead(){
        Collections.sort(pmList, new Comparator<PM>() {
            @Override
            public int compare(PM o1, PM o2) {
                if(o1.getVmList().size() > o2.getVmList().size()){
                    return -1;
                } else if(o1.getVmList().size() == o2.getVmList().size()){
                    return 0;
                } else{
                    return 1;
                }
            }
        });
    }

    // Descending order with PM Cpu
    public void sortPmsCpuUtil(){
        Collections.sort(pmList, new Comparator<PM>() {
            @Override
            public int compare(PM o1, PM o2) {
                if(o1.getCpuUtil() > o2.getCpuUtil()){
                    return -1;
                } else if(o1.getCpuUtil() == o2.getCpuUtil()){
                    return 0;
                } else {
                    return 1;
                }
            }
        });
    }

    // Descending order with PM Mem:
    public void sortPmsMemUtil(){
        Collections.sort(pmList, new Comparator<PM>() {
            @Override
            public int compare(PM o1, PM o2) {
                if(o1.getMemUtil() > o2.getMemUtil()){
                    return -1;
                } else if(o1.getCpuUtil() == o2.getCpuUtil()){
                    return 0;
                } else {
                    return 1;
                }
            }
        });
    }

    public void checkViolations(){
        for(PM pm:pmList){
            ArrayList<VM> vmList = pm.getVmList();
            for(VM vm:vmList){
                ArrayList<Container> containerList = vm.getContainerList();
                for(Container container:containerList){
//                    if(container.getMotherVm() != null){
//                        throw new IllegalStateException();
//                    }
                    if(container.getPm().getID() != pm.getID()){
                        throw new IllegalStateException();
                    }
                }
            }
        }

    }



//    public void checkMotherVm(){
//        for(PM pm:pmList){
//            ArrayList<VM> vmList = pm.getVmList();
//            for(VM vm:vmList){
//                ArrayList<Container> containerList = vm.getContainerList();
//                for(Container container : containerList){
//                    if(container.getMotherVm() == null){
//                        System.out.println("alert");
//                        return;
//                    }
//                }
//            }
//        }
//    }

    public void setPmList(ArrayList<PM> pmList) {
        this.pmList = pmList;
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public int size() {
        return pmList.size();
    }

    @Override
    public Gene cut(int cutPoint, int geneIndicator) {
        return null;
    }

    @Override
    public void print() {
        System.out.println("PM num = " + size());
        System.out.println("VM num = " + getNoOfVm());
        System.out.println("PM ave Cpu Util = " + averagePmCpuUtil());
        System.out.println("PM ave Mem Util = " + averagePmMemUtil());
//        for(PM pm:pmList){
//            System.out.println("pm waste = " + pm.updateWastedResources());
//        }
    }

    @Override
    public Chromosome clone() {
        MultiGroupGAChromosome chromosome = new MultiGroupGAChromosome(numOfContainers);
        ArrayList<PM> newPmList = new ArrayList<>();
        for(int i = 0; i < pmList.size(); i++){
            newPmList.add(pmList.get(i).clone());
        }

        chromosome.setPmList(newPmList);
//        chromosome.updateAppList();
        chromosome.setEnergyFitness(energyFitness);
        chromosome.setAvailabilityFitness(availabilityFitness);
        return chromosome;
    }

    @Override
    public boolean equals(Chromosome target) {
        return false;
    }


    private MicroService getMicroService(ArrayList<MicroService> microList, int microId){
        for(MicroService microService:microList){
            if(microService.getID() == microId) return microService;
        }
        return null;
    }

    private boolean appExist(ArrayList<Application> appList, int applicationId){
        for(Application app:appList){
            if(app.getID() == applicationId) return true;
        }
        return false;
    }

    private Application getApp(ArrayList<Application> appList, int applicationId){
        for(Application app:appList){
            if(app.getID() == applicationId) return app;
        }
        return null;
    }

    // loop through all the containers to renew the applications on all PMs
    private ArrayList<Application> constructAppList(){
        ArrayList<Application> appList = new ArrayList<>();
        for(PM pm:pmList){
            ArrayList<VM> vmList = pm.getVmList();
            for(VM vm:vmList){
                ArrayList<Container> containerList = vm.getContainerList();
                for(Container container:containerList){
                    int applicationId = container.getApplicationId();
                    int microId = container.getMicroServiceId();
                    Application app;
                    MicroService microService;
                    // If the application list contains this application
                    if(appExist(appList, applicationId)){
                        app = getApp(appList, applicationId);
                    } else {
                        app = new Application(applicationId);
                        appList.add(app);
                    }
                    ArrayList<MicroService> microServiceList = app.getMicroServiceList();
                    // If the application contains this micro service
                    if(microExist(microServiceList, microId)){
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

    private boolean microExist(ArrayList<MicroService> microList, int microId){
        for(MicroService microService:microList){
            if(microService.getID() == microId) return true;
        }
        return false;
    }

    private int getContainerNum(){
        int total = 0;
        for(PM pm:pmList){
            total += pm.getContainerIdList().size();
        }
        return total;
    }


}
