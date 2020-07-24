package GroupGA.src;

public class Container implements Cloneable{

    private int id;
    private double cpu;
    private double mem;

    //where the container has been allocated to
    private VM motherVm = null;

    public Container(int id, double cpu, double mem) {
        this.id = id;
        this.cpu = cpu;
        this.mem = mem;
    }

    // The mother VM will be reset by the new VM mother
    @Override
    public Container clone(){
        Container newCon = new Container(id, cpu, mem);
        return newCon;
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
