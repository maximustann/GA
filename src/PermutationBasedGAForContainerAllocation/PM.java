package PermutationBasedGAForContainerAllocation;

import java.util.ArrayList;
import java.util.Collections;

public class PM implements Comparable, Cloneable{

    // The basic configuration of PM;
    private double configureCpu;
    private double configureMem;



    private double maxEnergy;
    private double k;


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

    // the current wasted resources, including unused resources and VM overheads,
    // e.g. wastedCpu = PM - total cpu used by containers
    // it is calculated as wastedCpu < wastedMem ? wastedCpu : wastedMem.
    // It is normalized by the PM's total resources
    private double wastedResource;

    // the list of VM
    private ArrayList<VM> vmList;

    // the list of Containers;
    private ArrayList<Integer> containerIdList;


    // Initialize the PM
    public PM(double configureCpu,
              double configureMem,
              double k,
              double maxEnergy){
        this.configureCpu = configureCpu;
        this.configureMem = configureMem;
        this.maxEnergy = maxEnergy;
        this.k = k;
        cpuRemain = configureCpu;
        memRemain = configureMem;
        cpuUsage = 0;
        memUsage = 0;
        cpuUtil = 0;
        memUtil = 0;
        energy = k * maxEnergy;
        vmList = new ArrayList<>();
        containerIdList = new ArrayList<>();
    }



    public ArrayList<Integer> getVmTypeList(){
        ArrayList<Integer> vmTypeList = new ArrayList<>();
        for(VM vm:vmList){
            vmTypeList.add(vm.getType());
        }
        Collections.sort(vmTypeList);
        return vmTypeList;
    }

    // Only copy the structure of the PM. The PM does not include any container yet.
    public PM copyStructure(){
        PM newPm = new PM(configureCpu, configureMem, k, maxEnergy);
        ArrayList<VM> newVmList = new ArrayList<>();
        for(VM vm:vmList){
            VM newVm = new VM(vm.getType(), vm.getConfigureCpu(), vm.getConfigureMem(), vm.getCpuOverheadRate(), vm.getMemOverhead());
            newVmList.add(newVm);
            newPm.allocate(newVm);
        }
        newPm.setVmList(newVmList);
        return newPm;
    }

    // This must be a deep copy
    // NOTICE: This code is correct but think again when you clone the entire PM.
    @Override
    public PM clone(){
        PM newPm = new PM(configureCpu, configureMem, k, maxEnergy);
        ArrayList<VM> newVmList = new ArrayList<>();
        ArrayList<Integer> newContainerIdList = new ArrayList<>();

        // 1. Clone a VM
        // 2. redirect the mother PM to the new PM
        // 3. add to the new vm list
        for(int i = 0; i < vmList.size(); i++){
            VM vm = vmList.get(i);
            VM newVm = vm.clone();
            newVm.setMotherPm(newPm);
            newVmList.add(newVm);
        }

        for(int i = 0; i < containerIdList.size(); i++){
            newContainerIdList.add(containerIdList.get(i));
        }

        newPm.setVmList(newVmList);
        newPm.setContainerIdList(newContainerIdList);
        newPm.setCpuUsage(cpuUsage);
        newPm.setMemUsage(memUsage);
        newPm.setCpuRemain(cpuRemain);
        newPm.setMemRemain(memRemain);

        return newPm;
    }

    public void setContainerIdList(ArrayList<Integer> containerIdList){
        this.containerIdList = containerIdList;
    }

    public ArrayList<Integer> getContainerIdList() {
        return containerIdList;
    }

    public void setVmList(ArrayList<VM> vmList){
        this.vmList = vmList;
    }

    public ArrayList<VM> getVmList() {
        return vmList;
    }

    public void releaseContainer(Integer containerId){
        for(int i = 0; i < vmList.size(); i++){
            VM vm = vmList.get(i);
            if(vm.getContainerIdList().contains(containerId)){
                ArrayList<Integer> containerIdList = vm.getContainerIdList();
                int containerIndex = containerIdList.indexOf(containerId);
                vm.release(containerIndex);
                return;
            }
        }
    }

    public double getCpuUtil() {
        return cpuUtil;
    }

    public double getMemUtil() {
        return memUtil;
    }

    public Integer findOwnerVmIndex(Integer containerId){
        for(int i = 0; i < vmList.size(); i++){
            VM vm = vmList.get(i);
            if(vm.getContainerIdList().contains(containerId))
                return i;
        }
        return null;
    }

    public VM findOwnerVm(Integer vmIndex){
        return vmList.get(vmIndex);
    }


    // Allocate a VM into this PM
    public void allocate(PM this, VM vm){
        // Check whether the resources are enough
        if(cpuRemain < vm.getConfigureCpu() || memRemain < vm.getConfigureMem()){
            System.out.println("PM CPU = " + cpuRemain + ", PM MEM = " + memRemain);
            System.out.println("VM CPU = " + vm.getConfigureCpu() + ", VM MEM = " + vm.getConfigureMem());
            throw new IllegalArgumentException("No enough resources in the PM");
        }


        cpuRemain -= vm.getConfigureCpu();
        memRemain -= vm.getConfigureMem();
        vmList.add(vm);
        containerIdList.addAll(vm.getContainerIdList());
        vm.setMotherPm(this);

        cpuUsage += vm.getCpuUsage();
        memUsage += vm.getMemUsage();
    }

    // Use it as a lazy function
    public void updateUtilization(){
        cpuUtil = cpuUsage / configureCpu;
        memUtil = memUsage / configureMem;
    }

    // Use it as a lazy function
    public double calEnergy(){
        updateUtilization();
        energy = k * maxEnergy + (1 - k) * maxEnergy * cpuUtil;
        return energy;
    }

    @Override
    public int compareTo(Object o) {
        PM comparedPm = (PM) o;
        if(wastedResource < comparedPm.wastedResource){
            return -1;
        } else{
            return 1;
        }
    }

    // add container's id, which is called by a VM
    public void addContainerId(Integer containerId){
        containerIdList.add(containerId);
    }

    // remove object not index
    public void removeContainerId(Integer containerId){
        containerIdList.remove(containerId);
    }

    // the current wasted resources, including unused resources and VM overheads,
    // e.g. wastedCpu = PM - total cpu used by containers
    // it is calculated as wastedCpu < wastedMem ? wastedCpu : wastedMem.
    // It is normalized by the PM's total resources
    public double updateWastedResources(){
        double totalContainerCpu = 0;
        double totalContainerMem = 0;
        for(VM vm:vmList){
            totalContainerCpu += vm.totalContainerCpu();
            totalContainerMem += vm.totalContainerMem();
        }
        double normalizedWastedCpu = (configureCpu - totalContainerCpu) / configureCpu;
        double normalizedWastedMem = (configureMem - totalContainerMem) / configureMem;
        wastedResource = normalizedWastedCpu < normalizedWastedMem ? normalizedWastedCpu:normalizedWastedMem;
        return wastedResource;
    }

    public double getWastedResource(){
        updateWastedResources();
        return wastedResource;
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
        containerIdList.removeAll(vm.getContainerIdList());
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
