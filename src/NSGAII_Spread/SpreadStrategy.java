package NSGAII_Spread;

import cloudResourceUnit.*;

import java.util.ArrayList;

public class SpreadStrategy {

    // Collector
    SpreadCollector spreadCollector;

    // Once the vmNumEstimation is called, this array will be filled with VM types.
    // It means the VM types to host the containers.
//    private ArrayList<Integer> vmList;
    private ArrayList<PM> pmList;
    private double pmCpu;
    private double pmMem;
    private double k;
    private double maxPmEnergy;
    private double crushPro;
    private double vmCpuOverheadRate;
    private double vmMemOverhead;
    private double[] containerCpu;
    private double[] containerMem;
    private double[] containerAppId;
    private double[] containerMicroServiceId;
    private double[] containerReplicaId;
    private double[] vmCpu;
    private double[] vmMem;

    private ArrayList<Application> appList;
    private double availabilityFitness;

    // Constructor
    public SpreadStrategy(
                            SpreadCollector spreadCollector,
                            double pmCpu,
                            double pmMem,
                            double k,
                            double maxPmEnergy,
                            double crushPro,
                            double vmCpuOverheadRate,
                            double vmMemOverhead,
                            double[] containerCpu,
                            double[] containerMem,
                            double[] containerAppId,
                            double[] containerMicroServiceId,
                            double[] containerReplicaId,
                            double[] vmCpu,
                            double[] vmMem){

        this.spreadCollector = spreadCollector;
        this.pmCpu = pmCpu;
        this.pmMem = pmMem;
        this.k = k;
        this.maxPmEnergy = maxPmEnergy;
        this.crushPro = crushPro;
        this.vmCpuOverheadRate = vmCpuOverheadRate;
        this.vmMemOverhead = vmMemOverhead;
        this.containerCpu = containerCpu;
        this.containerMem = containerMem;
        this.containerAppId = containerAppId;
        this.containerMicroServiceId = containerMicroServiceId;
        this.containerReplicaId = containerReplicaId;
        this.vmCpu = vmCpu;
        this.vmMem = vmMem;
        pmList = new ArrayList<>();
        appList = new ArrayList<>();
    }


    public double getAvailabilityFitness() {
        return availabilityFitness;
    }

    // The estimation uses a FirstFit and just-Fit to estimate the number of VMs that will be used
    public void allocate(){
        // start
        spreadCollector.collectTime(0);
        constructAppList();

        for(Application application:appList){
            ArrayList<MicroService> microServices = application.getMicroServiceList();
            for(MicroService microService:microServices){
                allocateToPms(microService);
            }
        }

//        updateAverageAvailability();
        spreadCollector.collect(pmList);
        spreadCollector.collectTime(1);
        // end

    }

    private void allocateToPms(MicroService microService){
        ArrayList<Container> containers = microService.getContainerList();
        // each container must be allocate to a different PM
        ArrayList<PM> skipList = new ArrayList<>();
        for(Container container:containers) {
            // If fails, create a new PM
            if(!allocateContainer(container, skipList)){
                PM pm = new PM(pmCpu, pmMem, k, maxPmEnergy, crushPro);
                VM vm = createVM(container, pm);
                pm.allocate(vm);
                pmList.add(pm);
            }

            skipList.add(container.getPm());
        }
    }




    private Boolean allocateContainer(Container container, ArrayList<PM> skipList){
        if(pmList.size() == 0)
            return false;
        for(PM pm:pmList) {
            // check if we should skip this pm
            if(skip(pm, skipList)) continue;
            ArrayList<VM> vmList = pm.getVmList();
            for (VM vm : vmList) {
                if (vm.getCpuRemain() >= container.getCpu() && vm.getMemRemain() >= container.getMem()) {
                    vm.allocate(container);
                    return true;
                }
            }
            // If it has not been allocate to an existing VM, try to create a new VM
            VM vm = createVM(container, pm);
            if(vm != null) {
                pm.allocate(vm);
                return true;
            }
        }
        return false;
    }
    private boolean skip(PM pm, ArrayList<PM> skipList){
        if(skipList.size() == 0) return false;
        for(PM skipPm:skipList){
            if(pm.getID() == skipPm.getID()) return true;
        }
        return false;
    }

    // We apply the minimum resource or just-Fit heuristic to create VM
    // Choose a VM with minimum(CPU_v - CPU_c or Mem_v - Mem_c) in the VM list
    private VM createVM(Container container, PM pm){
        return bestFitCreation(container, pm);
    }

    // bestFit approach
    private VM bestFitCreation(Container container, PM pm){

        int numOfVm = vmCpu.length;
        double minimumRemainResource = 1;
        double maximumVolume = 0;
//        int bestVm = 0;
        Integer bestVm = null;

        // find the VM with minimum remaining resources to host this container
        // Also, this VM cannot violate the resource constraint
        for(int i = 0; i < numOfVm; i++){
            if(vmCpu[i] < container.getCpu() + vmCpu[i] * vmCpuOverheadRate ||
                    vmMem[i] < container.getMem() + vmMemOverhead) continue;
            if(vmCpu[i] > pm.getCpuRemain() || vmMem[i] > pm.getMemRemain()) continue;


            double normalizedCpuRemain = (vmCpu[i] - container.getCpu() - vmCpu[i] * vmCpuOverheadRate) / vmCpu[i];
            double normalizedMemRemain = (vmMem[i] - container.getMem() - vmMemOverhead) / vmMem[i];

            // find which resource is smaller
//            double remain = normalizedCpuRemain > normalizedMemRemain ? normalizedCpuRemain:normalizedMemRemain;
            double volume =  1 / (1 - normalizedCpuRemain) * 1 / (1 - normalizedMemRemain);

            // update the choice of VM
//            if(minimumRemainResource > remain) {
//                bestVm = i;
//                minimumRemainResource = remain;
//            }

            if(maximumVolume < volume){
                maximumVolume = volume;
                bestVm = i;
            }

        }

        // NO VM is available
        if(bestVm == null) return null;

        VM vm = new VM(bestVm, vmCpu[bestVm], vmMem[bestVm], vmCpuOverheadRate, vmMemOverhead);
        vm.allocate(container);

        return vm;
    }

//    private double[] firstFitCreation(
//            double containerCpu,
//            double containerMem,
//            double[] vmCpu,
//            double[] vmMem,
//            double vmCpuOverheadRate,
//            double vmMemOverhead){
//
//        int bestVm = 0;
//        double[] vmRemainResource = new double[2];
//
//
//        // we first sort the VMs according to their CPU or memory
//        ArrayList<double[]> vmTypes = new ArrayList();
//        for(int i = 0; i < vmCpu.length; ++i){
//            vmTypes.add(new double[]{vmCpu[i], vmMem[i]});
//        }
//
//        Collections.sort(vmTypes, new Comparator<double[]>() {
//            public int compare(double[] o1, double[] o2) {
//                int condition = 0;
//                int flag = 1;
//                if(o1[flag] - o2[flag] > 0.0) condition = 1;
//                else if(o1[flag] - o2[flag] < 0.0) condition = -1;
//                else condition = 0;
//                return condition;
//            }
//        });
////        for(int i = 0; i < vmTypes.size(); ++i){
////            System.out.println(vmTypes.get(i)[0] + " : " + vmTypes.get(i)[1]);
////        }
//
//        // Find the smallest one and break from it
//        for(int i = 0; i < vmTypes.size(); ++i){
//            if(vmTypes.get(i)[0] >= containerCpu + vmTypes.get(i)[0] * vmCpuOverheadRate
//            && vmTypes.get(i)[1] >= containerMem + vmMemOverhead){
//                vmRemainResource[0] = vmTypes.get(i)[0] - containerCpu - vmTypes.get(i)[0] * vmCpuOverheadRate;
//                vmRemainResource[1] = vmTypes.get(i)[1] - containerMem - vmMemOverhead;
//                bestVm = i;
//                break;
//            }
//        }
//
////        System.out.println("bestVm = " + bestVm);
//        // update the vmList
//        vmList.add(bestVm);
//
//        return vmRemainResource;
//    }


    // Using FF to allocate VMs to PMs and return the number of PMs
//    public int pmNumEstimation(
//                               double[] vmCpu,
//                               double[] vmMem,
//                               double pmCpu,
//                               double pmMem){
//
//        ArrayList<double[]> pmList = new ArrayList();
//        for(Integer vm:vmList){
//            double currentVmCpu = vmCpu[vm];
//            double currentVmMem = vmMem[vm];
//            if(!allocateVM(currentVmCpu, currentVmMem, pmList)){
//                pmList.add(createPM(currentVmCpu, currentVmMem, pmCpu, pmMem));
//            }
//        }
//        return pmList.size();
//    }


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

    private void constructAppList(){
        appList = new ArrayList<>();
        int numOfContainers = containerCpu.length;
        for(int i = 0; i < numOfContainers; i++){
            int appId = (int) containerAppId[i];
            int microId = (int) containerMicroServiceId[i];
            Application app;
            MicroService microService;
            if(appExist(appList, appId)){
                app = getApp(appList, appId);
            } else {
                app = new Application(appId);
                appList.add(app);
            }

            ArrayList<MicroService> microServices = app.getMicroServiceList();
            if(microExist(microServices, microId)){
                microService = getMicroService(microServices, microId);
            } else {
                microService = new MicroService(microId);
                microServices.add(microService);
            }
            microService.addContainer(new Container(i, containerCpu[i], containerMem[i],
                                            (int) containerAppId[i], (int) containerMicroServiceId[i],
                                            (int) containerReplicaId[i]));
        }

    }

    public double updateAverageAvailability(){
        double availability = 0;
        for(Application application:appList){
            availability += application.availability();
        }
        availabilityFitness = availability / appList.size();
        return availabilityFitness;
    }

    public double[] energy(){
        double[] totalEnergy = new double[1];
        for(PM pm:pmList){
            totalEnergy[0] += pm.calEnergy();
        }
        return totalEnergy;
    }

    public double averageEnergy(){
        return energy()[0];
    }

    public double averageNoOfPm(){
        return pmList.size();
    }
    public double averageNoOfVm(){
        double totalVM = 0;
        for(PM pm:pmList){
            totalVM += pm.getVmList().size();
        }
        return totalVM;
    }

    public double[] averageUtil(){
        double[] util = new double[2];
        for(PM pm:pmList){
            pm.updateUtilization();
            util[0] += pm.getCpuUtil();
            util[1] += pm.getMemUtil();
        }
        util[0] = util[0] / pmList.size();
        util[1] = util[1] / pmList.size();
        return util;
    }

    public double[] sdUtil(){
        double[] util = new double[2];
        return util;
    }

    public double waste(){
        double totalWaste = 0;
        for(PM pm:pmList){
            totalWaste += pm.getWastedResource();
        }
        return totalWaste / pmList.size();
    }


    public double sdNoOfPm(){
        return 0;
    }

    public double sdNoOfVm(){
        return 0;
    }



    public double sdEnergy(){
        return 0;
    }

    private PM createPM(VM vm){
        PM pm = new PM(pmCpu, pmMem, k, maxPmEnergy, crushPro);
        pm.allocate(vm);
        return pm;
    }

    // Using FF to allocate VM
    private Boolean allocateVM(VM vm, ArrayList<PM> pmList){
        for(PM pm:pmList){
            if(pm.getCpuRemain() >= vm.getConfigureCpu() && pm.getMemRemain() >= vm.getConfigureMem()){
                pm.allocate(vm);
                return true;
            }
        }
        return false;
    }


}
