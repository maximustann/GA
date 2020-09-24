package GroupGA;

import GroupGA.src.GroupGA;

import java.util.ArrayList;

public class VM implements Cloneable{

    private double memOverhead;
    private double cpuOverheadRate;

    private double configureCpu;
    private double configureMem;
    private int type;

    // The usage of the VM
    private double cpuUsage;
    private double memUsage;

    // The remain of the VM
    private double cpuRemain;
    private double memRemain;

    // The PM that this VM has been allocated to
    private PM motherPm;

    // The list of containers;
    private ArrayList<Container> containerList;

    // The list of containers' id
    private ArrayList<Integer> containerIdList;

    // Initialize the VM
    public VM(
            int type,
            double configureCpu,
            double configureMem,
            double cpuOverheadRate,
            double memOverhead) {

        this.type = type;
        this.configureCpu = configureCpu;
        this.configureMem = configureMem;
        this.cpuOverheadRate = cpuOverheadRate;
        this.memOverhead = memOverhead;

        cpuUsage = configureCpu * cpuOverheadRate;
        memUsage = memOverhead;

        cpuRemain = configureCpu - cpuUsage;
        memRemain = configureMem - memUsage;

        // Initially, the VM has no mother
        motherPm = null;

        containerList = new ArrayList<>();
        containerIdList = new ArrayList<>();
    }


    // We copy everything except the motherPM, which will be updated from the PM's side
    @Override
    public VM clone(){
        VM newVm = new VM(type, configureCpu, configureMem, cpuOverheadRate, memOverhead);
//        ArrayList<Container> newConList = new ArrayList<>();
//        ArrayList<Integer> newConIdList = new ArrayList<>();
        for(int i = 0; i < numOfContainers(); i++){
//            Container container = containerList.get(i);
//            container.setMotherVm(newVm);
//            newConList.add(container.clone());
//            newConIdList.add(container.getId());
            newVm.allocate(containerList.get(i).clone());
        }
//        newVm.setContainerList(newConList);
//        newVm.setContainerIdList(newConIdList);
        newVm.setCpuRemain(cpuRemain);
        newVm.setMemRemain(memRemain);
        newVm.setCpuUsage(cpuUsage);
        newVm.setMemUsage(memUsage);
        return newVm;
    }

    public void setContainerIdList(ArrayList<Integer> containerIdList){
        this.containerIdList = containerIdList;
    }

    public ArrayList<Integer> getContainerIdList() {
        return containerIdList;
    }

    public void setContainerList(ArrayList<Container> containerList){
        this.containerList = containerList;
    }


    public double totalContainerCpu(){
        double containerCpu = configureCpu - cpuRemain - configureCpu * cpuOverheadRate;
        return containerCpu;
    }

    public double totalContainerMem(){
        double containerMem = configureMem - memRemain - memOverhead;
        return containerMem;
    }

    public void setMotherPm(PM pm){
        motherPm = pm;
    }

    // Allocate a container to this VM
    public void allocate(Container container) {

        // Check the size
        if(cpuRemain < container.getCpu() || memRemain < container.getMem()) {
            System.out.println("cpuRemain = " + cpuRemain + ", memRemain = " + memRemain);
            System.out.println("containerCpu = " + container.getCpu() + ", containerMem = " + container.getMem());
            throw new IllegalArgumentException("No enough resources for this VM");
        }

        cpuUsage += container.getCpu();
        memUsage += container.getMem();
        cpuRemain -= container.getCpu();
        memRemain -= container.getMem();


        // round
        cpuUsage = round2(cpuUsage);
        memUsage = round2(memUsage);
        cpuRemain = round2(cpuRemain);
        memRemain = round2(memRemain);

        container.setMotherVm(this);
        containerList.add(container);
        containerIdList.add(container.getId());

        // If the VM has been allocated to a PM, update the PM as well
        if(motherPm != null){
            double pmCpuUsage = motherPm.getCpuUsage() + container.getCpu();
            double pmMemUsage = motherPm.getMemUsage() + container.getMem();
            motherPm.setCpuUsage(pmCpuUsage);
            motherPm.setMemUsage(pmMemUsage);
            motherPm.addContainerId(container.getId());
        }
    }


    // Release a container by its Id
    public void releaseById(int containerId){
        for(int i = 0; i < containerList.size(); i++){
            if(containerList.get(i).getId() == containerId){
                release(i);
                break;
            }
        }
    }

    // Release a container from this VM using the index of the container
    public void release(int indexOfContainer){

        // check the index
        if(indexOfContainer >= containerList.size()) {
            throw new IllegalArgumentException("No such container");
        }

        Container container = containerList.get(indexOfContainer);
        container.setMotherVm(null);
        cpuUsage -= container.getCpu();
        memUsage -= container.getMem();
        cpuRemain += container.getCpu();
        memRemain += container.getMem();


        // round
        cpuUsage = round2(cpuUsage);
        memUsage = round2(memUsage);
        cpuRemain = round2(cpuRemain);
        memRemain = round2(memRemain);

        containerList.remove(indexOfContainer);
        //remove object, not index
        containerIdList.remove(indexOfContainer);
        if(motherPm != null){
            double pmCpuUsage = motherPm.getCpuUsage() - container.getCpu();
            double pmMemUsage = motherPm.getMemUsage() - container.getMem();
            motherPm.setCpuUsage(pmCpuUsage);
            motherPm.setMemUsage(pmMemUsage);
            motherPm.removeContainerId(container.getId());
        }
    }

    private double round2(double num){
        return Math.round(num * 100.0) / 100.0;
    }

    public double getCpuRemain(){
        return cpuRemain;
    }

    public double getMemRemain() {
        return memRemain;
    }

    public void setCpuUsage(double cpuUsage) {
        this.cpuUsage = cpuUsage;
    }

    public void setMemUsage(double memUsage) {
        this.memUsage = memUsage;
    }

    public void setCpuRemain(double cpuRemain) {
        this.cpuRemain = cpuRemain;
    }

    public void setMemRemain(double memRemain) {
        this.memRemain = memRemain;
    }

    // Number of containers
    public int numOfContainers(){
        return containerList.size();
    }

    public double getCpuUsage() {
        return cpuUsage;
    }

    public double getMemUsage(){
        return memUsage;
    }

    public ArrayList<Container> getContainerList() {
        return containerList;
    }

    public double getConfigureCpu() {
        return configureCpu;
    }

    public void setConfigureCpu(double configureCpu) {
        this.configureCpu = configureCpu;
    }

    public double getConfigureMem() {
        return configureMem;
    }

    public void setConfigureMem(double configureMem) {
        this.configureMem = configureMem;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public double getCpuOverheadRate() {
        return cpuOverheadRate;
    }

    public double getMemOverhead() {
        return memOverhead;
    }
}
