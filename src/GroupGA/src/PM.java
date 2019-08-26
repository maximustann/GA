package GroupGA.src;

import java.util.ArrayList;

public class PM {

    // The basic configuration of PM;
    static double configueCpu;
    static double configueMem;
    static double maxEnergy;
    static double k;


    // VM boundary
    private double cpuRemain;
    private double memRemain;

    // the real usage by containers and vm overheads
    private double cpuUsage;
    private double memUsage;


    // The utilization
    private double cpuUtil;
    private double memUtil;

    // the current energy consumption
    private double energy;

    // the list of VM
    private ArrayList<VM> vmList;


    // Initialize the PM
    public PM(){
        cpuRemain = configueCpu;
        memRemain = configueMem;
        cpuUsage = 0;
        memUsage = 0;
        cpuUtil = 0;
        memUtil = 0;
        energy = k * maxEnergy;
        vmList = new ArrayList<>();
    }


    // Allocate a VM into this PM
    public void allocate(PM  this, VM vm){
        // Check whether the resources are enough
        if(cpuRemain < vm.getConfigureCpu() || memRemain < vm.getConfigureMem()){
            throw new IllegalArgumentException("No enough resources in the PM");
        }


        cpuRemain -= vm.getConfigureCpu();
        memRemain -= vm.getConfigureMem();
        vmList.add(vm);
        vm.setMotherPm(this);

        cpuUsage += vm.getCpuUsage();
        memUsage += vm.getMemUsage();
    }

    // Use it as a lazy function
    public void updateUtilization(){
        cpuUtil = cpuUsage / configueCpu;
        memUtil = memUsage / configueMem;
    }

    // Use it as a lazy function
    public void calEnergy(){
        energy = k * maxEnergy + (1 - k) * cpuUtil;
    }

    // Release a VM from this PM using the index of the VM
    public void release(int indexOfVm){
        if(indexOfVm >= vmList.size()){
            throw new IllegalArgumentException("There is no such VM in the PM");
        }

        VM vm = vmList.get(indexOfVm);
        cpuRemain += vm.getConfigureCpu();
        memRemain += vm.getConfigureMem();
        cpuUsage -= vm.getCpuUsage();
        memUsage -= vm.getMemUsage();
        vmList.remove(indexOfVm);
    }


    // Check how many containers it has
    public int numOfContainers(){
        int num = 0;
        for(VM vm:vmList){
            num += vm.numOfContainers();
        }
        return num;
    }


    public double getCpuRemain() {
        return cpuRemain;
    }

    public void setCpuRemain(double cpuRemain) {
        this.cpuRemain = cpuRemain;
    }

    public double getMemRemain() {
        return memRemain;
    }

    public void setMemRemain(double memRemain) {
        this.memRemain = memRemain;
    }

    public double getCpuUsage() {
        return cpuUsage;
    }

    public void setCpuUsage(double cpuUsage) {
        this.cpuUsage = cpuUsage;
    }

    public double getMemUsage() {
        return memUsage;
    }

    public void setMemUsage(double memUsage) {
        this.memUsage = memUsage;
    }
}
