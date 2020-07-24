package FirstStepALNS;

public class Container implements Cloneable{

    private int id;
    private int applicationId;
    private int microServiceId;
    private int replicaId;
    private double cpu;
    private double mem;

    //where the container has been allocated to
    private VM motherVm = null;

    // Where the container has been allocated to
    private PM pm = null;

    public Container(int id, double cpu, double mem, int applicationId, int microServiceId, int replicaId) {
        this.id = id;
        this.cpu = cpu;
        this.mem = mem;
        this.applicationId = applicationId;
        this.microServiceId = microServiceId;
        this.replicaId = replicaId;
    }

    // The mother VM will be reset by the new VM mother
    @Override
    public Container clone(){
        Container newCon = new Container(id, cpu, mem, applicationId, microServiceId, replicaId);
        return newCon;
    }

    public void setPm(PM pm) {
        this.pm = pm;
    }

    public PM getPm() {
        return pm;
    }

    public int getApplicationId() {
        return applicationId;
    }

    public int getMicroServiceId() {
        return microServiceId;
    }

    public int getReplicaId() {
        return replicaId;
    }

    public void setMotherVm(VM vm){
        motherVm = vm;
    }

    public VM getMotherVm(){
        return motherVm;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getCpu() {
        return cpu;
    }

    public void setCpu(double cpu) {
        this.cpu = cpu;
    }

    public double getMem() {
        return mem;
    }

    public void setMem(double mem) {
        this.mem = mem;
    }
}
