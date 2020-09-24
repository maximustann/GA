package NSGAII_Spread;

public class DataPack {

    // seed
    private int seed;

    // experiment related parameters
    private double vmCpuOverheadRate;
    private double vmMemOverhead;
    private double k;
    private double crushPro;
    private int vmTypes;
    private int microServiceSize;
    private int applicationSize;
    private int maximumServiceNum;


    // These are the addresses of test cases
    private String testCasePath;
    private String VMConfigPath;
    private String PMConfigPath;
    private String applicationPath;

    // Data
    private double pmCpu;
    private double pmMem;
    private double pmEnergy;
    private double[] vmMem;
    private double[] vmCpu;


    private double[] serviceCpu;
    private double[] serviceMem;
    private double[] serviceId;
    private double[] applicationId;

    private int[][] microService;

    private int maxNumOfVm;



    public DataPack(
            int seed,
            Configure configure
    ){
        this.seed = seed;
        this.vmCpuOverheadRate = configure.getVmCpuOverheadRate();
        this.vmMemOverhead = configure.getVmMemOverhead();
        this.k = configure.getK();
        this.crushPro = configure.getCrushPro();
        this.vmTypes = configure.getNumOfVmTypes();
        this.applicationSize = configure.getApplicationSize();
        this.microServiceSize = configure.getMicroServiceSize();
        this.maximumServiceNum = configure.getMaximumServiceNum();
        this.testCasePath = configure.getTestCasePath();
        this.VMConfigPath = configure.getVmConfigPath();
        this.PMConfigPath = configure.getPmConfigPath();
        this.applicationPath = configure.getApplicationPath();

        readData();
    }

    private void readData(){
        ReadFile readFile = new ReadFile(
                vmTypes, microServiceSize, applicationSize,
                testCasePath, PMConfigPath,
                VMConfigPath, applicationPath);

        ReadMicroServices readMicroServices = new ReadMicroServices(
                applicationSize,
                applicationPath);

        pmCpu = readFile.getPMCpu();
        pmMem = readFile.getPMMem();
        pmEnergy = readFile.getPMEnergy();
        vmMem = readFile.getVMMem();
        vmCpu = readFile.getVMCpu();

        serviceCpu = readFile.getServiceCpu();
        serviceMem = readFile.getServiceMem();
        serviceId = readFile.getServiceId();
        applicationId = readFile.getApplicationId();
//        containerReplicaId = readFile.getTaskReplicaId();
    }

    public int getSeed() {
        return seed;
    }

    public double getVmCpuOverheadRate() {
        return vmCpuOverheadRate;
    }

    public double getVmMemOverhead() {
        return vmMemOverhead;
    }

    public double getK() {
        return k;
    }

    public double getCrushPro() {
        return crushPro;
    }

    public int getVmTypes() {
        return vmTypes;
    }

    public int getApplicationSize() {
        return applicationSize;
    }

    public int getMaximumServiceNum() {
        return maximumServiceNum;
    }

    public String getTestCasePath() {
        return testCasePath;
    }

    public String getVMConfigPath() {
        return VMConfigPath;
    }

    public String getPMConfigPath() {
        return PMConfigPath;
    }

    public String getApplicationPath() {
        return applicationPath;
    }

    public double getPmCpu() {
        return pmCpu;
    }

    public double getPmMem() {
        return pmMem;
    }

    public double getPmEnergy() {
        return pmEnergy;
    }

    public double[] getVmMem() {
        return vmMem;
    }

    public double[] getVmCpu() {
        return vmCpu;
    }

    public double[] getServiceCpu() {
        return serviceCpu;
    }

    public double[] getServiceMem() {
        return serviceMem;
    }

    public double[] getServiceId() {
        return serviceId;
    }

    public double[] getApplicationId() {
        return applicationId;
    }

    public int[][] getMicroService() {
        return microService;
    }

    public int getMicroServiceSize() {
        return microServiceSize;
    }
    public int getMaxNumOfVm() {
        return maxNumOfVm;
    }
}
