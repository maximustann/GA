package LNS_NSGGA;

public class DataPack {

    // seed
    private int seed;


    // algorithm parameters
    private int lns_maxGen;
    private int tabuSize;
    private int improvementThreshold;
    private int lns_lbound;
    private int lns_ubound;
    private double replicaChangePercentage;
    private double allocationChangePercentage;

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
    private String replicaNumPath;

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


        this.lns_maxGen = configure.getLns_maxGen();
        this.lns_lbound = configure.getLns_lbound();
        this.lns_ubound = configure.getLns_ubound();
        this.allocationChangePercentage = configure.getAllocationChangePercentage();
        this.improvementThreshold = configure.getImprovementThreshold();
        this.tabuSize = configure.getTabuSize();
        this.replicaChangePercentage = configure.getReplicaChangePercentage();

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
        this.replicaNumPath = configure.getReplicaNumPath();

        readData();
    }

    private void readData(){
        ReadFile readFile = new ReadFile(
                vmTypes, microServiceSize, applicationSize,
                testCasePath, PMConfigPath,
                VMConfigPath, applicationPath);

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

    public int getLns_maxGen() {
        return lns_maxGen;
    }

    public int getTabuSize() {
        return tabuSize;
    }

    public int getImprovementThreshold() {
        return improvementThreshold;
    }

    public int getLns_lbound() {
        return lns_lbound;
    }

    public int getLns_ubound() {
        return lns_ubound;
    }

    public double getReplicaChangePercentage() {
        return replicaChangePercentage;
    }

    public double getAllocationChangePercentage() {
        return allocationChangePercentage;
    }

    public String getReplicaNumPath() {
        return replicaNumPath;
    }
}
